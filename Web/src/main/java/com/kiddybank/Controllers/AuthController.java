package com.kiddybank.Controllers;

import com.kiddybank.Entities.Account;
import com.kiddybank.LogicInterfaces.IAuthLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private IAuthLogic authLogic;

    @Autowired
    public AuthController(IAuthLogic authLogic) {
        this.authLogic = authLogic;
    }

    @RequestMapping(value = "/signin", method = RequestMethod.POST, produces = "application/json")
    public String LoginUser(@RequestBody Account account) {
        String token = authLogic.SignIn(account);
//        String jsonResponse = "{ \"token\":" + "\"" + token + "\"" + "}";
        String jsonResponse = String.format("{\"token\": \"%s\"}", token);
        return jsonResponse;
    }

}
