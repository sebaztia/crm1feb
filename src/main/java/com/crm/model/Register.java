package com.crm.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Register {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String registerToken;

    public Register(String username, String email, String password, String registerToken) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.registerToken = registerToken;
    }

    public Register() { }
}
