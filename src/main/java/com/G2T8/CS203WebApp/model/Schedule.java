package com.G2T8.CS203WebApp.model;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data 
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
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;
    
    public Schedule(){

    }

    public Schedule(LocalDateTime startDateTime, LocalDateTime endDateTime){
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;

    }







    



    
}
