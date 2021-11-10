package com.G2T8.CS203WebApp.model;
import java.util.*;
import javax.persistence.*;
import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data // A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and
      // @Setter on all non-final fields, and @RequiredArgsConstructor(generate
      // constructor with args annotated with @NonNull)
@Entity
@Table(name = "officerequest")
public class OfficeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "Approved", nullable = false)
    @NonNull
    private boolean Approved;

    @Column(name = "startDateTime")
    @NonNull
    private LocalDateTime startDateTimeOffice;

    @Column(name = "endDateTime")
    @NonNull
    private LocalDateTime endDateTimeOffice;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; 
}
