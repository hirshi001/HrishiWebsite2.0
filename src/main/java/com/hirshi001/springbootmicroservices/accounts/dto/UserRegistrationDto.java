package com.hirshi001.springbootmicroservices.accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRegistrationDto {

    @NotEmpty
    private String username;

    @NotEmpty
    private String name;

    @NotEmpty(message = "Email is required")
    @Email
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

}
