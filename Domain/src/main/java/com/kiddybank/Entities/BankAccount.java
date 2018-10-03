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

    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "bankAccounts")
    private Set<Account> accounts = new HashSet<>();

    public BankAccount() {}

    public BankAccount(int id, int bankNumber, int balance) {
        this.id = id;
        this.bankNumber = bankNumber;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public int getBankNumber() {
        return bankNumber;
    }

    public float getBalance() {
        return balance;
    }
    public void setBalance(float balance) {
        this.balance = balance;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }
    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }
}
