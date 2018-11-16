package com.kiddybank.Logic;

import com.kiddybank.DataInterfaces.IRoleRepository;
import com.kiddybank.Entities.Account;
import com.kiddybank.Entities.Role;
import com.kiddybank.LogicInterfaces.IAccountLogic;
import com.kiddybank.DataInterfaces.IAccountRepository;
import org.assertj.core.util.Strings;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;


@Service
public class AccountLogic implements IAccountLogic {
    private IAccountRepository accountContext;
    private IRoleRepository roleContext;

    @Autowired
    public AccountLogic(IAccountRepository accountContext, IRoleRepository roleContext) {
        this.accountContext = accountContext;
        this.roleContext = roleContext;
    }

    @Override
    public Account getUser(int id) throws IllegalArgumentException {
        //check if account was found in the system
        Account foundAccount = checkAccountExists(id);

        //clear sensitive data
        foundAccount.setPassword("");
        return foundAccount;
    }

    @Override
    public Account getUser(String username, Principal user) throws IllegalArgumentException, AccessDeniedException {
        //only the user can access his/her own personal data
        if(!username.equals(user.getName())) {
            throw new AccessDeniedException("you do not have access to view this!");
        }
        //check if account is in the system and return it.
        Account foundAccount = checkAccountExists(username);

        //clear sensitive data
        foundAccount.setPassword("");
        return foundAccount;
    }

    @Override
    public int getUserId(String username, String password) {
        //check if account was found in the system
        Account account = checkAccountExists(username);

        return account.getId();
    }

    @Override
    public Account createUser(Account account) throws IllegalArgumentException {
        //check if account is filled correctly
        if (Strings.isNullOrEmpty(account.getUsername() )|| Strings.isNullOrEmpty(account.getPassword())|| Strings.isNullOrEmpty(account.getEmail())) {
            throw new IllegalArgumentException("Values cannot be null");
        }

        //check if account already exists in the db
        Optional<Account> accountFromDb = accountContext.findByUsername(account.getUsername());
        if(accountFromDb.isPresent()){
            throw new IllegalArgumentException("Account with: " + account.getId() + " already exists in the system");
        }

        //password encrypting
        String encryptedPassword = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());
        account.setPassword(encryptedPassword);

        //set registration date
        account.setRegistrationDate(Date.valueOf(LocalDate.now()));

        //set default role
        Role userRole = roleContext.findByName("user").get();
        account.getRoles().add(userRole);
        userRole.getUsers().add(account);

        //save to the db
        return this.accountContext.save(account);
    }

    @Override
    @Transactional
    public void deleteUser(int accountID, Principal user) throws IllegalArgumentException {
        //Check access to account
        checkAccess(user.getName(), accountID);
        //check if the account exists in the db
        checkAccountExists(accountID);

        //delete the account from the db
        this.accountContext.deleteById(accountID);
    }

    //region Generic exception methods

    private Account checkAccountExists(int accountId){
        Optional<Account> accountFromDb = accountContext.findById(accountId);
        checkAccountStatus(accountFromDb, String.valueOf(accountId), "id");

        return accountFromDb.get();
    }

    private Account checkAccountExists(String username){
        Optional<Account> accountFromDb = accountContext.findByUsername(username);
        checkAccountStatus(accountFromDb, username, "username");

        return accountFromDb.get();
    }

    private Boolean checkAccess(String username, int accountID) {
        Optional<Account> foundAccount = accountContext.findByUsername(username);
        checkAccountStatus(foundAccount, username, "username");

        Account account = foundAccount.get();

        if(account.getId() != accountID) {
            throw new AccessDeniedException("You do not have access to do this");
        }

        return true;
    }

    private void checkAccountStatus(Optional<Account> account, String accountIdentifier, String identifierSort){
        if(!account.isPresent()) {
            throw new IllegalArgumentException("Account with " + identifierSort + ": " + accountIdentifier + " not found in the system");
        }
    }

    //endregion
}
