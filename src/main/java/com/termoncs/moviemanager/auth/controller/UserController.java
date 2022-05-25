
package com.termoncs.moviemanager.auth.controller;

import com.termoncs.moviemanager.auth.config.JwtUtils;
import com.termoncs.moviemanager.auth.model.AuthenticationRequest;
import com.termoncs.moviemanager.auth.model.AuthenticationResponse;
import com.termoncs.moviemanager.auth.service.AuthUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author aiden
 */
@RestController
public class UserController {
    
    @Autowired
    private AuthenticationManager authManager;
    
    @Autowired
    private AuthUserDetailsService userService;
    
    @Autowired
    private JwtUtils jwtTokenUtil;

    //@GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {

        String errorMsg = "An error has occured.";
        int httpErrorCode = getErrorCode(request);


        switch (httpErrorCode) {
            case 400: {
                errorMsg = "Http Error Code: 400. Bad Request";
                break;
            }
            case 401: {
                errorMsg = "Http Error Code: 401. Unauthorized";
                break;
            }
            case 404: {
                errorMsg = "Http Error Code: 404. Resource not found";
                break;
            }
            case 500: {
                errorMsg = "Http Error Code: 500. Internal Server Error";
                break;
            }
        }
        model.addAttribute("errorMsg", errorMsg);
        return "error";
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
                .getAttribute("javax.servlet.error.status_code");
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest req) { //throws Exception {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Invalid credentials");
           //throw new Exception("Invalid user credentials", e);
        }
        
        final UserDetails details = userService.loadUserByUsername(req.getUsername());
        
        final String jwt = jwtTokenUtil.generateToken(details);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
    
}
