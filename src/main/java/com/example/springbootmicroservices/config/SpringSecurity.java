package com.example.springbootmicroservices.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${server.ssl.enabled:#{false}}")
    private boolean secure;
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .requiresChannel(channel -> {
            if(secure) {
                channel.anyRequest().requiresSecure();
            }else{
                channel.anyRequest().requiresInsecure();
            }
        })
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/projects").permitAll()
                                .requestMatchers("/projects_display.json").permitAll()
                                .requestMatchers("/projects.json").permitAll()
                                .requestMatchers("/githubrepoupdate").permitAll()

                                .requestMatchers("/libraries.json").permitAll()
                                .requestMatchers("/libScreenshot").permitAll()

                                .requestMatchers("/accounts/register/**").permitAll()
                                .requestMatchers("/accounts/users").hasRole("ADMIN")

                                .requestMatchers("/index").permitAll()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/sidebar").permitAll()
                                .requestMatchers("/website/**").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/signup").permitAll()
                ).formLogin(
                        form -> form
                                .loginPage("/accounts/login")
                                .loginProcessingUrl("/accounts/login")
                                .defaultSuccessUrl("/accounts/users")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }
}
