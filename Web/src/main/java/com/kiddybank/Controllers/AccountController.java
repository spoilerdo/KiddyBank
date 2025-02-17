package com.kiddybank.Controllers;

import com.kiddybank.Entities.Account;
import com.kiddybank.LogicInterfaces.IAccountLogic;
import com.kiddybank.Wrappers.createAccountRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path="/accounts")
public class AccountController {
    private IAccountLogic accountLogic;

    @Autowired
    public AccountController(IAccountLogic accountLogic) {
        this.accountLogic = accountLogic;
    }

    @PostMapping
    public ResponseEntity<Account> AddNewUser(@RequestBody createAccountRequestModel requestModel) throws IllegalArgumentException{
        Account createdAccount = accountLogic.createUser(requestModel.getUsername(), requestModel.getPassword(), requestModel.getEmail(), requestModel.getPhonenr());
        return new ResponseEntity<>(createdAccount, HttpStatus.OK);
    }

    @DeleteMapping(path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void DeleteUser(@PathVariable("id") int id, Principal user) throws IllegalArgumentException {
        accountLogic.deleteUser(id, user);
    }

    @GetMapping(value = "/{userId:[0-9]+}")
    public Account GetUser(@PathVariable("userId") int userId) {
        return accountLogic.getUser(userId);
    }

    @GetMapping(path = "/{username:[A-Za-z0-9]+}")
    public Account getUserByName(@PathVariable("username") String username) {
        return accountLogic.getUser(username);
    }

}
