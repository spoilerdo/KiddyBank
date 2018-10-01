package application.Controllers;

import application.Domain.Account;
import application.Logic.Interfaces.IAccountLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/account")
public class AccountController {
    private IAccountLogic _logic;

    @Autowired
    public AccountController(IAccountLogic logic) {
        this._logic = logic;
    }

    //TODO : fixen dat json gemapt wordt naar account
    @PostMapping(path="/add")
    public @ResponseBody String AddNewUser(Account account) {
        Boolean accountCreated = _logic.CreateUser(account);
        return accountCreated.toString();
    }


}
