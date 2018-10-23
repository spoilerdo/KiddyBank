package com.kiddybank.LogicInterfaces;

import com.kiddybank.Entities.Account;
import io.jsonwebtoken.Claims;

public interface IJwtLogic {
    String GenerateToken(Account account);
    boolean Verify(String token);
    Claims Decode(String token);
}
