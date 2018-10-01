package com.kiddybank.Controllers;

import com.kiddybank.Entities.Account;
import com.kiddybank.LogicInterfaces.IAccountLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/account")
public class AccountController {
    private IAccountLogic _accountLogic;

    @Autowired
    public AccountController(IAccountLogic accountLogic) {
        this._accountLogic = accountLogic;
    }

    //TODO : fixen dat json gemapt wordt naar account
    @PostMapping(path="/add")
    public Boolean AddNewUser(@RequestBody  Account account) {
        Boolean accountCreated = _accountLogic.CreateUser(account);
        return accountCreated;
    }
}
