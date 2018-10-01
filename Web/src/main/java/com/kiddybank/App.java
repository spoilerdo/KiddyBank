package com.kiddybank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Scanbasepackages om van een package hoger te scannen. zodat alle benodigde beans enzo gevonden kunnen worden.
//Indien er nieuwe packages gemaakt moeten worden MOETEN deze beginnen met com.kiddybank
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
