package com.kiddybank.Logic;

import com.kiddybank.DataInterfaces.IBankRepository;
import com.kiddybank.DataInterfaces.ITransactionRepository;
import com.kiddybank.Entities.BankAccount;
import com.kiddybank.Entities.TransactionHistory;
import com.kiddybank.LogicInterfaces.IBankLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankLogic implements IBankLogic {
    private IBankRepository _bankContext;
    private ITransactionRepository _transactionContext;

    @Autowired
    public BankLogic(IBankRepository bankContext, ITransactionRepository transactionContext) {
        this._bankContext = bankContext;
        this._transactionContext = transactionContext;
    }

    @Override
    public Float GetBalance(int accountId) {
        return _bankContext.findById(accountId).get().getBalance();
    }

    @Override
    public Boolean Transaction(int senderId, int receiverId, Float price) {
        ChangeBalance(senderId, price);
        ChangeBalance(receiverId, -price);
        _transactionContext.save(new TransactionHistory(senderId, receiverId, price));
        return true;
    }

    private void ChangeBalance (int id, Float price){
        BankAccount account = _bankContext.findById(id).get();
        account.setBalance(account.getBalance() + price);
        _bankContext.save(account);
    }
}
