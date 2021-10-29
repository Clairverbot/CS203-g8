package com.G2T8.CS203WebApp.model;

import java.util.*;
import javax.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data // A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and
      // @Setter on all non-final fields, and @RequiredArgsConstructor(generate
      // constructor with args annotated with @NonNull)
@Entity(name = "covid_his")
@Table(name = "covid_history")
public class CovidHistory {

    // primary key 
    @Id
    @Column(name = "covidHistoryid")
    private long CovidHistoryid;

    // each row can be uniquely discovered by userid 
    // hence created a findByUserId in CovidHistoryRepo
    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private User user;

    @Column(name = "contractedDate")
    @NonNull
    private LocalDateTime contractedDate;

    @Column(name = "recoverDate")
    private LocalDateTime RecoverDate;

    public CovidHistory(){
          
    }


    /*
     * 
     * @Id
     * 
     * @ManyToOne
     * 
     * @PrimaryKeyJoinColumn(name = "userid", referencedColumnName = "ID") private
     * User user;
     * 
     * @Column(name = "contractedDate")
     * 
     * @NonNull private LocalDateTime contractedDate;
     * 
     * @Column(name = "recoverDate") private LocalDateTime RecoverDate;
     * 
     * 
     */
    
}
