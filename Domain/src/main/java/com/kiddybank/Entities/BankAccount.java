package com.kiddybank.Entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bank_account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="banknumber")
    private int bankNumber;
    @Column(name="balance")
    private float balance;

<<<<<<< HEAD
   // @ManyToMany(mappedBy = "accounts")
=======
    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "bankAccounts")
>>>>>>> dffd126afd63d31112b2c8b3f39d92add36a6e3c
    private Set<Account> accounts = new HashSet<>();

    public BankAccount() {}

    public BankAccount(int id, int bankNumber, int balance) {
        this.id = id;
        this.bankNumber = bankNumber;
        this.balance = balance;
    }

<<<<<<< HEAD
    public int getBankNumber() {
        return bankNumber;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

=======
>>>>>>> dffd126afd63d31112b2c8b3f39d92add36a6e3c
    public Set<Account> getAccounts() {
        return accounts;
    }
}
