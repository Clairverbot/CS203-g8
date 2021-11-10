package com.G2T8.CS203WebApp.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    @Min(value = 33, message = "Temperature should not be less than 33 degree celclius")
    @Max(value = 43, message = "Temperature should not be greater than 43 degree celclius")
    private double temperature;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
}
