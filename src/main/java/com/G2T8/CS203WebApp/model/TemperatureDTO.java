package com.G2T8.CS203WebApp.model;

import java.time.LocalDateTime;
import lombok.*;
import javax.validation.constraints.NotBlank;

@Data
public class TemperatureDTO {
    @NotBlank
    private Long userId;
    @NotBlank   
    private double temperature;
    @NotBlank
    private LocalDateTime date;
}
