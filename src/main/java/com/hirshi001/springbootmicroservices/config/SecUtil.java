package com.hirshi001.springbootmicroservices.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Vector;

@Component
public class SecUtil
{
    public static List<String> sec_get_grants()
    {
        List<String> _ret = new Vector<>();
        for(GrantedAuthority _a : SecurityContextHolder.getContext().getAuthentication().getAuthorities())
        {
            _ret.add(_a.getAuthority().toUpperCase());
        }
        return _ret;
    }

    public static String sec_subject_name()
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
