package com.kiddybank.Logic;

import com.kiddybank.Entities.Account;
import com.kiddybank.LogicInterfaces.IAccountLogic;
import com.kiddybank.DataInterfaces.IAccountRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountLogic implements IAccountLogic {
    private IAccountRepository _context;

    @Autowired
    public AccountLogic(IAccountRepository context) {
        this._context = context;
    }

    @Override
    public Account GetUser(int id) {
        return this._context.findById(id).get();
    }

    @Override
    public Boolean Login(Account account) {
        //Account in database opvragen indien deze bestaat.
        Account accountInDatabase = _context.findByUsername(account.getUsername());
        if(accountInDatabase == null) {
            return false;
        }

        if (BCrypt.checkpw(account.getPassword(), accountInDatabase.getPassword())) {
            return true;
            // TODO : VERIFICATIE TOKEN GENEREN  - TYGO

        }
        return false;
    }

    @Override
    public Boolean CreateUser(Account account) {
        String encryptedPassword = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());
        account.setPassword(encryptedPassword);
        this._context.save(account);

        //Get account in database to check it is created
        Optional<Account> accountInDatabase = this._context.findById(account.getId());
        if(!accountInDatabase.isPresent()) {
            return false;
        }

        //Account bestaat
        return true;
    }

    @Override
    public Boolean DeleteUser(Account account) {
        this._context.deleteAccountByUsername(account.getUsername());

        //Get account in database to check if it is deleted
        Optional<Account> accountInDatabase = this._context.findById(account.getId());
        if(accountInDatabase.isPresent()) {
            //Account bestaat nog, is niet gedelete
            return false;
        }
        //account is verwijderd
        return true;
    }
}
