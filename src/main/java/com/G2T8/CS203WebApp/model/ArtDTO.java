package com.G2T8.CS203WebApp.model;

import java.time.LocalDateTime;
import lombok.*;
import javax.validation.constraints.NotBlank;

@Data
public class ArtDTO {
    @NotBlank
    private Long userId;
    @NotBlank
    private LocalDateTime weeksMonday;
    @NotBlank
    private LocalDateTime date;
    @NotBlank
    private Boolean result;
}
