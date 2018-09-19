package DAL.Contexts;

import DAL.Interfaces.IUserContext;
import Domain.Account;

public class UserContext implements IUserContext {
    public UserContext() {
        Account GetUser(int id);
    }
}
