package com.kiddybank.LogicInterfaces;

import com.kiddybank.Entities.BankAccount;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface IBankLogic {
    /**
     * Creates Bank Account for given account parameter
     * @param user The claim of the account you want to create a bank-account for
     * @param bankAccountNaam the name you want to give to the bankaccount
     * @return created bank-account
     * @throws IllegalArgumentException if given account doesn't exists in the db
     */
    BankAccount createAccount(Principal user, String bankAccountNaam);

    /**
     * Delete Bank Account for given account parameter
     * @param bankAccountId The Id of the account you want to delete
     * @param user The claim of the account you want to delete a bank-account for
     * @return nothing if everything goes right
     * @throws IllegalArgumentException if given account doesn't have a existing bank account
     */
    void deleteAccount(int bankAccountId, Principal user);

    /**
     * Link a given user account to a given bank account
     * @param myUsername the username of the logged in user retrieved from the jwt token claim
     * @param otherAccountId the account you want to link to a bank account
     * @param bankAccountId the bank account you want to add a new user to
     * @return nothing if everything goes right
     * @throws IllegalArgumentException if given logged-in account doesn't exist
     * @throws IllegalArgumentException if logged-in account is linked to the given bank-account
     * @throws IllegalArgumentException if other account exists
     */
    void linkAnotherUserToBankAccount (String myUsername, int otherAccountId, int bankAccountId);

    /**
     * Get balance for given account
     * @param bankAccountId The id of the bank-account you want to get the balance f rom
     * @return Balance of the account
     * @throws IllegalArgumentException if given accountId was not found in the system
     * @throws IllegalArgumentException if given account contains no bank-accounts
     */
    Float getBalance(int bankAccountId);

    /**
     * Get all bank-accounts the logged-in user
     * @param accountId The id of the logged-in user
     * @retun A list of all bank-accounts
     * @throws IllegalArgumentException if given accountId was not found in the system
     * @throws IllegalArgumentException if given account doesn't have any bank-accounts
     */
    Set<BankAccount> getBankAccounts(int accountId);

    /**
     * Send money from one account to the other
     * @param senderId the person who is sending money
     * @param receiverId the person who is receiving money
     * @param price the amount to transfer
     * @return nothing if everything goes right
     * @throws IllegalArgumentException if senderID or receiverID is not found in the system
     * @throws IllegalArgumentException if sender doesn't have enough balance
     */
    void transaction(int senderId, int receiverId, Float price);
}
