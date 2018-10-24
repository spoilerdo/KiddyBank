package com.kiddybank.Logic;

import com.kiddybank.Entities.Account;
import com.kiddybank.LogicInterfaces.IAccountLogic;
import com.kiddybank.DataInterfaces.IAccountRepository;
import org.assertj.core.util.Strings;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;


@Service
public class AccountLogic implements IAccountLogic {
    private IAccountRepository context;

    @Autowired
    public AccountLogic(IAccountRepository context) {
        this.context = context;
    }

    @Override
    public Account getUser(int id) throws IllegalArgumentException {
        //check if account was found in the system
        Optional<Account> foundAccount = checkAccountExists(id);

        //return account
        return foundAccount.get();
    }

    @Override
    public Account createUser(Account account) throws IllegalArgumentException {
        //check if account is filled correctly
        if (Strings.isNullOrEmpty(account.getUsername() )|| Strings.isNullOrEmpty(account.getPassword())|| Strings.isNullOrEmpty(account.getEmail())) {
            throw new IllegalArgumentException("Values cannot be null");
        }

        //check if account already exists in the db
        Optional<Account> accountInDatabase = this.context.findByUsername(account.getUsername());
        if(accountInDatabase.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        //password encrypting
        String encryptedPassword = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());
        account.setPassword(encryptedPassword);

        //set registration date
        account.setRegistrationDate(Date.valueOf(LocalDate.now()));

        //save to the db
        Account createdUser = this.context.save(account);

        return createdUser;
    }

    @Override
    @Transactional
    public void deleteUser(int accountID) throws IllegalArgumentException {
        //check if the account exists in the db
        checkAccountExists(accountID);

        //delete the account from the db
        this.context.deleteById(accountID);
    }

    private Optional<Account> checkAccountExists(int accountId){
        Optional<Account> accountFromDb = context.findById(accountId);
        if(!accountFromDb.isPresent()) {
            throw new IllegalArgumentException("Account with id: " + String.valueOf(accountId) + " not found in the system");
        }

        return accountFromDb;
    }
}
