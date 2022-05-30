package com.termoncs.moviemanager.auth.config;

import com.termoncs.moviemanager.auth.service.AuthUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * SecurityConfigurer updated to new component based style recommended in SpringSecurity 5.7
 *    - use of WebSecurityConfigurerAdapter has been deprecated
 *    - https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 * @author aiden
 */
@Configuration
@EnableWebSecurity
public class SecurityConfigurer {

    @Autowired
    private AuthUserDetailsService service;

    // important this is autowired so that the filters autowired dependencies are also created
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors().disable()
            .csrf().disable()
            .authorizeHttpRequests(authz -> authz.antMatchers("/authenticate").permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("ADMIN","USER")
                .antMatchers("/api/movies").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin()
            .defaultSuccessUrl("/movies", true);
        return http.build();

        // enable specific routes with no authentication
        //http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/movies").permitAll();
        //http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // NoOpPasswordEncoder has been deprecated
        return NoOpPasswordEncoder.getInstance();

        //https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html
        // requires password to be prefixed with {noop} when plain text passwords used for development/testing
        //return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

