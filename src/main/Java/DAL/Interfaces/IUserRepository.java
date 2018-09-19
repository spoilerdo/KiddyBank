package DAL.Interfaces;

import Domain.Account;
import Domain.StorageType;

import java.util.Date;

public interface IUserRepository {
    Account GetUser(int id);
    Boolean CreateUser(String username, String password, String email, String TelephoneNumber , Date DateOfBirth);
    Boolean DeleteUser(int id);
    Boolean UpdateUser(Account account);
    void ChangeType(StorageType type);
}
