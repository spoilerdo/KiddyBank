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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bankNumber;
    @Column(name="name")
    private String name;
    @Column(name="balance")
    private float balance;

    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "bankAccounts")
    private Set<Account> accounts = new HashSet<>();

    public BankAccount() {}
    public BankAccount(int bankNumber, String name, int balance) {
        this.bankNumber = bankNumber;
        this.name = name;
        this.balance = balance;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
