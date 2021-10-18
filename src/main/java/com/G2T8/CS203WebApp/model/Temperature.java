package com.G2T8.CS203WebApp.model;

import javax.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.*;

@Data
@NoArgsConstructor
@Entity
public class Temperature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tempId;
    private LocalDateTime date;
    private double temperature;
    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;
}
