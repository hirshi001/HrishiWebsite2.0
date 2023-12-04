package com.hirshi001.springbootmicroservices.accounts.dto;

import com.hirshi001.springbootmicroservices.accounts.entity.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfoDto {

    public Integer id;

    public String username;

    public String email;

    public List<String> roles;
}
