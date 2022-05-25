package com.termoncs.moviemanager.auth.config;

import com.termoncs.moviemanager.auth.service.AuthUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author aiden
 */
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private AuthUserDetailsService service;
    
    // important this is autowired so that the filters autowired dependencies are also created
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().disable()
            .csrf().disable() 		
            .authorizeRequests()
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("ADMIN","USER")
                .antMatchers("/api/movies").permitAll()
                .antMatchers("/").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .defaultSuccessUrl("/movies", true);
//            .and()
//            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        
        // enable specific routes with no authentication
        //http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/movies").permitAll();   
       
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        
    }
    
    // ** Needed by SpringBoot 2.0 - AuthenticationManager no longer provided by default **
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    // needed as we are returning a plain password and it does not need encdoded
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    
    
}
