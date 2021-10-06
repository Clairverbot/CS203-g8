package com.G2T8.CS203WebApp.model;

import java.util.*;

import javax.persistence.*;
import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data // A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and
      // @Setter on all non-final fields, and @RequiredArgsConstructor(generate
      // constructor with args annotated with @NonNull)
@NoArgsConstructor
@Entity
@Table(name = "User")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "name", nullable = false)
    @NotEmpty
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 45)
    @NotEmpty
    @Email
    private String email;

    @Column(name = "vaccinationStatus") // , nullable = false)
    // @NonNull
    private int vaccinationStatus;

    @Column(name = "role", nullable = false)
    @NotEmpty
    private String role;

    @Column(name = "password", nullable = false)
    // @Size(min = 8, max = 30)
    @NotEmpty
    private String password;

    // Foreign key of Team class to identify which team the user is in;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teamID")
    // @NonNull
    private Team team;

    // Recursive key of User class to identify which user is managing a particular
    // user object;
    @OneToMany(mappedBy = "ManagerUser", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<User> EmployeeUsers;

    @ManyToOne
    @JoinColumn(name = "ManagerUser_Id")
    private User ManagerUser;

    // user id becomes a foreign key for class/table schedule
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules;

    // user id becomes a foreign key for CovidHistory class/table
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CovidHistory> covidHistories;

    // user id becomes a foreign key for Officerequest
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OfficeRequest> officeRequests;

    @Column(name = "firstLogin", nullable = false)
    private Boolean firstLogin; 







}
