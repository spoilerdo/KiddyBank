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

    //Exception afvanger toevoegen
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

        //controleren of save van repository is aangeroepen
        verify(bankRepository, times(1)).save(bankAccount);

        //controleren of account en bank account aan elkaar gekoppeld zijn
        Assert.assertTrue(dummyAccount.getBankAccountFromId(bankAccount.getId()) == bankAccount);
    }

    @Test
    public void TestCreatAccountUnvalid(){
        Account dummyAccount = new Account("dummy1", "wachtwoord", "dummy@dummy.com", "0000", Date.valueOf(LocalDate.now()));

        //when(accountRepository.findById(dummyAccount.getId() + 1)).thenReturn(Optional.ofNullable(dummyAccount));

        //we verwachten dat er een exception optreed omdat het gegeven account niet bestaat
        exception.expect(IllegalArgumentException.class);

        BankAccount bankAccount = _logic.createAccount(dummyAccount);

        verify(bankRepository, times(1)).save(bankAccount);
    }

    @Test
    public void TestDeleteAccountValid(){
        BankAccount dummyAccount = new BankAccount(1, 1, 100);

        when(bankRepository.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        _logic.deleteAccount(dummyAccount.getId());

        //controleren of het bank account is deleted en dat er geen exceptions zijn
        verify(bankRepository, times(1)).deleteById(dummyAccount.getId());
    }

    @Test
    public void TestDeleteAccountUnvalid(){
        BankAccount dummyAccount = new BankAccount(1, 1, 100);

        when(bankRepository.findById(dummyAccount.getId())).thenReturn(Optional.empty());

        //We verwacthen een exception omdat hij geen bank account kan vinden
        exception.expect(IllegalArgumentException.class);

        _logic.deleteAccount(dummyAccount.getId());
    }

    @Test
    public void TestLinkAnotherUserToBankAccountValid(){
        Account dummyOwnAccount = new Account("dummy1", "wacthwoord", "dummy@dummy.com", "0000", Date.valueOf(LocalDate.now()));
        Account dummyOtherAccount = new Account("dummy2", "wachtwoord", "dummy@dummy.com", "0000", Date.valueOf(LocalDate.now()));
        BankAccount dummyBankAccount = new BankAccount(1, 1, 100);

        dummyOtherAccount.setId(1); //TODO: kan dit niet anders?

        when(accountRepository.findById(dummyOwnAccount.getId())).thenReturn(Optional.ofNullable(dummyOwnAccount));
        when(accountRepository.findById(dummyOtherAccount.getId())).thenReturn(Optional.ofNullable(dummyOtherAccount));

        dummyOwnAccount.addBankAccount(dummyBankAccount);

        _logic.linkAnotherUserToBankAccount(dummyOwnAccount.getId(), dummyOtherAccount.getId(), dummyBankAccount.getId());

        //controleren of de dummyOtherAccount een nieuw bank account heeft
        Assert.assertTrue(!dummyOtherAccount.getBankAccounts().isEmpty());
    }

    @Test
    public void TestLinkAnotherUserToBankAccountUnvalid(){
        Account dummyOwnAccount = new Account("dummy1", "wacthwoord", "dummy@dummy.com", "0000", Date.valueOf(LocalDate.now()));
        Account dummyOtherAccount = new Account("dummy2", "wachtwoord", "dummy@dummy.com", "0000", Date.valueOf(LocalDate.now()));
        BankAccount dummyBankAccount = new BankAccount(1, 1, 100);

        dummyOtherAccount.setId(1); //TODO: kan dit niet anders?

        when(accountRepository.findById(dummyOwnAccount.getId())).thenReturn(Optional.ofNullable(dummyOwnAccount));
        //when(accountRepository.findById(dummyOtherAccount.getId())).thenReturn(Optional.ofNullable(dummyOtherAccount));

        //we verwachten dat logic.linkAnotherUserToBankAccount een exception teruggooid omdat dummyOwnAccount geen bank account bevat
        exception.expect(IllegalArgumentException.class);

        _logic.linkAnotherUserToBankAccount(dummyOwnAccount.getId(), dummyOtherAccount.getId(), dummyBankAccount.getId());
    }

    @Test
    public void TestGetBalanceValidId(){
        //Given
        BankAccount dummyAccount = new BankAccount(1, 1, 100);
        when(bankRepository.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        //When
        Float balance = _logic.getBalance(1);

        //Then
        Assert.assertTrue(balance == 100);
    }

    @Test
    public void TestGetBalanceUnvalidId() {
        BankAccount dummyAccount = new BankAccount(1, 1, 1);

        //we verwachten dat logic.getbalance een exception teruggooid omdat accountid van 0 niet bestaat.
        exception.expect(IllegalArgumentException.class);

        Float balance = _logic.getBalance(0);
    }

    @Test
    public void TestTransactionValid() {
        BankAccount dummy1Account = new BankAccount(1, 1, 100);
        BankAccount dummy2Account = new BankAccount(2, 2, 110);

        when(bankRepository.findById(dummy1Account.getId())).thenReturn(Optional.ofNullable(dummy1Account));
        when(bankRepository.findById(dummy2Account.getId())).thenReturn(Optional.ofNullable(dummy2Account));

        _logic.transaction(dummy1Account.getId(), dummy2Account.getId(), 10f);

        //controleren of er een save call naar transaction repository gemaakt is met een transactionhistory klasse.
        //indien dit gebeurd is en er is geen exception opgetreden kunnen we concluderen dat de actie juist uitgevoerd is.
        verify(transactionRepository, times(1)).save(any(TransactionHistory.class));
    }

    @Test
    public void TestTransactionInvalid() {
        BankAccount dummy1Account = new BankAccount(1, 1, 100);
        //receiver bestaat in deze context niet
        when(bankRepository.findById(dummy1Account.getId())).thenReturn(Optional.ofNullable(dummy1Account));
        when(bankRepository.findById(1)).thenReturn(Optional.empty());

        //we verwachten dat er een exception optreed omdat receiver niet bestaat.
        exception.expect(IllegalArgumentException.class);

        _logic.transaction(dummy1Account.getId(), 1, 10f);
    }
}
