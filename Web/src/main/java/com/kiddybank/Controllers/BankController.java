package com.kiddybank.Controllers;

import com.kiddybank.Entities.Account;
import com.kiddybank.Entities.BankAccount;
import com.kiddybank.LogicInterfaces.IAccountLogic;
import com.kiddybank.LogicInterfaces.IBankLogic;
import com.kiddybank.Wrappers.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path="/bank")
public class BankController {
    private IBankLogic _bankLogic;

    @Autowired
    public BankController(IBankLogic bankLogic, IAccountLogic accountLogic) {
        this._bankLogic = bankLogic;
    }

    @PostMapping(path = "/create")
    public BankAccount CreateAccount(Principal user) throws IllegalArgumentException {
        return _bankLogic.createAccount(user);
    }

    @PostMapping(path = "/delete/{id}")
    public void DeleteAccount(@PathVariable("id") int id, Principal user) throws IllegalArgumentException{
        _bankLogic.deleteAccount(id, user);
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