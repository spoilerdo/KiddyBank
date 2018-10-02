package com.kiddybank.LogicInterfaces;

import com.kiddybank.Entities.Account;

public interface IAuthLogic {
    public String SignIn(Account account);
    public Boolean Verify(String token);
}
