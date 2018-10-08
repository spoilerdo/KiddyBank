package com.kiddybank.Controllers;

import com.kiddybank.Entities.Account;
import com.kiddybank.LogicInterfaces.IAccountLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.FailedLoginException;

@RestController
@RequestMapping(path="/account")
public class AccountController {
    private IAccountLogic _accountLogic;

    //todo kijken naar : http://websystique.com/spring-boot/spring-boot-rest-api-example/ voor werking responsentity, hiermee kunnen we html codes enzo bepalen.

    @Autowired
    public AccountController(IAccountLogic accountLogic) {
        this._accountLogic = accountLogic;
    }

    @PostMapping(path="/add")
    public Account AddNewUser(@RequestBody  Account account) throws IllegalArgumentException{
        return _accountLogic.createUser(account);
    }

    @DeleteMapping(path="/delete/{id}")
    public void DeleteUser(@PathVariable("id") int id) throws IllegalArgumentException {
         _accountLogic.deleteUser(id);
    }

    @PostMapping(path="/login")
    public void LoginUser(@RequestBody Account account) throws FailedLoginException {
        _accountLogic.login(account);
    }
}
