package com.intallysh.widom.config;
import javax.security.sasl.AuthenticationException;


import com.intallysh.widom.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



public class SecurityUtil {

    private SecurityUtil() {}

    public static User getCurrentUserDetails() throws AuthenticationException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if(principal != null) {
                User user = (User) principal;
                return user;
            }

        }
        throw new AuthenticationException("Session Expired");
    }


    public static boolean isAuthenticatedUser() throws AuthenticationException {
        return getCurrentUserDetails() != null;
    }



}
