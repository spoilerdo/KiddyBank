package com.kiddybank.LogicInterfaces;

import com.kiddybank.Entities.Account;

public interface IAccountLogic {
    Account GetUser(int id);
    Boolean Login(String username, String password);
    Boolean CreateUser(Account account);
    Boolean DeleteUser(int id);
}
