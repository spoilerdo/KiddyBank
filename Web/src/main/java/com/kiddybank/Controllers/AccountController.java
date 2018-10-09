package com.kiddybank.Controllers;

import com.kiddybank.Entities.Account;
import com.kiddybank.LogicInterfaces.IAccountLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.FailedLoginException;

@RestController
@RequestMapping(path="/account")
public class AccountController {
    private IAccountLogic accountLogic;


    @Autowired
    public AccountController(IAccountLogic accountLogic) {
        this.accountLogic = accountLogic;
    }

    @PostMapping(path="/add")
    public ResponseEntity<Account> AddNewUser(@RequestBody  Account account) throws IllegalArgumentException{
        Account createdAccount = _accountLogic.createUser(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.OK);
    }

    @DeleteMapping(path="/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void DeleteUser(@PathVariable("id") int id) throws IllegalArgumentException {
         _accountLogic.deleteUser(id);
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public Account GetUser(@PathVariable("userId") int userId) {
        Account account = accountLogic.GetUser(userId);
        return account;
    }
    }

    @PostMapping(path="/login")
    public boolean LoginUser(@RequestBody Account account) {
        boolean login = _accountLogic.Login(account);
        return login;
    }
}
