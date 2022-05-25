
package com.termoncs.moviemanager.auth.model;

/**
 *
 * @author aiden
 */
public class AuthenticationResponse {
    
    private final String jwt;

    public AuthenticationResponse (String jwt) {
        this.jwt = jwt;
    }    

    /**
     * @return the jwt
     */
    public String getJwt() {
        return jwt;
    }
   
}
