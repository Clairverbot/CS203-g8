package com.G2T8.CS203WebApp.model;

import lombok.*;

/**
 * DTO layer for user details
 * 
 * Used in the creation of new users, as during creation you don't supply entire
 * data about users, like vaccinationStatus, managerID, teamID, etc.
 */
@Data
public class UserDTO {
    private String email;
    private String name;
    private String password;
    private String role;
}
