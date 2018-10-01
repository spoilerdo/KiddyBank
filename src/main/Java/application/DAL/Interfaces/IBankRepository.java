package application.DAL.Interfaces;

import application.Domain.BankAccount;
import org.springframework.data.repository.CrudRepository;

public interface IBankRepository extends CrudRepository<BankAccount, Integer> {

}
