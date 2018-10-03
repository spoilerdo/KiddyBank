package com.kiddybank.LogicInterfaces;

import com.kiddybank.Entities.Account;

public interface IAccountLogic {
    Account GetUser(int id);
    Boolean Login(Account account);
    Boolean CreateUser(Account account);
    Boolean DeleteUser(Account account);
}
