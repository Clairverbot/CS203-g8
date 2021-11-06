package com.G2T8.CS203WebApp.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.*;
import java.util.*;

@Data // A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and
      // @Setter on all non-final fields, and @RequiredArgsConstructor(generate
      // constructor with args annotated with @NonNull)
@Entity
@NoArgsConstructor
@Table(name = "Team")
public class Team {

    // @Autowired
    // private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TeamID;

    @Column(name = "name", nullable = false)
    @NonNull
    private String name;

    @OneToMany(mappedBy = "team")
    @JsonIgnore
    private Set<User> users;

}
