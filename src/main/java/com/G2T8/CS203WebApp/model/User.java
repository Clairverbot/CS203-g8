package com.G2T8.CS203WebApp.model;

import java.util.*;

import javax.persistence.*;
import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

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
@Table(name = "User")
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
    // @Size(min = 8, max = 30)
    @NotEmpty
    @Getter(onMethod = @__(@JsonIgnore))
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // Foreign key of Team class to identify which team the user is in;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teamID")
    // @NonNull
    @JsonManagedReference
    private Team team;

    // Recursive key of User class to identify which user is managing a particular
    // user object;

    @OneToMany(mappedBy = "ManagerUser", orphanRemoval = true)
    @Getter(onMethod = @__(@JsonIgnore))
    private List<User> EmployeeUsers;

    @ManyToOne
    @JoinColumn(name = "ManagerUser_id")
    private User ManagerUser;

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

    @Column(name = "first_login", nullable = false, columnDefinition = "boolean default true")
    private Boolean firstLogin;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private PasswordResetToken passwordResetToken;

    // public void setChildrenOR(List<OfficeRequest> children) {
    // this.officeRequests.addAll(children);
    // for (OfficeRequest child : children)
    // child.setUser(this);
    // }

    // public User(Long ID, String name, String email, int vaccination_status,
    // String role, String password,
    // Boolean first_login) {
    // this.ID = ID;
    // this.name = name;
    // this.email = email;
    // this.vaccinationStatus = vaccination_status;
    // this.role = role;
    // this.password = password;
    // this.firstLogin = firstLogin;
    // }
    
    public Boolean isVaccinated(){
        if(vaccinationStatus == 2){
            return true;
        }
        return false;
    }
}
