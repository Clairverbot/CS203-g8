package com.G2T8.CS203WebApp.model;

import javax.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@Entity
public class ARTTestResults {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate weeksMonday;
    private Boolean artResult;
    
    @JsonFormat(pattern = "dd MMM yyyy, HH:mm")
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

}
