package src.main.java.com.G2T8.CS203WebApp.model;

import javax.persistence.*;
import lombok.*;

public class Temperature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tempId;
    private LocalDateTime date;
    private double temperature;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
