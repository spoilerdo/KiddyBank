package com.kiddybank.DataInterfaces;

import com.kiddybank.Entities.BankAccount;
import org.springframework.data.repository.CrudRepository;

public interface IBankRepository extends CrudRepository<BankAccount, Integer> {
}
