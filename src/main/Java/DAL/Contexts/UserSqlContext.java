package DAL.Contexts;

import DAL.Interfaces.IUserContext;
import Domain.Account;

import java.sql.Connection;
import java.util.Date;

public class UserSqlContext extends DatabaseConnectionContext implements IUserContext {
    private Connection SqlConnection;

    public UserSqlContext() {
        SqlConnection = GetDatabaseConnection();
    }

    @Override
    public Account GetUser(int id) {
        return null;
    }

    @Override
    public Boolean CreateUser(String username, String password, String email, String TelephoneNumber, Date DateOfBirth) {
        return null;
    }

    @Override
    public Boolean DeleteUser(int id) {
        return null;
    }

    @Override
    public Boolean UpdateUser(Account account) {
        return null;
    }
}
