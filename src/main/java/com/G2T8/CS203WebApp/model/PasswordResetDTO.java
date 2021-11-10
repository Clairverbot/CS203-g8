package com.G2T8.CS203WebApp.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * DTO layer for reset password
 * 
 * Used in resetting a user password. Token is the password reset token,
 * newPassword is the SHA-1 hash from the frontend
 */
@Data
public class PasswordResetDTO {
    @NotBlank
    private String token;
    @NotBlank
    private String newPassword;
}
