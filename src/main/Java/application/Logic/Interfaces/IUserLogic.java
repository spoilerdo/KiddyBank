package Logic.Interfaces;

import Domain.Account;

import java.sql.Date;
import java.time.LocalDate;

public interface IUserLogic {
    Account GetUser(int id);
    Boolean Login(String username, String password);
    Boolean CreateUser(String username, String password, String email, String TelephoneNumber , LocalDate DateOfBirth);
    Boolean DeleteUser(int id);
    Boolean UpdateUser(Account account);
}
