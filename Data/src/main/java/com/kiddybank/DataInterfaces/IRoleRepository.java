package com.kiddybank.DataInterfaces;

import com.kiddybank.Entities.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IRoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
