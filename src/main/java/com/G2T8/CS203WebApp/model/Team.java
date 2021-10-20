package com.G2T8.CS203WebApp.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.*;
import java.util.*;

@Data
@Entity
@Table(name = "Team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TeamID;

    @Column(name = "name", nullable = false)
    @NonNull
    private String name;

    @Transient
    @OneToMany(mappedBy = "team")
    private Set<User> users;

    public Team(String name) {
        this.name = name;
    }
}
