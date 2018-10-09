package com.kiddybank.Logic;

import com.kiddybank.DataInterfaces.IAccountRepository;
import com.kiddybank.Entities.Account;
import com.kiddybank.LogicInterfaces.IAuthLogic;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthLogic implements IAuthLogic {
    private IAccountRepository context;
    JwtLogic jwtLogic;

    public AuthLogic(IAccountRepository context) {
        this.context = context;
        jwtLogic = new JwtLogic();
    }



    @Override
    public String SignIn(Account account) {
        // Find account in database
        Account foundAccount = context.findByUsername(account.getUsername());

        // If no account is found return nothing
        if(foundAccount == null) {
            return null;
        }

        // When a account is found has the given password
        // and compare it to the password in the database
        if (BCrypt.checkpw(account.getPassword(), foundAccount.getPassword())) {
            String token = jwtLogic.GenerateToken(foundAccount);
            return token;
        } else {
            return null;
        }

    }

}
