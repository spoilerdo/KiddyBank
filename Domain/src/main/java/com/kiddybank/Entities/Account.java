package com.kiddybank.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Account {
    //JsonProperty wordt toegevoegd zodat de ingebouwde json omzetter weet naar welke namen hij moet zoeken.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonProperty("name")
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
  /*  @JoinTable(
            name = "AccountBankAccount",
            joinColumns = { @JoinColumn(name = "AccountID") },
            inverseJoinColumns = { @JoinColumn(name = "BankAccountID") }
    )
    private Set<BankAccount> bankAccounts = new HashSet<>(); */


    public Account() {}

    public Account(String username, String password, String email, String phoneNumber, Date registrationDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
    }

    public String getUsername() {
        return username;
    }

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

  /*  public Set<BankAccount> getBankAccounts() {
        return bankAccounts;
    } */

}
