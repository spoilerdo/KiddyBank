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
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public BankAccount createAccount(Principal user, String bankAccountName) throws IllegalArgumentException {
        //check if account exists in the db
        Optional<Account> accountFromDb = checkAccountExistsByUsername(user.getName());

        //get full account data from database
        Account account = accountFromDb.get();

        //make a new bank-account
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(0);
        bankAccount.setName(bankAccountName);
        bankAccount.getAccounts().add(account);
        account.getBankAccounts().add(bankAccount);

        bankAccount = _bankContext.save(bankAccount);

        return bankAccount;
    }

    @Override
    @Transactional
    public void deleteAccount(int bankAccountId, Principal user) throws IllegalArgumentException {
        //check if user has access to bank-account
        checkHasAccessToBankAccount(user.getName(), bankAccountId);

        //delete the account from the db
        this._bankContext.deleteById(bankAccountId);
    }

    @Override
    public void linkAnotherUserToBankAccount(String myUsername, int otherAccountId, int bankAccountId) throws IllegalArgumentException {
        //check if logged-in account exists in the db
        Optional<Account> accountFromDb = checkAccountExistsByUsername(myUsername);

        //get the logged-in account
        Account ownAccount = accountFromDb.get();

        //check if the logged-in account actually has the given bank-account
        BankAccount bankAccountFromDb = ownAccount.getBankAccountFromId(bankAccountId);
        if(bankAccountFromDb == null){
            throw new IllegalArgumentException("Account with id: " + ownAccount.getId() + " isn't linked to the given bank-account");
        }

        //check if the other account exists
        Optional<Account> otherAccountFromDb = checkAccountExists(otherAccountId);


        //link the other account to the given bank-account
        otherAccountFromDb.get().addBankAccount(bankAccountFromDb);
        bankAccountFromDb.getAccounts().add(otherAccountFromDb.get());

        //save changes to database
        _bankContext.save(bankAccountFromDb);
    }

    @Override
    public Float getBalance(int bankAccountId) throws IllegalArgumentException {
        //check if bank-account exists in db
        Optional<BankAccount> bankAccountFromDb = checkBankAccountExists(bankAccountId);

        //return balance
        return bankAccountFromDb.get().getBalance();
    }

    @Override
    public Set<BankAccount> getBankAccounts(int accountId){
        //check if account exists in db
        Optional<Account> accountFromDb = checkAccountExists(accountId);

        //get account
        Account account = accountFromDb.get();

        //check if account contains any bank-accounts
        if(account.getBankAccounts().isEmpty()){
            throw new IllegalArgumentException("Account with id: " + account.getId() + "has no bank-accounts");
        }

        return account.getBankAccounts();
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
        checkAccountStatus(accountFromDb, String.valueOf(accountId), "id");

        return accountFromDb;
    }

    private Optional<Account> checkAccountExistsByUsername(String username){
        Optional<Account> accountFromDb = _accountContext.findByUsername(username);
        checkAccountStatus(accountFromDb, username, "username");

        return accountFromDb;
    }

    private Optional<BankAccount> checkBankAccountExists(int bankAccountId){
        Optional<BankAccount> bankAccountFromDb = _bankContext.findById(bankAccountId);
        checkBankAccountStatus(bankAccountFromDb, String.valueOf(bankAccountId));

        return bankAccountFromDb;
    }

    private Boolean checkHasAccessToBankAccount(String username, int bankAccountId) {
        Optional<Account> foundAccount = _accountContext.findByUsername(username);
        checkAccountStatus(foundAccount, username, "username");

        Optional<BankAccount> foundBankAccount = _bankContext.findById(bankAccountId);
        checkBankAccountStatus(foundBankAccount, String.valueOf(bankAccountId));

        BankAccount bankAccount = foundBankAccount.get();
        //Check if user is authorized within bank account
        return bankAccount.getAccounts().contains(foundAccount.get());
    }

    private void checkAccountStatus(Optional<Account> account, String accountIdentifier, String identifierSort){
        if(!account.isPresent()) {
            throw new IllegalArgumentException("Account with " + identifierSort + ": " + accountIdentifier + " not found in the system");
        }
    }

    private void checkBankAccountStatus(Optional<BankAccount> bankAccount, String accountIdentifier){
        if(!bankAccount.isPresent()) {
            throw new IllegalArgumentException("Account with id: " + accountIdentifier + " not found in the system");
        }
    }

    //endregion
}
