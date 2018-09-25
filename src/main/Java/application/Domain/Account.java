package Domain;

import java.sql.Date;

public class Account {
    //Account parameters die we doorgeven met de JSON CALLS
    private int id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private Date regisrationDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getRegisrationDate() {
        return regisrationDate;
    }

    public void setRegisrationDate(Date regisrationDate) {
        this.regisrationDate = regisrationDate;
    }
}
