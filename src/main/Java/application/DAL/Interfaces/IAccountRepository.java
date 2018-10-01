package application.DAL.Interfaces;

import application.Domain.Account;
import org.springframework.data.repository.CrudRepository;

public interface IAccountRepository extends CrudRepository<Account, Integer> {
    Account findByUsername(String username);
}
