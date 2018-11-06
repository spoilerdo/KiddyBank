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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.Principal;
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
        BankAccount dummyBankAccount = new BankAccount(1, "mooie account",  100);
        dummyBankAccount.getAccounts().add(dummyAccount);
        Principal mockPrincipal = Mockito.mock(Principal.class);

        when(accountRepository.findByUsername(dummyAccount.getUsername())).thenReturn(Optional.ofNullable(dummyAccount));
        when(mockPrincipal.getName()).thenReturn("dummy1");
        when(bankRepository.save(any(BankAccount.class))).thenReturn(dummyBankAccount);

        BankAccount returnedBankAccount = _logic.createAccount(mockPrincipal ,"mooie bank account");

        //check if save is called from the bankrepo
        verify(bankRepository, times(1)).save(any(BankAccount.class));

        //check if account and bank-account are connected
        Assert.assertTrue(returnedBankAccount.getAccounts().contains(dummyAccount));
    }

    @Test
    public void TestCreatAccountUnvalid(){
        Principal mockPrincipal = Mockito.mock(Principal.class);

        when(mockPrincipal.getName()).thenReturn("dummy1");

        //We expect a exception cause the given account doesn't exist
        exception.expect(IllegalArgumentException.class);

        BankAccount bankAccount = _logic.createAccount(mockPrincipal ,"mooie bank account");

        verify(bankRepository, times(1)).save(bankAccount);
    }

    @Test
    public void TestDeleteAccountValid(){
        Principal mockPrincipal = Mockito.mock(Principal.class);
        Account dummyAccount = new Account("dummy1", "wachtwoord", "dummy@dummy.com", "0000", Date.valueOf(LocalDate.now()));
        BankAccount dummyBankAccount = new BankAccount(1, "mooie bank account", 100);
        dummyAccount.getBankAccounts().add(dummyBankAccount);

        when(mockPrincipal.getName()).thenReturn("dummy1");

        when(accountRepository.findByUsername(dummyAccount.getUsername())).thenReturn(Optional.of(dummyAccount));
        when(bankRepository.findById(dummyBankAccount.getId())).thenReturn(Optional.ofNullable(dummyBankAccount));

        _logic.deleteAccount(dummyBankAccount.getId(), mockPrincipal);

        //check if the bank-account is deleted and that there are no exceptions
        verify(bankRepository, times(1)).deleteById(dummyAccount.getId());
    }

    @Test
    public void TestDeleteAccountUnvalid(){
        Principal mockPrincipal = Mockito.mock(Principal.class);
        BankAccount dummyAccount = new BankAccount(1, "mooie bank account", 100);

        when(mockPrincipal.getName()).thenReturn("dummy1");

        //We expect a exception cause the given bank-account doesn't exist
        exception.expect(IllegalArgumentException.class);

        _logic.deleteAccount(dummyAccount.getId(), mockPrincipal);
    }

    @Test
    public void TestLinkAnotherUserToBankAccountValid() {
        Account dummyOwnAccount = new Account("dummy1", "wacthwoord", "dummy@dummy.com", "0000", Date.valueOf(LocalDate.now()));
        Account dummyOtherAccount = new Account("dummy2", "wachtwoord", "dummy@dummy.com", "0000", Date.valueOf(LocalDate.now()));
        dummyOtherAccount.setId(1);
        BankAccount dummyBankAccount = new BankAccount(1, "mooie rekening", 100);

        when(accountRepository.findByUsername(dummyOwnAccount.getUsername())).thenReturn(Optional.ofNullable(dummyOwnAccount));
        when(accountRepository.findById(dummyOtherAccount.getId())).thenReturn(Optional.ofNullable(dummyOtherAccount));

        //Ads the dummy bank account to the owner's account
        dummyOwnAccount.addBankAccount(dummyBankAccount);

        _logic.linkAnotherUserToBankAccount(dummyOwnAccount.getUsername(), dummyOtherAccount.getId(), dummyBankAccount.getId());

        //check if the   dummyOtherAccount has a new bank account
        Assert.assertTrue(!dummyOtherAccount.getBankAccounts().isEmpty());
    }

    @Test
    public void TestLinkAnotherUserToBankAccountUnvalid(){
        Account dummyOtherAccount = new Account("dummy2", "wachtwoord", "dummy@dummy.com", "0000", Date.valueOf(LocalDate.now()));
        dummyOtherAccount.setId(1);
        BankAccount dummyBankAccount = new BankAccount(1, "mooie rekening", 100);


        //We expect a exception cause dummyOwnAccount doesn't contain a bank-account
        exception.expect(IllegalArgumentException.class);

        _logic.linkAnotherUserToBankAccount("nietgelinktepersoon", dummyOtherAccount.getId(), dummyBankAccount.getId());
    }

    @Test
    public void TestGetBalanceValidId(){
        BankAccount dummyAccount = new BankAccount(1, "mooie rekening", 100);

        when(bankRepository.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        Float balance = _logic.getBalance(dummyAccount.getId());

        //check if the founded balance is equal to the dummyAccount balance
        Assert.assertTrue(balance == 100);
    }

    @Test
    public void TestGetBalanceUnvalidId() {
        BankAccount dummyAccount = new BankAccount(1, "mooie rekening", 1);

        when(bankRepository.findById(dummyAccount.getId())).thenReturn(Optional.empty());

        //We expect a exception cause the given bank-account doesn't exist
        exception.expect(IllegalArgumentException.class);

        _logic.getBalance(dummyAccount.getId());
    }

    @Test
    public void TestTransactionValid() {
        BankAccount dummy1Account = new BankAccount(1, "mooie rekening", 100);
        BankAccount dummy2Account = new BankAccount(2,"mooie rekening", 110);
        dummy2Account.setId(1);

        when(bankRepository.findById(dummy1Account.getId())).thenReturn(Optional.ofNullable(dummy1Account));
        when(bankRepository.findById(dummy2Account.getId())).thenReturn(Optional.ofNullable(dummy2Account));

        _logic.transaction(dummy1Account.getId(), dummy2Account.getId(), 10f);

        //check if the transaction is saved
        verify(transactionRepository, times(1)).save(any(TransactionHistory.class));
    }

    @Test
    public void TestTransactionInvalid() {
        BankAccount dummy1Account = new BankAccount(1,"mooie rekening", 100);
        BankAccount dummy2Account = new BankAccount(2,"mooie rekening", 100);
        dummy2Account.setId(1);

        when(bankRepository.findById(dummy1Account.getId())).thenReturn(Optional.ofNullable(dummy1Account));
        when(bankRepository.findById(dummy2Account.getId())).thenReturn(Optional.empty());

        //We expect a exception cause the receiver doesn't exist;
        exception.expect(IllegalArgumentException.class);

        _logic.transaction(dummy1Account.getId(), 1, 10f);
    }
}
