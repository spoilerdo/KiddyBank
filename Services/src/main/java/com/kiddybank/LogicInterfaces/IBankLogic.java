package com.kiddybank.LogicInterfaces;

import com.kiddybank.Entities.Account;
import com.kiddybank.Entities.BankAccount;

public interface IBankLogic {
    /**
     * Creates Bank Account for given account parameter
     * @param account The account you want to create a bank account for
     * @return created bank account
     * @throws IllegalArgumentException if account already exists in the database
     */
    BankAccount createAccount(Account account);

    /**
     * Delete Bank Account for given account parameter
     * @param bankAccountID The ID of the account you want to delete
     * @return nothing if everything goes right
     * @throws IllegalArgumentException if the given account doesn't have a existing bank account
     */
    void deleteAccount(int bankAccountID);

    /**
     * Link a given user account to a given bank account
     * @param ownAccountID the account of the logged in user
     * @param otherAccountID the account you want to link to a bank account
     * @param bankAccountID the bank account you want to add a new user to
     * @return nothing if everything goes right
     * @throws IllegalArgumentException if the given user or bank account does not exist
     */
    void linkAnotherUserToBankAccount (int ownAccountID, int otherAccountID, int bankAccountID);

    /**
     * Get balance for given account
     * @param accountId The id of the account you want to get the balance f rom
     * @return Balance of the account
     * @throws  IllegalArgumentException if given accountid was not found in the system
     */
    Float getBalance(int accountId);

    /**
     * Send money from one account to the other
     * @param senderId the person who is sending money
     * @param receiverId the person who is receiving money
     * @param price the amount to transfer
     * @return nothing if everything goes right
     * @throws IllegalArgumentException if senderID or receiverID is not found in the system
     */
    void transaction(int senderId, int receiverId, Float price);
}
