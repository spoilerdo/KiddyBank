package DAL.Repositories;

import DAL.Interfaces.IUserContext;
import DAL.Interfaces.IUserRepository;
import Domain.Account;
import Domain.StorageType;

import java.util.Date;

public class UserRepository implements IUserRepository {
    private IUserContext _context;

    public UserRepository() {
        //Met factory context opvragen

    }

    @Override
    public Account GetUser(int id) {
        return _context.GetUser(id);
    }

    @Override
    public Boolean CreateUser(String username, String password, String email, String TelephoneNumber, Date DateOfBirth) {
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
