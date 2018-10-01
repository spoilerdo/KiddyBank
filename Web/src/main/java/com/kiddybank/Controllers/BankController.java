package com.kiddybank.Controllers;

import com.kiddybank.LogicInterfaces.IBankLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BankController {
    private IBankLogic _bankLogic;

    @Autowired
    public BankController(IBankLogic bankLogic) {
        this._bankLogic = bankLogic;
    }


}
