package com.G2T8.CS203WebApp.model;

import java.time.LocalDate;
import javax.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data // A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and
      // @Setter on all non-final fields, and @RequiredArgsConstructor(generate
      // constructor with args annotated with @NonNull)
@Entity
@Table(name = "Schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NonNull
    private LocalDate startDate;

    @Column(name = "end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NonNull
    private LocalDate endDate;

    // 0 for wfh, 1 for office
    @Column(name = "mode")
    @NonNull
    private int mode;

    // teamID is foreign key for the class/table Schedule
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    
    public Schedule() {

    }

    public Schedule(LocalDate startDate, LocalDate endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
