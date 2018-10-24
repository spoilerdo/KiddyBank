package com.kiddybank.LogicTests;

import com.kiddybank.DataInterfaces.IAccountRepository;
import com.kiddybank.DataInterfaces.IBankRepository;
import com.kiddybank.DataInterfaces.ITransactionRepository;
import com.kiddybank.Entities.Account;
import com.kiddybank.Entities.BankAccount;
import com.kiddybank.Entities.TransactionHistory;
import com.kiddybank.Logic.BankLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BankLogicTest {

    //Add exception handler
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    //Mock repos
    @Mock
    private IBankRepository bankRepository;
    @Mock
    private ITransactionRepository transactionRepository;
    @Mock
    private IAccountRepository accountRepository;

    //The logic you want to test injected with the repo mocks
    @InjectMocks
    private BankLogic _logic;

    @Before //sets up the mock
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestCreateAccountValid(){
        Account dummyAccount = new Account("dummy1", "wachtwoord", "dummy@dummy.com", "0000", Date.valueOf(LocalDate.now()));

        when(accountRepository.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        BankAccount bankAccount = _logic.createAccount(dummyAccount);

        //check if save is called from the bankrepo
        verify(bankRepository, times(1)).save(bankAccount);

        //check if account and bank-account are connected
        Assert.assertTrue(dummyAccount.getBankAccountFromId(bankAccount.getId()) == bankAccount);
    }

    @Test
    public void TestCreatAccountUnvalid(){
        Account dummyAccount = new Account("dummy1", "wachtwoord", "dummy@dummy.com", "0000", Date.valueOf(LocalDate.now()));

        //We expect a exception cause the given account doesn't exist
        exception.expect(IllegalArgumentException.class);

        BankAccount bankAccount = _logic.createAccount(dummyAccount);

        verify(bankRepository, times(1)).save(bankAccount);
    }

    @Test
    public void TestDeleteAccountValid(){
        BankAccount dummyAccount = new BankAccount(1, 100);

        when(bankRepository.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        _logic.deleteAccount(dummyAccount.getId());

        //check if the bank-account is deleted and that there are no exceptions
        verify(bankRepository, times(1)).deleteById(dummyAccount.getId());
    }

    @Test
    public void TestDeleteAccountUnvalid(){
        BankAccount dummyAccount = new BankAccount(1, 100);

        when(bankRepository.findById(dummyAccount.getId())).thenReturn(Optional.empty());

        //We expect a exception cause the given bank-account doesn't exist
        exception.expect(IllegalArgumentException.class);

        _logic.deleteAccount(dummyAccount.getId());
    }

    @Test
    public void TestLinkAnotherUserToBankAccountValid(){
        Account dummyOwnAccount = new Account("dummy1", "wacthwoord", "dummy@dummy.com", "0000", Date.valueOf(LocalDate.now()));
        Account dummyOtherAccount = new Account("dummy2", "wachtwoord", "dummy@dummy.com", "0000", Date.valueOf(LocalDate.now()));
        dummyOtherAccount.setId(1);
        BankAccount dummyBankAccount = new BankAccount(1, 100);

        when(accountRepository.findById(dummyOwnAccount.getId())).thenReturn(Optional.ofNullable(dummyOwnAccount));
        when(accountRepository.findById(dummyOtherAccount.getId())).thenReturn(Optional.ofNullable(dummyOtherAccount));

        //Ads the dummy bank account to the owner's account
        dummyOwnAccount.addBankAccount(dummyBankAccount);

        _logic.linkAnotherUserToBankAccount(dummyOwnAccount.getId(), dummyOtherAccount.getId(), dummyBankAccount.getId());

        //check if the dummyOtherAccount has a new bank account
        Assert.assertTrue(!dummyOtherAccount.getBankAccounts().isEmpty());
    }

    @Test
    public void TestLinkAnotherUserToBankAccountUnvalid(){
        Account dummyOwnAccount = new Account("dummy1", "wacthwoord", "dummy@dummy.com", "0000", Date.valueOf(LocalDate.now()));
        Account dummyOtherAccount = new Account("dummy2", "wachtwoord", "dummy@dummy.com", "0000", Date.valueOf(LocalDate.now()));
        dummyOtherAccount.setId(1);
        BankAccount dummyBankAccount = new BankAccount(1, 100);

        when(accountRepository.findById(dummyOwnAccount.getId())).thenReturn(Optional.ofNullable(dummyOwnAccount));

        //We expect a exception cause dummyOwnAccount doesn't contain a bank-account
        exception.expect(IllegalArgumentException.class);

        _logic.linkAnotherUserToBankAccount(dummyOwnAccount.getId(), dummyOtherAccount.getId(), dummyBankAccount.getId());
    }

    @Test
    public void TestGetBalanceValidId(){
        BankAccount dummyAccount = new BankAccount(1, 100);

        when(bankRepository.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        Float balance = _logic.getBalance(dummyAccount.getId());

        //check if the founded balance is equal to the dummyAccount balance
        Assert.assertTrue(balance == 100);
    }

    @Test
    public void TestGetBalanceUnvalidId() {
        BankAccount dummyAccount = new BankAccount(1, 1);

        when(bankRepository.findById(dummyAccount.getId())).thenReturn(Optional.empty());

        //We expect a exception cause the given bank-account doesn't exist
        exception.expect(IllegalArgumentException.class);

        _logic.getBalance(dummyAccount.getId());
    }

    @Test
    public void TestTransactionValid() {
        BankAccount dummy1Account = new BankAccount(1, 100);
        BankAccount dummy2Account = new BankAccount(2, 110);
        dummy2Account.setId(1);

        when(bankRepository.findById(dummy1Account.getId())).thenReturn(Optional.ofNullable(dummy1Account));
        when(bankRepository.findById(dummy2Account.getId())).thenReturn(Optional.ofNullable(dummy2Account));

        _logic.transaction(dummy1Account.getId(), dummy2Account.getId(), 10f);

        //check if the transaction is saved
        verify(transactionRepository, times(1)).save(any(TransactionHistory.class));
    }

    @Test
    public void TestTransactionInvalid() {
        BankAccount dummy1Account = new BankAccount(1, 100);
        BankAccount dummy2Account = new BankAccount(2, 100);
        dummy2Account.setId(1);

        when(bankRepository.findById(dummy1Account.getId())).thenReturn(Optional.ofNullable(dummy1Account));
        when(bankRepository.findById(dummy2Account.getId())).thenReturn(Optional.empty());

        //We expect a exception cause the receiver doesn't exist;
        exception.expect(IllegalArgumentException.class);

        _logic.transaction(dummy1Account.getId(), 1, 10f);
    }
}
