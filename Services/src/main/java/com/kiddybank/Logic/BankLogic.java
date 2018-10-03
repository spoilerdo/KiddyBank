package com.kiddybank.Logic;

import com.kiddybank.DataInterfaces.IAccountRepository;
import com.kiddybank.DataInterfaces.IBankRepository;
import com.kiddybank.DataInterfaces.ITransactionRepository;
import com.kiddybank.Entities.Account;
import com.kiddybank.Entities.BankAccount;
import com.kiddybank.Entities.TransactionHistory;
import com.kiddybank.LogicInterfaces.IBankLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankLogic implements IBankLogic {
    private IBankRepository _bankContext;
    private ITransactionRepository _transactionContext;
    private IAccountRepository _accountContext;

    @Autowired
    public BankLogic(IBankRepository bankContext, ITransactionRepository transactionContext, IAccountRepository accountContext) {
        this._bankContext = bankContext;
        this._transactionContext = transactionContext;
        this._accountContext = accountContext;
    }

    @Override
    public BankAccount CreatAccount(Account account) {
        Account accountFromDb = _accountContext.findById(account.getId()).get();

        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(0);
        bankAccount.getAccounts().add(accountFromDb);
        accountFromDb.getBankAccounts().add(bankAccount);

        _bankContext.save(bankAccount);

        //TODO: als je een bank account hebt gecrëerd dan moet hij true of false terugsturen
        bankAccount.getAccounts().clear();
        return bankAccount;
    }

    @Override
    public Float GetBalance(int accountId) {
        try{
            return _bankContext.findById(accountId).get().getBalance();
        }
        catch (Exception e) {
            return null;
        }
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
