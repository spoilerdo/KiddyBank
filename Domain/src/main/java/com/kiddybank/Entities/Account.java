package com.kiddybank.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Account {
    //JsonProperty wordt toegevoegd zodat de ingebouwde json omzetter weet naar welke namen hij moet zoeken.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonProperty("name")
    private String username;
    @JsonProperty("password")
    @JsonIgnore
    private String password;
    @JsonProperty("email")
    private String email;
    @Column(name="phonenumber")
    @JsonProperty("phonenr")
    private String phoneNumber;
    @Column(name="dateofbirth")
    @JsonProperty("regdate")
    private Date registrationDate;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "AccountBankAccount",
            joinColumns = { @JoinColumn(name = "AccountID") },
            inverseJoinColumns = { @JoinColumn(name = "BankAccountID") }
    )
    @JsonIgnore
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    @JsonIgnore
    public Set<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

}
