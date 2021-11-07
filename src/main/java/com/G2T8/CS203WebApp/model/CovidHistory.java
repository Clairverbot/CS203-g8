package com.G2T8.CS203WebApp.model;

import java.util.*;
import javax.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data // A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and
      // @Setter on all non-final fields, and @RequiredArgsConstructor(generate
      // constructor with args annotated with @NonNull)
@Entity(name = "covid_his")
@Table(name = "covid_history")
public class CovidHistory {

    // primary key 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "covidHistoryid")
    private Long CovidHistoryid;

    // each row can be uniquely discovered by userid 
    // hence created a findByUserId in CovidHistoryRepo
    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private User user;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "contractedDate")
    @NonNull
    private LocalDateTime contractedDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "recoverDate")
    private LocalDateTime RecoverDate;

    public CovidHistory(){

    }
    public CovidHistory(User user,LocalDateTime contractedDate, LocalDateTime RecoverDate){
          this.user = user;
          this.contractedDate = contractedDate;
          this.RecoverDate = RecoverDate; 
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
      @Override
      public boolean equals(Object o) {
            if (this == o)
                  return true;
            if (o == null || getClass() != o.getClass())
                  return false;
            CovidHistory covidHistory = (CovidHistory) o;
            return CovidHistoryid.equals(covidHistory.CovidHistoryid) && user.getID().equals(covidHistory.user.getID()) && contractedDate.equals(covidHistory.contractedDate); 
      }

      @Override
      public int hashCode() {
            return Objects.hash(CovidHistoryid, user.getID(), contractedDate);
      }

      public Boolean recovered(){
            if(contractedDate != null && RecoverDate == null){
                  return false;
            }
            return true;
      }
    
}
