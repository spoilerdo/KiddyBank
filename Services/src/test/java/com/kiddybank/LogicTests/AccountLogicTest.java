package com.kiddybank.LogicTests;

import com.kiddybank.DataInterfaces.IAccountRepository;
import com.kiddybank.Entities.Account;
import com.kiddybank.Logic.AccountLogic;
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

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountLogicTest {
    @Mock
    private IAccountRepository accountRepository;

    //The logic you want to test injected with the repo mocks
    @InjectMocks
    private AccountLogic _logic;

    @Before //sets up the mock
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestAddUserValid() {
        //Given
        Account dummyAccount = new Account("Peter", "wachtwoord", "jan@live.nl", "012345", Date.valueOf(LocalDate.now()));
        when(accountRepository.findById(0)).thenReturn(Optional.of(dummyAccount));

        //Then
        this._logic.CreateUser(dummyAccount);

        //When
        verify(accountRepository, times(1)).save(dummyAccount);
    }

    @Test
    public void TestDeleteUserValid() {
        //Given
        Account dummyAccount = new Account("Peter", "", "jan@live.nl", "012345", Date.valueOf(LocalDate.now()));
        when(accountRepository.findById(0)).thenReturn(Optional.of(dummyAccount));

        //When
        this._logic.DeleteUser(dummyAccount);
        //Then
        verify(accountRepository, times(1)).deleteAccountByUsername(dummyAccount.getUsername());
    }


}
