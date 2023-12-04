package com.hirshi001.springbootmicroservices.config;

import com.hirshi001.springbootmicroservices.accounts.authentication.NormalAuthenticationProvider;
import com.hirshi001.springbootmicroservices.accounts.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private OAuth2UserService userDetailsService;

    @Value("${server.ssl.enabled:#{false}}")
    private boolean secure;

    @Autowired
    private NormalAuthenticationProvider normalAuthenticationProvider;

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
                        authorize.requestMatchers("/projects/**").permitAll()
                                .requestMatchers("/projects_display.json").permitAll()
                                .requestMatchers("/projects.json").permitAll()
                                .requestMatchers("/githubrepoupdate").permitAll()

                                .requestMatchers("/libraries.json").permitAll()
                                .requestMatchers("/libScreenshot").permitAll()

                                .requestMatchers("/accounts/register/**").permitAll()
                                .requestMatchers("/accounts/users.json").authenticated()

                                .requestMatchers("/index").permitAll()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/sidebar").permitAll()
                                .requestMatchers("/website/**").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/signup").permitAll()

                                .requestMatchers("/admin/**").hasAuthority(Role.ADMIN)
                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/accounts/login")
                                .defaultSuccessUrl("/")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()

                );/*.oauth2ResourceServer(
                        oauth2 -> oauth2
                                .jwt(Customizer.withDefaults())
                ).oauth2Login(
                        oauth2 -> oauth2
                                .loginPage("/login")
                                .userInfoEndpoint(
                                        userInfo -> userInfo
                                                .userService(userDetailsService)
                                )
                );
                    */

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(normalAuthenticationProvider);
    }
}
