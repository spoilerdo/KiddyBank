package com.kiddybank.LogicInterfaces;

import com.kiddybank.Entities.Account;
import javax.security.auth.login.FailedLoginException;

public interface IAccountLogic {
    /**
     * Gets user based on id given
     * @param id the id of the user you want to find
     * @return found user
     * @throws IllegalArgumentException if userID is not found in our system
     */
    Account getUser(int id);

    /**
     * Login User based on information given
     * @param account the account object translated from JSON into an Account object.
     * @return nothing if everything goes correct
     * @throws FailedLoginException if account contains wrong information
     */
    void login(Account account) throws FailedLoginException;

    /**
     * Create user with information given
     * @param account the account object translated from JSON into an Account object
     * @return created user
     * @throws IllegalArgumentException if given paremeters are already known in the system
     */
    Account createUser(Account account);

    /**
     * Delete user with information given
     * @param accountID the account id for the account to delete.
     * @return nothing if everything goes correct, in most cases the client side should already have all necessary data
     * @throws IllegalArgumentException if account was not found
     */
    void deleteUser(int accountID);
}
