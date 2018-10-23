package com.kiddybank.DataInterfaces;

import com.kiddybank.Entities.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface IAccountRepository extends CrudRepository<Account, Integer> {
    Optional<Account> findByUsername(String username);
}
