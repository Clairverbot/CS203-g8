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
public class ARTTestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
    
    private Boolean artResult;
    
    @JsonFormat(pattern = "dd MMM yyyy, HH:mm")
    private LocalDateTime date;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate weeksMonday;

    /**
     * Constructor for ARTTestResult
     * 
     * @param user        User that submitted the ART Test Result
     * @param artResult   Result of the ART Test
     * @param date        DateTime that the ART Test Result was submitted
     * @param weeksMonday Date of the Monday of the week that the ART Test 
     *                    was taken
     */
    public ARTTestResult(User user, Boolean artResult, LocalDateTime date, LocalDate weeksMonday) {
        this.user = user;
        this.artResult = artResult;
        this.date = date;
        this.weeksMonday = weeksMonday;
    }

}
