package com.kiddybank.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @JsonBackReference
    @ManyToMany(mappedBy = "roles")
    private Set<Account> users = new HashSet<>();

    public Role() {

    }
    public Role(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<Account> getUsers() {
        return users;
    }
}
