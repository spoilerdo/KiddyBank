package com.kiddybank.Entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User{
    private int userID;

    public CustomUser(int userID, String username, String password,
                      Collection<? extends GrantedAuthority> authorities) {
        super(username,password, authorities);
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }
}
