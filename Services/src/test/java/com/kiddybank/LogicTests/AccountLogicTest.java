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


//Voorbeeld : https://stackoverflow.com/questions/36001201/spring-mock-repository-does-not-work
@RunWith(MockitoJUnitRunner.class)
public class AccountLogicTest {

    //Exception afvanger toevoegen
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

        //controleren of save van repository is aangeroepen
        verify(accountRepository, times(1)).save(dummyAccount);
        //controleren of we juiste terugkrijgen, na twee gegevens geloven we het wel.
        Assert.assertEquals("Peter", createUser.getUsername());
        Assert.assertEquals("jan@live.nl", createUser.getEmail());
    }

    @Test
    public void TestAddUserInvalid() {
        //Given
        Account dummyAccount = new Account("peter", "wachtwoord", "", "012345", Date.valueOf(LocalDate.now()));
        dummyAccount.setId(0);

        //we verwachten dat er een exception optreed.
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
        dummyAccount.setId(0); //TODO: Martijn: is volgens mij niet nodig

        when(accountRepository.findById(0)).thenReturn(Optional.of(dummyAccount));

        //When
        this._logic.deleteUser(dummyAccount.getId());

        //we verwachten dat de call naar deletaccountbyid gemaakt is en dat er geen exception optreed.
        verify(accountRepository, times(1)).deleteById(dummyAccount.getId());
    }

    @Test
    public void TestDeleteUserInvalid() {
        //account die nog niet bestaat
        Account dummyAccount = new Account("Peter", "", "jan@live.nl", "012345", Date.valueOf(LocalDate.now()));
        dummyAccount.setId(0); //TODO: hier nog zo eentje
        when(accountRepository.findById(0)).thenReturn(Optional.empty());

        //We verwachten een exception van illegalargument omdat de mock nu aangeeft dat de account nog bestaat
        exception.expect(IllegalArgumentException.class);

        //when
        this._logic.deleteUser(dummyAccount.getId());

    }


}
