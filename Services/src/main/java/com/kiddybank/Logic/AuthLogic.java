package com.kiddybank.Logic;

import com.kiddybank.DataInterfaces.IAccountRepository;
import com.kiddybank.Entities.Account;
import com.kiddybank.Entities.CustomUser;
import com.kiddybank.Entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
public class AuthLogic implements UserDetailsService {
    private IAccountRepository context;

    @Autowired
    public AuthLogic(IAccountRepository context) {
        this.context = context;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountFromDb = context.findByUsername(username);
        //Checks if the user exists in the db
        if (!accountFromDb.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        Account account = accountFromDb.get();
        return new CustomUser(account.getId(), account.getUsername(), account.getPassword(), getUserAuthority(account.getRoles()));
    }

    private List<GrantedAuthority> getUserAuthority(final Collection<Role> userRoles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role role : userRoles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }

}