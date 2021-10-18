package com.G2T8.CS203WebApp.model;

import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;
import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data // A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and
      // @Setter on all non-final fields, and @RequiredArgsConstructor(generate
      // constructor with args annotated with @NonNull)
@Entity
@Table(name = "Schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "startDateTime") @NonNull
    private LocalDateTime startDateTime;

    @Column(name = "endDateTime")
    @NonNull
    private LocalDateTime endDateTime;

    // 0 for wfh , 1 for office
    @Column(name = "mode")
    @NonNull
    private int mode;

    // userid is a foreign key for the class/table Schedule
    @ManyToOne
    @JoinColumn(name = "userID")
    private User user; 







    



    
}
