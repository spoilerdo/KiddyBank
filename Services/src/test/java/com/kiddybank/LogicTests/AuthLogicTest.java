package com.kiddybank.LogicTests;

import com.kiddybank.DataInterfaces.IAccountRepository;
import com.kiddybank.Entities.Account;
import com.kiddybank.Logic.AuthLogic;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthLogicTest {
    @Value("${jwt.key}")
    private String keyString;

    // Mock repos
    @Mock
    private IAccountRepository accountRepository;

    @InjectMocks
    private AuthLogic authLogic;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TestValidToken() {
        // Get key from string
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        // Create a dummy account
        Account dummyAccount = new Account("test", "test123", "test@test.com", "0123456789", Date.valueOf(LocalDate.now()));
        when(accountRepository.findById(dummyAccount.getId())).thenReturn(Optional.ofNullable(dummyAccount));

        String jwt = authLogic.SignIn(dummyAccount);

        try {
            Claims parsedJWT = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
            String username = parsedJWT.getSubject();

            Assert.assertEquals(dummyAccount.getUsername(), username);

        } catch (Exception e) {}


    }

}
