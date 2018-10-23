package com.kiddybank.Logic;

import com.kiddybank.DataInterfaces.IAccountRepository;
import com.kiddybank.Entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
        Account account = context.findByUsername(username).get();
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(account.getUsername(), account.getPassword(), emptyList());
    }
}
