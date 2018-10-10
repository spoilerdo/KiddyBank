package com.kiddybank.Logic;

import com.kiddybank.Entities.Account;
import com.kiddybank.LogicInterfaces.IAccountLogic;
import com.kiddybank.DataInterfaces.IAccountRepository;
import org.assertj.core.util.Strings;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.FailedLoginException;
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
        Optional<Account> foundAccount = context.findById(id);
        //check if account was found in the system
        if(!foundAccount.isPresent()) {
            throw new IllegalArgumentException("Account with id : " + String.valueOf(id) + "not found in the system");
        }
        //return account
        return foundAccount.get();
    }

    @Override
    public Account createUser(Account account) throws IllegalArgumentException {
        //Controleren of belangrijke waardes nul zijn. TODO : Werkt nog niet
        if (Strings.isNullOrEmpty(account.getUsername() )|| Strings.isNullOrEmpty(account.getPassword())|| Strings.isNullOrEmpty(account.getEmail())) {
            throw new IllegalArgumentException("Values cannot be null");
        }

        Optional<Account> accountInDatabase = this.context.findByUsername(account.getUsername());
        if(accountInDatabase.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        //password encrypten
        String encryptedPassword = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());
        account.setPassword(encryptedPassword);

        //registration date is tijd van nu
        account.setRegistrationDate(Date.valueOf(LocalDate.now()));

        //opslaan in database en result ophalen.
        Account createdUser = this.context.save(account);

        return createdUser;
    }

    @Override
    @Transactional
    public void deleteUser(int accountID) throws IllegalArgumentException {
        //Controleren of account wel bestaat
        Optional<Account> accountInDatabase = this.context.findById(accountID);
        if(!accountInDatabase.isPresent()) {
            throw new IllegalArgumentException("Account does not exist");
        }

        //account verwijderen van database
        this.context.deleteAccountById(accountID);
    }
}
