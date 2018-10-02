package com.kiddybank.Logic;

import com.kiddybank.Entities.Account;
import com.kiddybank.LogicInterfaces.IAccountLogic;
import com.kiddybank.DataInterfaces.IAccountRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return true;
    }

    @Override
    public Boolean DeleteUser(int id) {
        this._context.deleteById(id);
        return true;
    }
}
