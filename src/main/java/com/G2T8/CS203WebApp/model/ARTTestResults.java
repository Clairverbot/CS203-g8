package com.G2T8.CS203WebApp.model;

import javax.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
public class ARTTestResults {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long artId;
    private LocalDateTime weeksMonday;
    private Boolean artResult;
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    
}
