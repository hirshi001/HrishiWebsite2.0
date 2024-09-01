package com.hirshi001.springbootmicroservices.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Value("${server.ssl.enabled:#{false}}")
    private boolean secure;

    @Autowired
    SecurityAuthHandler securityAuthHandler;



    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .requiresChannel(channel -> {
                    if (secure) {
                        channel.anyRequest().requiresSecure();
                    } else {
                        channel.anyRequest().requiresInsecure();
                    }
                });

        http.headers(headers -> {
                    headers
                            .httpStrictTransportSecurity(hsts -> hsts
                                    .includeSubDomains(true)
                                    .maxAgeInSeconds(31536000)
                            );
                    headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
                    headers.xssProtection(xssConfig -> xssConfig.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK));
                    headers.httpStrictTransportSecurity(hsts -> hsts
                            .includeSubDomains(true)
                            .maxAgeInSeconds(31536000)
                    );
                }
        );
        http.authorizeHttpRequests((authorize) ->
                authorize.requestMatchers("/projects/**").permitAll()
                        .requestMatchers("/githubrepoupdate").permitAll()

                        .requestMatchers("/libraries.json").permitAll()
                        .requestMatchers("/libScreenshot").permitAll()

                        .requestMatchers("/accounts/register/**").permitAll()
                        .requestMatchers("/accounts/users.json").authenticated()


                        .requestMatchers("/", "/login_error", "/index.html", "/sidebar", "/common/**", "/webjar/**", "/website/**").permitAll()
                        .requestMatchers("/login", "/signup").permitAll()

                        // .requestMatchers("/admin/**").hasAuthority(Role.ADMIN)
        );
        http.formLogin(
                form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/accounts/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(securityAuthHandler)
                        .failureHandler(securityAuthHandler)
                        .permitAll()
        );
        http.logout(
                logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessHandler(securityAuthHandler)
                        .permitAll()

        );

        return http.build();
    }
}

