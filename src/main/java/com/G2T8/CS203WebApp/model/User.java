package com.G2T8.CS203WebApp.model;

import java.util.*;

import javax.persistence.*;
import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
public class User implements Serializable {


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

    @Column(name = "vaccinationStatus")
    private int vaccinationStatus;

    @Column(name = "role", nullable = false)
    @NotEmpty
    private String role;

    @Column(name = "password", nullable = false)
    @NotEmpty
    @Getter(onMethod = @__(@JsonIgnore))
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // Foreign key of Team class to identify which team the user is in;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teamID")
    // @NonNull
    private Team team;

    // Recursive key of User class to identify which user is managing a particular
    // user object;

    @OneToMany(mappedBy = "managerUser", orphanRemoval = true)
    private List<User> employeeUsers;

    @ManyToOne
    @JoinColumn(name = "managerUser_id")
    private User managerUser;

    // user id becomes a foreign key for class/table schedule
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Schedule> schedules;

    // user id becomes a foreign key for CovidHistory class/table
    @JsonIgnore
    @OneToMany(mappedBy = "user", orphanRemoval = false)
    private List<CovidHistory> covidHistories;

    // user id becomes a foreign key for Officerequest
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<OfficeRequest> officeRequests;

    @Column(name = "firstLogin", nullable = false, columnDefinition = "boolean default true")
    private Boolean firstLogin;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private PasswordResetToken passwordResetToken;

}
