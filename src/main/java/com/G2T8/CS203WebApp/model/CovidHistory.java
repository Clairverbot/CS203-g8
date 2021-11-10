package com.G2T8.CS203WebApp.model;

import java.util.*;
import javax.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Data // A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and
      // @Setter on all non-final fields, and @RequiredArgsConstructor(generate
      // constructor with args annotated with @NonNull)
@Entity
@Table(name = "covid_history")
public class CovidHistory {

      // primary key
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Column(name = "covid_history_id")
      private Long CovidHistoryid;

      // each row can be uniquely discovered by userid
      // hence created a findByUserId in CovidHistoryRepo
      @ManyToOne
      @JoinColumn(name = "user_id", referencedColumnName = "id")
      private User user;

      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
      @Column(name = "contracted_date")
      @NonNull
      private LocalDateTime contractedDate;

      @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
      @Column(name = "recover_date")
      private LocalDateTime recoverDate;

      public CovidHistory() {

      }

      public CovidHistory(User user, LocalDateTime contractedDate, LocalDateTime recoverDate) {
            this.user = user;
            this.contractedDate = contractedDate;
            this.recoverDate = recoverDate;
      }

      @Override
      public boolean equals(Object o) {
            if (this == o)
                  return true;
            if (o == null || getClass() != o.getClass())
                  return false;
            CovidHistory covidHistory = (CovidHistory) o;
            return CovidHistoryid.equals(covidHistory.CovidHistoryid) && user.getID().equals(covidHistory.user.getID())
                        && contractedDate.equals(covidHistory.contractedDate);
      }

      @Override
      public int hashCode() {
            return Objects.hash(CovidHistoryid, user.getID(), contractedDate);
      }

      public Boolean recovered() {
            if (contractedDate != null && recoverDate == null) {
                  return false;
            }
            return true;
      }

}
