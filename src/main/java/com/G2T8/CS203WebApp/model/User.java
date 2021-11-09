package com.G2T8.CS203WebApp.model;

import java.util.*;
import javax.persistence.*;
import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.*;

@Data // A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and
      // @Setter on all non-final fields, and @RequiredArgsConstructor(generate
      // constructor with args annotated with @NonNull)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements Serializable {

    // private static final long serialVersionUID = 1L;

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

    @Column(name = "vaccinationStatus", nullable = false)
    // @NonNull
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
    @JoinColumn(name = "team_id")
    @JsonManagedReference
    private Team team;

    // Recursive key of User class to identify which user is managing a particular
    // user object;

    @OneToMany(mappedBy = "managerUser")
    @Getter(onMethod = @__(@JsonIgnore))
    private List<User> employeeUsers;

    @ManyToOne
    @JoinColumn(name = "manager_user_id")
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ARTTestResults> artTestResult;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Temperature> temperature;

    @Column(name = "firstLogin", nullable = false, columnDefinition = "boolean default true")
    private Boolean firstLogin;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private PasswordResetToken passwordResetToken;

}
