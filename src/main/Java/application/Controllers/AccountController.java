package Controllers;

import Factory.UserLogicFactory;
import Logic.Interfaces.IUserLogic;

import java.time.LocalDate;

public class AccountController {
    private IUserLogic _logic;

    public AccountController() {
        this._logic = UserLogicFactory.CreateLogic();
    }

    public Boolean CreateAccount() {
       Boolean accountCreated = _logic.CreateUser("admin", "admin", "admin@live.nl", "012345678910", LocalDate.now());
       return accountCreated;
    }


}
