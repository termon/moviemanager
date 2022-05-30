
package com.termoncs.moviemanager.auth.config;

import com.termoncs.moviemanager.auth.controller.UserController;
import com.termoncs.moviemanager.auth.service.AuthUserDetailsService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author aiden
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private AuthUserDetailsService service;
    
    @Autowired
    private JwtUtils jwtUtil;
    
    org.slf4j.Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
    
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse rsp, FilterChain fc) throws ServletException, IOException {

        logger.info("======== In Request Filter =========");
        final String authHeader = req.getHeader("Authorization");
        String username = null, jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication()==null) {
            UserDetails userDetails = this.service.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, userDetails)) {
                var authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("================ setting token");
            }
        }
        // pass on to the next filter
        fc.doFilter(req, rsp);
    
    }
    
}
