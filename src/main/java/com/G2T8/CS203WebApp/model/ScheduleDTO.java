package com.G2T8.CS203WebApp.model;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * DTO layer for schedule
 * 
 * Used for creating and updating schedule
 */
@Data
public class ScheduleDTO {
  private Long scheduleId;
  @NotBlank
  private Long teamId;
  @NotBlank
  private int mode;
  @NotBlank
  private LocalDate startDate;
  @NotBlank
  private LocalDate endDate;
}
