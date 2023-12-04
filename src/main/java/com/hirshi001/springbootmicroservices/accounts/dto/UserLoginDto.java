package com.hirshi001.springbootmicroservices.accounts.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

    private String email;

    private String password;

}
