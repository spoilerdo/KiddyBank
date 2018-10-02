package com.kiddybank.LogicTests;

import com.kiddybank.DataInterfaces.IAccountRepository;
import com.kiddybank.DataInterfaces.IBankRepository;
import com.kiddybank.DataInterfaces.ITransactionRepository;
import com.kiddybank.Entities.BankAccount;
import com.kiddybank.Logic.BankLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
public class BankLogicTest {

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
        BankAccount dummyAccount = new BankAccount(1, 1, 1);
        when(bankRepository.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        //When
        Float balance = _logic.GetBalance(1);

        //Then
        Assert.assertTrue(balance >= 0);
    }

    @Test
    public void TestGetBalanceUnvalidId(){
        BankAccount dummyAccount = new BankAccount(1, 1, 1);
        when(bankRepository.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        Assert.assertNull(_logic.GetBalance(0));
    }

    @Test
    public void TestTransactionValid() {
        BankAccount dummy1Account = new BankAccount(1, 1, 1);
        BankAccount dummy2Account = new BankAccount(2, 2, 1);
        when(bankRepository.findById(dummy1Account.getId())).thenReturn(Optional.ofNullable(dummy1Account));
        when(bankRepository.findById(dummy2Account.getId())).thenReturn(Optional.ofNullable(dummy2Account));

        Assert.assertTrue(_logic.Transaction(dummy1Account.getId(), dummy2Account.getId(), 1f));

        //Assert.assertEquals(_logic.GetBalance(dummy2Account.getId()), 2f);
    }
}
