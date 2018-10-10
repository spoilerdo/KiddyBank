package com.kiddybank.LogicInterfaces;

import com.kiddybank.Entities.Account;

import javax.security.auth.login.FailedLoginException;

public interface IAuthLogic {
    String SignIn(Account account) throws FailedLoginException;
}
