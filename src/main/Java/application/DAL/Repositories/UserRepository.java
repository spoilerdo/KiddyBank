package DAL.Repositories;

import DAL.Interfaces.IUserContext;
import DAL.Interfaces.IUserRepository;
import Domain.Account;
import Domain.StorageType;

import java.sql.Date;
import java.time.LocalDate;

public class UserRepository implements IUserRepository {
    private IUserContext _context;

    public UserRepository(IUserContext context) {
        this._context = context;
    }

    @Override
    public Account GetUser(int id) {
        return _context.GetUser(id);
    }

    @Override
    public Boolean Login(String username, String password) {
        return null;
    }

    @Override
    public Boolean CreateUser(String username, String password, String email, String TelephoneNumber, LocalDate DateOfBirth) {
        return _context.CreateUser(username, password, email, TelephoneNumber, DateOfBirth);
    }

    @Override
    public Boolean DeleteUser(int id) {
        return _context.DeleteUser(id);
    }

    @Override
    public Boolean UpdateUser(Account account) {
        return _context.UpdateUser(account);
    }

    @Override
    public void ChangeType(StorageType type) {
        //Factory.changetype(type);
    }
}
