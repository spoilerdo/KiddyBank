package com.kiddybank.Controllers;

import com.kiddybank.Entities.BankAccount;
import com.kiddybank.LogicInterfaces.IBankLogic;
import com.kiddybank.Wrappers.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/bank")
public class BankController {
    private IBankLogic _bankLogic;

    @Autowired
    public BankController(IBankLogic bankLogic) {
        this._bankLogic = bankLogic;
    }

    @PostMapping(path = "/create/{id}")
    public BankAccount CreateAccount(@PathVariable("id") int id, @RequestBody BankAccount bankAccount) throws IllegalArgumentException {
        return _bankLogic.createAccount(id, bankAccount);
    }

    @PostMapping(path = "/delete/{id}")
    public void DeleteAccount(@PathVariable("id") int id) throws IllegalArgumentException{
        _bankLogic.deleteAccount(id);
    }

    @PostMapping(path = "/link/{ownID}/{otherID}/{bankID}")
    public void LinkAnotherUserToBankAccount(@PathVariable("ownID") int ownID, @PathVariable("otherID") int otherID, @PathVariable("bankID") int bankID) throws IllegalArgumentException {
        _bankLogic.linkAnotherUserToBankAccount(ownID, otherID, bankID);
    }

    @GetMapping(path = "/balance/{id}")
    public Float GetBalance(@PathVariable("id") int id) throws IllegalArgumentException {
        return _bankLogic.getBalance(id);
    }

    @PostMapping(path = "/all/{id}")
    public List<BankAccount> GetBankAccounts(@PathVariable("id") int id) throws IllegalArgumentException{
        return _bankLogic.getBankAccounts(id);
    }

    @PostMapping(path = "/transfer")
    public void Transaction (@RequestBody TransactionResponse response) throws IllegalArgumentException {
        _bankLogic.transaction(response.getSenderID(), response.getReceiverID(), response.getPrice());
    }
}