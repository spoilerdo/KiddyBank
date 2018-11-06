package com.kiddybank.Controllers;

import com.kiddybank.Entities.Account;
import com.kiddybank.LogicInterfaces.IAccountLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.FailedLoginException;
import java.security.Principal;

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
        Account createdAccount = accountLogic.createUser(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.OK);
    }

    @DeleteMapping(path="/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void DeleteUser(@PathVariable("id") int id, Principal user) throws IllegalArgumentException {
        accountLogic.deleteUser(id, user);
    }

    @RequestMapping(value = "/{userId:[0-9]+}", method = RequestMethod.GET)
    public Account GetUser(@PathVariable("userId") int userId) {
        Account account = accountLogic.getUser(userId);
        return account;
    }

    @GetMapping(path = "/{username:[A-Za-z]+}")
    public Account getUserByName(@PathVariable("username") String username, Principal user) {
        Account account = accountLogic.getUser(username, user);
        return account;
    }
}
