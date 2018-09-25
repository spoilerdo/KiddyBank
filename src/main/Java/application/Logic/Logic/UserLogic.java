package Logic.Logic;

import DAL.Interfaces.IUserRepository;
import Domain.Account;
import Logic.Interfaces.IUserLogic;

import java.sql.Date;
import java.time.LocalDate;

public class UserLogic implements IUserLogic {
    private IUserRepository _context;

    public UserLogic(IUserRepository context) {
        this._context = context;
    }

    @Override
    public Account GetUser(int id) {
        return this._context.GetUser(id);
    }

    @Override
    public Boolean Login(String username, String password) {
        //WACHTWOORD ENCRYPTEN MET BCRYPT
        return null;
    }

    @Override
    public Boolean CreateUser(String username, String password, String email, String TelephoneNumber, LocalDate DateOfBirth) {
        //WACHTWOORD ENCRYPTEN MET BCRYPT
        return this._context.CreateUser(username, password, email, TelephoneNumber, DateOfBirth);
    }

    @Override
    public Boolean DeleteUser(int id) {
        return this._context.DeleteUser(id);
    }

    @Override
    public Boolean UpdateUser(Account account) {
        return this._context.UpdateUser(account);
    }
}
