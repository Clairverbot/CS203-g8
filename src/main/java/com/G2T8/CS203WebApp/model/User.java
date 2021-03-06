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

    @Column(name = "vaccination_status", nullable = false)
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

    // user id becomes a foreign key for CovidHistory class/table
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CovidHistory> covidHistories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ARTTestResult> artTestResult;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Temperature> temperature;

    @Column(name = "first_login", nullable = false, columnDefinition = "boolean default true")
    private Boolean firstLogin;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private PasswordResetToken passwordResetToken;

    public Boolean isVaccinated() {
        if (vaccinationStatus == 2) {
            return true;
        }
        return false;
    }
}
