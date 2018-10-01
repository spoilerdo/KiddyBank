package application.Logic.Interfaces;

import application.Domain.Account;

import java.time.LocalDate;

public interface IAccountLogic {
    Account GetUser(int id);
    Boolean Login(String username, String password);
    Boolean CreateUser(Account account);
    Boolean DeleteUser(int id);
}
