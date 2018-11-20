package com.kiddybank.LogicInterfaces;

import com.kiddybank.Entities.Account;
import org.springframework.security.access.AccessDeniedException;
import java.security.Principal;

public interface IAccountLogic {
    /**
     * Gets user based on id given
     * @param id the id of the user you want to find
     * @return found user
     * @throws IllegalArgumentException if userID is not found in our system
     */
    Account getUser(int id);
    /**
     * Gets user based on username given, this one gives you the sensitive data so we check the claim to make sure you have access to this and block it otherwise.
     * @param username the username of the user you want to find
     * @param user The claim of the user, we use this to check if you are or have access to the sensitive data of this user.
     * @return found user
     * @throws IllegalArgumentException if userID is not found in our system
     * @throws AccessDeniedException if claim has no right to access this users data
     */
    Account getUser(String username, Principal user);
    /**
     * Gets user id based on given username and password
     * @param username the username of the user you want to find
     * @param password the password of the user you want to find
     * @return found user id
     * @throws IllegalArgumentException if account is not found in the system
     */
    int getUserId(String username, String password);
    /**
     * Create user with information given
     * @param username the username of the account you want to create
     * @param password the password of the account you want to create
     * @param email the email of the account you want to create
     * @param phoneNumber the phone number of the account you want to create
     * @return created user
     * @throws IllegalArgumentException if given parameters are already known in the system
     */
    Account createUser(String username, String password, String email, String phoneNumber);

    /**
     * Delete user with information given
     * @param accountID the account id for the account to delete.
     * @return nothing if everything goes correct, in most cases the client side should already have all necessary data
     * @throws IllegalArgumentException if account was not found
     */
    void deleteUser(int accountID, Principal user);
}
