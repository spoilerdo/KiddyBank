package com.kiddybank.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiddybank.Entities.Account;
import com.kiddybank.Entities.CustomUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.kiddybank.Security.SecurityConstants.SecurityConstants.JWTKEY;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try{
            Account creds = new ObjectMapper()
                    .readValue(req.getInputStream(), Account.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        //generate jwt token with issued date and expiration date of issued date + 1
        Date expirationDate = Date.valueOf(LocalDate.now().plusDays(1));
        Date currentDate = Date.valueOf(LocalDate.now());

        CustomUser user = (CustomUser)auth.getPrincipal();
        //get username and make a claim with roles
        String subject = user.getUsername();
        Claims claim = Jwts.claims().setSubject(subject);
        claim.put("userID", user.getUserID());
        claim.put("scopes", auth.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));

        //build token
        String token =  Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .setClaims(claim)
                .signWith(SignatureAlgorithm.HS512, JWTKEY)
                .compact();

        //Convert token to json and return to the user
        JSONObject tokenResponse = new JSONObject();
        try {
            tokenResponse.put("token", token);
        } catch (JSONException e) {
            logger.error(e.getMessage());
        }

        //Write token response to body
        res.getWriter().print(tokenResponse);
    }

}
