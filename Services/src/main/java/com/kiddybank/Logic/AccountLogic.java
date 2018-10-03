package com.kiddybank.Logic;

import com.kiddybank.Entities.Account;
import com.kiddybank.LogicInterfaces.IAccountLogic;
import com.kiddybank.DataInterfaces.IAccountRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountLogic implements IAccountLogic {
    private IAccountRepository context;

    @Autowired
    public AccountLogic(IAccountRepository context) {
        this.context = context;
    }

    @Override
    public Account GetUser(int userId) {
        Account account = this.context.findById(userId).get();
        account.setPassword("");
        return account;
    }

    @Override
    public Boolean CreateUser(Account account) {
        String encryptedPassword = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());
        account.setPassword(encryptedPassword);
        this.context.save(account);
        return true;
    }

    @Override
    public Boolean DeleteUser(Account account) {
        this._context.deleteAccountByUsername(account.getUsername());
        return true;
    }
}
