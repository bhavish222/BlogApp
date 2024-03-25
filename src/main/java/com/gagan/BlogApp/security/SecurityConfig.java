package com.gagan.BlogApp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {

        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        userDetailsManager.setUsersByUsernameQuery("SELECT username, password, true FROM users WHERE username = ?");
        userDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.username, r.role_name " +
                        "FROM users u " +
                        "JOIN roles r ON r.user_id = u.id " +
                        "WHERE u.username = ?"
        );


        return userDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/allposts", "/post/{postId}", "/api/**",
                                        "/register").permitAll()
                                .requestMatchers("/newpost").hasAnyRole("AUTHOR", "ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .successForwardUrl("/chalgya")
                                .permitAll()
                )
                .logout(logout -> logout.permitAll()
                );
        httpSecurity.httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

}
