package com.kiddybank.LogicInterfaces;

import com.kiddybank.Entities.Account;
import com.kiddybank.Entities.BankAccount;

public interface IBankLogic {
    /**
     * Creates Bank Account for given account parameter
     * @param account The account you want to create a bank-account for
     * @return created bank-account
     * @throws IllegalArgumentException if given account doesn't exists in the db
     * //TODO: mag een account meerdere bankaccounts aanmaken met nul saldo?? miss moeten we dit voorkomen
     */
    BankAccount createAccount(Account account);

    /**
     * Delete Bank Account for given account parameter
     * @param bankAccountId The Id of the account you want to delete
     * @return nothing if everything goes right
     * @throws IllegalArgumentException if given account doesn't have a existing bank account
     */
    void deleteAccount(int bankAccountId);

    /**
     * Link a given user account to a given bank account
     * @param ownAccountId the account of the logged in user
     * @param otherAccountId the account you want to link to a bank account
     * @param bankAccountId the bank account you want to add a new user to
     * @return nothing if everything goes right
     * @throws IllegalArgumentException if given logged-in account doesn't exist
     * @throws IllegalArgumentException if logged-in account is linked to the given bank-account
     * @throws IllegalArgumentException if other account exists
     */
    void linkAnotherUserToBankAccount (int ownAccountId, int otherAccountId, int bankAccountId);

    /**
     * Get balance for given account
     * @param accountId The id of the account you want to get the balance f rom
     * @return Balance of the account
     * @throws  IllegalArgumentException if given accountID was not found in the system
     * @throws IllegalArgumentException if given account contains no bank-accounts
     */
    Float getBalance(int accountId);

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
