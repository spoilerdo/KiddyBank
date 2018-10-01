package application.Domain;

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

   // @ManyToMany(mappedBy = "accounts")
   // private Set<Account> accounts = new HashSet<>();

    public BankAccount() {}

    public BankAccount(int bankNumber, int balance) {
        this.bankNumber = bankNumber;
        this.balance = balance;
    }

    /*public Set<Account> getAccounts() {
        return accounts;
    } */
}
