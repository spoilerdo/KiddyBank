package com.kiddybank.Controllers;

import com.kiddybank.Entities.BankAccount;
import com.kiddybank.LogicInterfaces.IBankLogic;
import com.kiddybank.Wrappers.TransactionResponse;
import com.kiddybank.Wrappers.createRequestModel;
import com.kiddybank.Wrappers.linkRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path="/bank")
public class BankController {
    private IBankLogic _bankLogic;

    @Autowired
    public BankController(IBankLogic bankLogic) {
        this._bankLogic = bankLogic;
    }

    @PostMapping
    public BankAccount CreateAccount(Principal user, @RequestBody createRequestModel requestModel) throws IllegalArgumentException {
        return _bankLogic.createAccount(user, requestModel.getName());
    }

    @PostMapping(path = "/{id}")
    public void DeleteAccount(@PathVariable("id") int id, Principal user) throws IllegalArgumentException{
        _bankLogic.deleteAccount(id, user);
    }

    @PostMapping(path = "/link")
    public void LinkAnotherUserToBankAccount(Principal user, linkRequestModel linkRequestModel) throws IllegalArgumentException {
        _bankLogic.linkAnotherUserToBankAccount(user.getName(), linkRequestModel.getOtherID(), linkRequestModel.getBankID());
    }

    @GetMapping(path = "/balance/{id}")
    public Float GetBalance(@PathVariable("id") int id) throws IllegalArgumentException {
        return _bankLogic.getBalance(id);
    }

    @GetMapping(path = "/all/{id}")
    public Set<BankAccount> GetBankAccounts(@PathVariable("id") int accountId) throws IllegalArgumentException{
        return _bankLogic.getBankAccounts(accountId);
    }

    @PostMapping(path = "/transfer")
    @ResponseStatus(HttpStatus.OK)
    public void Transaction (@RequestBody TransactionResponse response) throws IllegalArgumentException {
        _bankLogic.transaction(response.getSenderId(), response.getReceiverId(), response.getPrice());
    }
}