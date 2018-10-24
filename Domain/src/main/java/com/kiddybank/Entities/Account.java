package com.kiddybank.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Account {
    //JsonProperty added so the build-in json converter knows what name to look for.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("email")
    private String email;
    @Column(name="phonenumber")
    @JsonProperty("phonenr")
    private String phoneNumber;
    @Column(name="dateofbirth")
    @JsonProperty("regdate")
    private Date registrationDate;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "AccountBankAccount",
            joinColumns = { @JoinColumn(name = "AccountID") },
            inverseJoinColumns = { @JoinColumn(name = "BankAccountID") }
    )
    private Set<BankAccount> bankAccounts = new HashSet<>();


    public Account() {}
    public Account(String username, String password, String email, String phoneNumber, Date registrationDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Set<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    @JsonIgnore
    public BankAccount getBankAccountFromId(int ID) throws IllegalArgumentException {
        for (Iterator<BankAccount> Iterator = bankAccounts.iterator(); Iterator.hasNext(); ){
            BankAccount bankAccount = Iterator.next();
            if(bankAccount.getId() == ID)
                return bankAccount;
        }

        throw new IllegalArgumentException("Can't find the given bank account");
    }

    @JsonIgnore
    public void addBankAccount(BankAccount bankAccount){
        bankAccounts.add(bankAccount);
    }
}
