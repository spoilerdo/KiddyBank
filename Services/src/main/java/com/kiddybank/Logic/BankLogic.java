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

import java.util.Optional;

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
    public BankAccount createAccount(Account account) throws IllegalArgumentException {
        Optional<Account> accountFromDb = _accountContext.findById(account.getId());

        if(!accountFromDb.isPresent()) {
            throw new IllegalArgumentException("Account with id : " + String.valueOf(account.getId()) + "Not found in the system");
        }
        //Get full account data from database
        account = accountFromDb.get();


        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(0);
        bankAccount.getAccounts().add(account);
        account.getBankAccounts().add(bankAccount);

        _bankContext.save(bankAccount);

        //accounts connectie via JPA weghalen voor het weergeven naar gebruiker, dit is niet relevant bij het creeren van de gebruiker
        bankAccount.getAccounts().clear();
        return bankAccount;
    }

    @Override
    public Float getBalance(int accountId) {
        Optional<BankAccount> bankAccountInDatabase = _bankContext.findById(accountId);
        //controleren of bank account gevonden is.
        if(!bankAccountInDatabase.isPresent()) {
            throw new IllegalArgumentException("bankaccount with id : " + String.valueOf(accountId) + "not found in the system");
        }

        //balance teruggeven
        return bankAccountInDatabase.get().getBalance();
    }

    @Override
    public void transaction(int senderId, int receiverId, Float price) throws IllegalArgumentException{
        //Sender en receiver account opvragen
        Optional<BankAccount> senderAccountInDatabase = _bankContext.findById(senderId);
        Optional<BankAccount> receiverAccountInDatabase = _bankContext.findById(receiverId);

        //tegelijkertijd controleren of deze accounts bestaan, anders word mogelijk al geld toegevoegd / verwijderd voordat gecontroleerd is of allebei de accounts bestaan.
        if(!senderAccountInDatabase.isPresent() || !receiverAccountInDatabase.isPresent()) {
            throw new IllegalArgumentException("Sender or Receiver account not found in database");
        }

        //bankaccounts ophalen van optional.
        BankAccount sender = senderAccountInDatabase.get();
        BankAccount receiver = receiverAccountInDatabase.get();

        //controleren of sender genoeg balans heeft
        if(sender.getBalance() < price) {
            throw new IllegalArgumentException("Sender heeft niet genoeg balans");
        }

        //balans aanpassen bij sender en receiver.
        changeBalance(sender, -price);
        changeBalance(receiver, price);
        //Opslaan in transaction historie.
        _transactionContext.save(new TransactionHistory(senderId, receiverId, price));
    }

    private void changeBalance (BankAccount account, Float price){
        //Balans updaten
        account.setBalance(account.getBalance() + price);
        _bankContext.save(account);
    }
}
