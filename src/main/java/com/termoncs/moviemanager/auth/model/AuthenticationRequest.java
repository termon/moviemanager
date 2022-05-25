
package com.termoncs.moviemanager.auth.model;

/**
 *
 * @author aiden
 */
public class AuthenticationRequest {
    
    private String username;
    private String password;

    public AuthenticationRequest () {
        
    }
    public AuthenticationRequest (String username, String password) {
        this.username = username;
        this.password = password;
    }    
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    
    
}
