package com.G2T8.CS203WebApp.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonBackReference
    private List<User> users;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private List<Schedule> schedules;

}
