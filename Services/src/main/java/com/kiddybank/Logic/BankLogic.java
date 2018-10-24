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

import javax.transaction.Transactional;
import java.util.List;
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
        //check if account exists in the db
        Optional<Account> accountFromDb = checkAccountExists(account.getId());

        //get full account data from database
        account = accountFromDb.get();

        //make a new bank-account
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(0);
        bankAccount.getAccounts().add(account);
        account.getBankAccounts().add(bankAccount);

        _bankContext.save(bankAccount);

        //delete account connections trough JPA before returning it.
        bankAccount.getAccounts().clear();
        return bankAccount;
    }

    @Override
    @Transactional
    public void deleteAccount(int bankAccountId) throws IllegalArgumentException {
        //check if bank-account exists in the db
        checkBankAccountExists(bankAccountId);

        //delete the account from the db
        this._bankContext.deleteById(bankAccountId);
    }

    @Override
    public void linkAnotherUserToBankAccount(int ownAccountId, int otherAccountId, int bankAccountId) throws IllegalArgumentException {
        //check if logged-in account exists in the db
        Optional<Account> accountFromDb = checkAccountExists(ownAccountId);

        //get the logged-in account
        Account ownAccount = accountFromDb.get();

        //check if the logged-in account actually has the given bank-account
        BankAccount bankAccountFromDb = ownAccount.getBankAccountFromId(bankAccountId);
        if(bankAccountFromDb == null){
            throw new IllegalArgumentException("Account with id: " + String.valueOf(ownAccount.getId()) + " isn't linked to the given bank-account");
        }

        //check if the other account exists
        Optional<Account> otherAccountFromDb = checkAccountExists(otherAccountId);

        //link the other account to the given bank-account
        otherAccountFromDb.get().addBankAccount(bankAccountFromDb);
    }

    @Override
    public Float getBalance(int bankAccountId) throws IllegalArgumentException {
        //check if bank-account exists in db
        Optional<BankAccount> bankAccountFromDb = checkBankAccountExists(bankAccountId);

        //return balance
        return bankAccountFromDb.get().getBalance();
    }

    @Override
    public List<BankAccount> getBankAccounts(int accountId){
        //check if account exists in db
        Optional<Account> accountFromDb = checkAccountExists(accountId);

        //get account
        Account account = accountFromDb.get();

        //check if account contains any bank-accounts
        if(account.getBankAccounts().isEmpty()){
            throw new IllegalArgumentException("Account with id: " + String.valueOf(account.getId()) + "has no bank-accounts");
        }

        return (List<BankAccount>) account.getBankAccounts();
    }

    @Override
    public void transaction(int senderId, int receiverId, Float price) throws IllegalArgumentException{
        //check if the sender and receiver bank-accounts exist
        Optional<BankAccount> senderAccountInDatabase = checkBankAccountExists(senderId);
        Optional<BankAccount> receiverAccountInDatabase = checkBankAccountExists(receiverId);

        //get bank-accounts
        BankAccount senderBankAccount = senderAccountInDatabase.get();
        BankAccount receiverBankAccount = receiverAccountInDatabase.get();

        //check if the sender has enough balance
        if(senderBankAccount.getBalance() < price) {
            throw new IllegalArgumentException("Sender doesn't have enough balance");
        }

        //change balance on sender and receiver account
        changeBalance(senderBankAccount, -price);
        changeBalance(receiverBankAccount, price);
        //save in transaction history
        _transactionContext.save(new TransactionHistory(senderId, receiverId, price));
    }

    private void changeBalance (BankAccount account, Float price){
        //update balance from the given bank-account
        account.setBalance(account.getBalance() + price);
        _bankContext.save(account);
    }

    //region Generic exception methods
    private Optional<Account> checkAccountExists(int accountId){
        Optional<Account> accountFromDb = _accountContext.findById(accountId);
        if(!accountFromDb.isPresent()) {
            throw new IllegalArgumentException("Account with id : " + String.valueOf(accountId) + "Not found in the system");
        }

        return accountFromDb;
    }

    private Optional<BankAccount> checkBankAccountExists(int bankAccountId){
        Optional<BankAccount> bankAccountFromDb = _bankContext.findById(bankAccountId);
        if(!bankAccountFromDb.isPresent()) {
            throw new IllegalArgumentException("Bank-account with id : " + String.valueOf(bankAccountId) + "not found in the system");
        }

        return bankAccountFromDb;
    }
    //endregion
}
