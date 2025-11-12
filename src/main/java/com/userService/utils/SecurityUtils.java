package com.userService.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static String getRole (){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .findFirst()
                .orElse("ROLE_ANONYMOUS");
    }

    public static String getId () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public static String getCurrentRole () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            for(GrantedAuthority authority : authentication.getAuthorities()){
                return authority.getAuthority();
            }
        }
        throw new IllegalStateException("No authenticated user found");
    }
}
