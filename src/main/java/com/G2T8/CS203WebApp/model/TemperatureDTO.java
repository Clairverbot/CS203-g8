package com.G2T8.CS203WebApp.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
public class TemperatureDTO {
    @NotBlank
    private LocalDateTime date;
    @NotBlank
    private double temperature;
}
