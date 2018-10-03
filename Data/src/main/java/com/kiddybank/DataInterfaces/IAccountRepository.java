package com.kiddybank.DataInterfaces;

import com.kiddybank.Entities.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface IAccountRepository extends CrudRepository<Account, Integer> {
    Account findByUsername(String username);
    void deleteAccountByUsername(String username);
}
