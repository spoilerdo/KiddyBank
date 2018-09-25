package DAL.Interfaces;

import Domain.Account;
import Domain.StorageType;

import java.sql.Date;
import java.time.LocalDate;

public interface IUserRepository {
    Account GetUser(int id);
    Boolean Login(String username, String password);
    Boolean CreateUser(String username, String password, String email, String TelephoneNumber , LocalDate DateOfBirth);
    Boolean DeleteUser(int id);
    Boolean UpdateUser(Account account);
    void ChangeType(StorageType type);
}
