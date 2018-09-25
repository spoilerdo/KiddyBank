package DAL.Contexts;

import DAL.Interfaces.IUserContext;
import Domain.Account;

import java.sql.*;
import java.time.LocalDate;

public class UserSqlContext extends DatabaseConnectionContext implements IUserContext {
    private Connection SqlConnection;

    //SQLCONNECTION IS NULL WHY THE FUCK MOTHERFUCKER
    public UserSqlContext() {
        SqlConnection = GetDatabaseConnection();
    }

    @Override
    public Account GetUser(int id) {
        try{
            CallableStatement getUserStatement = SqlConnection.prepareCall("{call GetUser(?)}");
            getUserStatement.setInt(1, id);

            ResultSet results = getUserStatement.executeQuery();

            Account account = new Account();
            while(results.next()) {
                account.setId(results.getInt(1));
                account.setUsername(results.getString(2));
                account.setPassword(results.getString(3));
                account.setEmail(results.getString(4));
                account.setPhoneNumber(results.getString(5));
                account.setRegisrationDate(results.getDate(6));
            }

            return account;

        } catch(SQLException e) {
            return null;
        }
    }

    @Override
    public Boolean Login(String username, String password) {
        //NOG MAKEN
        return false;
    }

    @Override
    public Boolean CreateUser(String username, String password, String email, String TelephoneNumber, LocalDate DateOfBirth) {
        try{
            CallableStatement createUserStatement = SqlConnection.prepareCall("{call Register(?,?,?,?,?)}");
            createUserStatement.setString(1, username);
            createUserStatement.setString(2, password);
            createUserStatement.setString(3, email);
            createUserStatement.setString(4, TelephoneNumber);
            createUserStatement.setDate(5, Date.valueOf(DateOfBirth));

            createUserStatement.executeQuery();

            return createUserStatement.getUpdateCount() == 1;
        } catch(SQLException e) {
            return false;
        }
    }

    @Override
    public Boolean DeleteUser(int id) {
        try {
            CallableStatement deleteUserStatement = SqlConnection.prepareCall("{call DeleteUser(?}");
            deleteUserStatement.setInt(1, id);
            deleteUserStatement.executeQuery();

            return deleteUserStatement.getUpdateCount() == 1;

        } catch(SQLException e) {
            //we fucked up
            return false;
        }

    }

    @Override
    public Boolean UpdateUser(Account account) {
        throw new UnsupportedOperationException();
    }
}
