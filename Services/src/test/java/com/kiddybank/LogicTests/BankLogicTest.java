package com.kiddybank.LogicTests;

import com.kiddybank.DataInterfaces.IAccountRepository;
import com.kiddybank.DataInterfaces.IBankRepository;
import com.kiddybank.DataInterfaces.ITransactionRepository;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
