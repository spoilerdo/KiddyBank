package com.kiddybank.Controllers;

import com.kiddybank.Entities.Account;
import com.kiddybank.LogicInterfaces.IBankLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/bank")
public class BankController {
    private IBankLogic _bankLogic;

    @Autowired
    public BankController(IBankLogic bankLogic) {
        this._bankLogic = bankLogic;
    }

    @PostMapping(path = "/balance")
    public Float GetBalance(@RequestBody Account account){
        return _bankLogic.GetBalance(account.getId());
    }

    //@RequestBody is maar een keer aangegeven is dit correct?
    //TODO: kijk ook of je @RequestBody wel nodig hebt.
    public Boolean Transaction (@RequestBody Account senderAccount, Account receiverAccount, Float price){
        return _bankLogic.Transaction(senderAccount.getId(), receiverAccount.getId(), price);
    }
}