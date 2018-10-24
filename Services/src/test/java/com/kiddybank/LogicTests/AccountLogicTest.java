package com.kiddybank.LogicTests;

import com.kiddybank.DataInterfaces.IAccountRepository;
import com.kiddybank.Entities.Account;
import com.kiddybank.Logic.AccountLogic;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


//Example : https://stackoverflow.com/questions/36001201/spring-mock-repository-does-not-work
@RunWith(MockitoJUnitRunner.class)
public class AccountLogicTest {

    //Add exception handler
    @Rule
    public final ExpectedException exception = ExpectedException.none();

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

        when(accountRepository.findByUsername("Peter")).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenReturn(dummyAccount);

        //Then
        Account createUser = this._logic.createUser(dummyAccount);

        //check if save is called from the accountrepo
        verify(accountRepository, times(1)).save(dummyAccount);
        //check if we get the correct values back
        Assert.assertEquals("Peter", createUser.getUsername());
        Assert.assertEquals("jan@live.nl", createUser.getEmail());
    }

    @Test
    public void TestAddUserInvalid() {
        //Given
        Account dummyAccount = new Account("peter", "wachtwoord", "", "012345", Date.valueOf(LocalDate.now()));

        //We expect a exception
        exception.expect(IllegalArgumentException.class);

        //when
        this._logic.createUser(dummyAccount);

        //Then
        verify(accountRepository, times(1)).save(dummyAccount);
    }

    @Test
    public void TestDeleteUserValid() {
        //Given
        Account dummyAccount = new Account("Peter", "", "jan@live.nl", "012345", Date.valueOf(LocalDate.now()));

        when(accountRepository.findById(0)).thenReturn(Optional.of(dummyAccount));

        //When
        this._logic.deleteUser(dummyAccount.getId());

        //we expect that a call to deleteById
        verify(accountRepository, times(1)).deleteById(dummyAccount.getId());
    }

    @Test
    public void TestDeleteUserInvalid() {
        //account that not exists yet
        Account dummyAccount = new Account("Peter", "", "jan@live.nl", "012345", Date.valueOf(LocalDate.now()));
        when(accountRepository.findById(0)).thenReturn(Optional.empty());

        //We expect a exception cause the mock says that the account doesn't exist yet
        exception.expect(IllegalArgumentException.class);

        //when
        this._logic.deleteUser(dummyAccount.getId());
    }
}
