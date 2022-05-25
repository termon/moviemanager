package com.termoncs.moviemanager.auth.service;

import com.termoncs.moviemanager.auth.model.MovieUser;
import com.termoncs.moviemanager.auth.model.MovieUserDetails;
import com.termoncs.moviemanager.auth.repository.MovieUserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 *
 * @author aiden
 */
@Service
public class AuthUserDetailsService implements UserDetailsService {

    @Autowired
    MovieUserJpaRepository repository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        var user = repository.findByUsername(name);
        if (user == null) {
            throw new UsernameNotFoundException(name);
        }
        return new MovieUserDetails(user.getUsername(), user.getPassword(), Arrays.asList(user.getRole()));
    }

    // Unsure if its ok to extend the UserDetailsService in this way, but works fine
    // adding a user using the underlying user repository
    public void addUser(MovieUser u) {
        repository.save(u);
    }

    public void deleteAllUsers() {
        repository.deleteAll();
    }
}
