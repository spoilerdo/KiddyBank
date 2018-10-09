package com.kiddybank.Logic;

import com.kiddybank.Entities.Account;
import com.kiddybank.LogicInterfaces.IJwtLogic;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Base64;

@Service
public class JwtLogic implements IJwtLogic {
    // Get keyString from application.properties
    @Value("${jwt.key}")
    private String keyString;
    private SecretKey key;

    public JwtLogic() {
        // Get key from keyString
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public String GenerateToken(Account account) {
        Date expirationDate = Date.valueOf(LocalDate.now().plusDays(1));
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

    public boolean Verify(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims Decode(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            return null;
        }
    }

}
