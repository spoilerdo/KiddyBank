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
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


//Voorbeeld : https://stackoverflow.com/questions/36001201/spring-mock-repository-does-not-work
@RunWith(MockitoJUnitRunner.class)
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
        boolean createUser = this._logic.CreateUser(dummyAccount);

        //When
        verify(accountRepository, times(1)).save(dummyAccount);
        Assert.assertEquals(true, createUser);
    }

    @Test
    public void TestAddUserInvalid() {
        //Given
        Account dummyAccount = new Account("peter", "wachtwoord", "", "012345", Date.valueOf(LocalDate.now()));
        //id 1 vooruit zetten omdat mockito niet slim is.
        Account dummyAccountDuplicate = new Account("peter", "wachtwoord", "", "012345", Date.valueOf(LocalDate.now()));
        dummyAccountDuplicate.setId(1);

        when(accountRepository.findById(0)).thenReturn(Optional.of(dummyAccount));
        //when(accountRepository.findById(1)).thenReturn(Optional.of(dummyAccountDuplicate));

        //when
        this._logic.CreateUser(dummyAccount);

        //Then
        verify(accountRepository, times(1)).save(dummyAccount);

        //Hierna testen of de createuser false returnt omdat de username niet uniek is.
        Assert.assertEquals(false, this._logic.CreateUser(dummyAccountDuplicate));
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

    @Test
    public void TestDeleteUserInvalid() {
        //account die nog niet bestaat
        Account dummyAccount = new Account("Peter", "", "jan@live.nl", "012345", Date.valueOf(LocalDate.now()));
        when(accountRepository.findById(0)).thenReturn(Optional.of(dummyAccount));
        //when
        boolean deletedUser = this._logic.DeleteUser(dummyAccount);

        //then  
        Assert.assertEquals(false, deletedUser);
    }


}
