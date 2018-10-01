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
    public Boolean Login(String username, String password) {
        Account account = this._context.findByUsername(username);
        if (BCrypt.checkpw(password, account.getPassword()))
            return true;
            //Token waarin id geencode zit
        else
            return false;
    }

    @Override
    public Boolean CreateUser(Account account) {
        String encryptedPassword = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());
        this._context.save(account);
        return true;
    }

    @Override
    public Boolean DeleteUser(int id) {
        this._context.deleteById(id);
        return true;
    }
}
