package com.kiddybank.LogicInterfaces;

import com.kiddybank.Entities.Account;

public interface IAccountLogic {
    Account GetUser(int userId);
    Boolean CreateUser(Account account);
    Boolean DeleteUser(int userId);
}
