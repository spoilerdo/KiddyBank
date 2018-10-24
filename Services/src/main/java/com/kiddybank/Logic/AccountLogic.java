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
    private IAccountRepository accountRepository;
    private IRoleRepository roleRepository;

    @Autowired
    public AccountLogic(IAccountRepository accountRepository, IRoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Account getUser(int id) throws IllegalArgumentException {
        //check if account was found in the system
        Optional<Account> foundAccount = checkAccountExists(id);

        //Handmatig password verwijderen, JSONIGNORE gaat niet omdat ik bij de andere getuser wel het wachtwoord nodig heb

        Account account = foundAccount.get();

        //clear sensitive data
        account.setPassword("");
        //return account
        return account;
    }

    @Override
    public Account getUser(String username, Principal user) throws IllegalArgumentException, AccessDeniedException {
        //only the user can access his/her own personal data
        if(!username.equals(user.getName())) {
            throw new AccessDeniedException("you do not have access to view this!");
        }
        //check if account is in the system and return it.
        Optional<Account> foundAccount = checkAccountExists(username);

        return foundAccount.get();
    }


    @Override
    public Account createUser(Account account) throws IllegalArgumentException {
        //check if account is filled correctly
        if (Strings.isNullOrEmpty(account.getUsername() )|| Strings.isNullOrEmpty(account.getPassword())|| Strings.isNullOrEmpty(account.getEmail())) {
            throw new IllegalArgumentException("Values cannot be null");
        }

        //check if account already exists in the db
        Optional<Account> accountInDatabase = this.accountRepository.findByUsername(account.getUsername());
        if(accountInDatabase.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        //password encrypting
        String encryptedPassword = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());
        account.setPassword(encryptedPassword);

        //set registration date
        account.setRegistrationDate(Date.valueOf(LocalDate.now()));
        //set default role
        Role userRole = roleRepository.findByName("user").get();
        account.getRoles().add(userRole);
        userRole.getUsers().add(account);

        //save to the db
        Account createdUser = this.accountRepository.save(account);

        return createdUser;
    }

    @Override
    @Transactional
    public void deleteUser(int accountID, Principal user) throws IllegalArgumentException {
        //Check access to account
        checkAccess(user.getName(), accountID);
        //check if the account exists in the db
        checkAccountExists(accountID);

        //delete the account from the db
        this.accountRepository.deleteById(accountID);
    }

    private Optional<Account> checkAccountExists(int accountId){
        Optional<Account> accountFromDb = accountRepository.findById(accountId);
        if(!accountFromDb.isPresent()) {
            throw new IllegalArgumentException("Account with id: " + String.valueOf(accountId) + " not found in the system");
        }

        return accountFromDb;
    }

    private Boolean checkAccess(String username, int accountID) {
        Optional<Account> foundAccount = accountRepository.findByUsername(username);
        if(!foundAccount.isPresent()) {
            throw new IllegalArgumentException("Account with username: " + username + " not found in the system");
        }

        Account account = foundAccount.get();

        if(account.getId() != accountID) {
            throw new AccessDeniedException("You do not have access to do this");
        }

        return true;
    }

    private Optional<Account> checkAccountExists(String username){
        Optional<Account> accountFromDb = accountRepository.findByUsername(username);
        if(!accountFromDb.isPresent()) {
            throw new IllegalArgumentException("Account with username: " + String.valueOf(username) + " not found in the system");
        }

        return accountFromDb;
    }
}
