package application.Domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Account {
    //Account parameters die we doorgeven met de JSON CALLS
    //   @JsonProperty("username")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    @Column(name="phonenumber")
    private String phoneNumber;
    @Column(name="dateofbirth")
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
