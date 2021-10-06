package com.G2T8.CS203WebApp.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
public class ARTDTO {
    @NotBlank
    private LocalDateTime weeksMonday;
    @NotBlank
    private Boolean artResult;
    @NotBlank
    private LocalDateTime date;
}
