package com.kiddybank.DataInterfaces;

import com.kiddybank.Entities.TransactionHistory;
import org.springframework.data.repository.CrudRepository;

public interface ITransactionRepository extends CrudRepository<TransactionHistory, Integer> {
}
