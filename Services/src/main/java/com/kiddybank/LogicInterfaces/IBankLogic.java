package com.kiddybank.LogicInterfaces;

import com.kiddybank.Entities.Account;
import com.kiddybank.Entities.BankAccount;

public interface IBankLogic {
    BankAccount CreateAccount(Account account);
    Float GetBalance(int accountId);
    Boolean Transaction(int senderId, int receiverId, Float price);
}
