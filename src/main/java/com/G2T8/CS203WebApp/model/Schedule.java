package com.G2T8.CS203WebApp.model;

import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;
import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data // A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and
      // @Setter on all non-final fields, and @RequiredArgsConstructor(generate
      // constructor with args annotated with @NonNull)
@Entity
@Table(name = "Schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "start_date_time") @NonNull
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    @NonNull
    private LocalDateTime endDateTime;

    // 0 for wfh , 1 for office
    @Column(name = "mode")
    @NonNull
    private int mode;

    // userid is a foreign key for the class/table Schedule
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    public Schedule(){

    }

    public Schedule(LocalDateTime startDateTime, LocalDateTime endDateTime){
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;

    }







    



    
}
