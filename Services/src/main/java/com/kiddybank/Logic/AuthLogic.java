package com.kiddybank.Logic;

import com.kiddybank.DataInterfaces.IAccountRepository;
import com.kiddybank.Entities.Account;
import com.kiddybank.LogicInterfaces.IAuthLogic;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDate;
import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
public class AuthLogic implements IAuthLogic {
    private IAccountRepository context;
    @Value("${jwt.key}")
    private String keyString;

    public AuthLogic(IAccountRepository context) {
        this.context = context;
    }

    // TODO: generate a token and return it
    private String GenerateToken(Account account) {

        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        Date expirationDate = Date.valueOf(LocalDate.now().plusDays(30));
        Date currentDate = Date.valueOf(LocalDate.now());

        String JWT = Jwts.builder()
                .setSubject(String.valueOf(account.getUsername()))
                .setExpiration(expirationDate)
                .setIssuedAt(currentDate)
                .setId(String.valueOf(account.getId()))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();

        return JWT;

    }

    @Override
    public String SignIn(Account account) {
        // Find account in database
        Account foundAccount = context.findByUsername(account.getUsername());

        // If no account is found return nothing
        if(foundAccount == null) {
            return null;
        }

        // When a account is found has the given password
        // and compare it to the password in the database
        if (BCrypt.checkpw(account.getPassword(), foundAccount.getPassword())) {
            String token = GenerateToken(foundAccount);
            return token;

            // TODO: Tygo -> generate verification token and return it with the user

        } else {
            return null;
        }

    }

    @Override
    public Boolean Verify(String token) {
        return null;
    }

}
