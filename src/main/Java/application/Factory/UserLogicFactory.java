package Factory;

import DAL.Contexts.UserSqlContext;
import DAL.Repositories.UserRepository;
import Logic.Interfaces.IUserLogic;
import Logic.Logic.UserLogic;

public class UserLogicFactory {
    public static IUserLogic CreateLogic() {
        return new UserLogic(new UserRepository(new UserSqlContext()));
    }

    public static IUserLogic CreateTestLogic() {
        throw new UnsupportedOperationException();
        //return new UserLogic(new UserRepository(new UserMemoryContext())));
    }

}
