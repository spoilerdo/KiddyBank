package com.kiddybank.Controllers;

import com.kiddybank.Entities.Account;
import com.kiddybank.LogicInterfaces.IAccountLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/account")
public class AccountController {
    private IAccountLogic accountLogic;

    @Autowired
    public AccountController(IAccountLogic accountLogic) {
        this.accountLogic = accountLogic;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Boolean AddNewUser(@RequestBody  Account account) {
        Boolean accountCreated = accountLogic.CreateUser(account);
        return accountCreated;
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public Account GetUser(@PathVariable("userId") int userId) {
        Account account = accountLogic.GetUser(userId);
        return account;
    }

    @PostMapping(path="/delete")
    public Boolean DeleteUser(@RequestBody Account account) {
        return accountLogic.DeleteUser(account);
    }

}
