package com.mativ707.Peluqueria.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/").permitAll()
//                        .requestMatchers("/logincheck").permitAll()
//                        .requestMatchers("/inicio").hasAnyRole("ADMIN", "USER")
//                        .requestMatchers("/turnos/**").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/user/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> {
                            formLogin
                                    .loginPage("/") // Login page will be accessed through this endpoint. We will create a controller method for this.
                                    .loginProcessingUrl("/logincheck") // This endpoint will be mapped internally. This URL will be our Login form post action.
                                    .usernameParameter("email")
                                    .passwordParameter("password")
                                    .permitAll()
                                    .defaultSuccessUrl("/inicio") // If the login is successful, user will be redirected to this URL.
                                    .failureUrl("/?error=true"); // If the user fails to login, application will redirect the user to this endpoint
                        }
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/"));

        return http.build();
    }

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }


}
