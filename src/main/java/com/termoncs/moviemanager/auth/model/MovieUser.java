package com.termoncs.moviemanager.auth.model;

import javax.persistence.*;

/**
 * Movie Model
 * @author aiden
 */

@Entity()
@Table(name="user")
public class MovieUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;

    public MovieUser() {}

    public MovieUser(Long id, String name, String password, String role ) {
        this.setId(id).setUsername(name).setPassword(password).setRole(role);
    }

    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }

    public MovieUser setId(long id) { this.id = id; return this; }
    public MovieUser setUsername(String username) { this.username = username; return this; }
    public MovieUser setPassword(String password) { this.password = password; return this; }
    public MovieUser setRole(String role) { this.role = role; return this; }

    @Override
    public String toString() {
        return "MovieUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
