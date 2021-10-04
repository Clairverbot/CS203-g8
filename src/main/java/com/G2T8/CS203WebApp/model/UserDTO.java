package com.G2T8.CS203WebApp.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * DTO layer for user details
 * 
 * Used in the creation of new users, as during creation you don't supply entire
 * data about users, like vaccinationStatus, managerID, teamID, etc.
 */
@Data
public class UserDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String name;
    @NotBlank
    private String password;
    @NotBlank
    private String role;
}
