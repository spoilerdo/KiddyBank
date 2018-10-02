package com.kiddybank.LogicInterfaces;

public interface IBankLogic {
    Float GetBalance(int accountId);
    Boolean Transaction(int senderId, int receiverId, Float price);
}
