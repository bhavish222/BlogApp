package com.gagan.BlogApp.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;

    public User() {}

    public User(String name, String email,String password) {
        this.name = name;
        this.password = password;
        this.email=email;
    }

    public User(Integer id, String name,String email, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email=email;

    }
}
