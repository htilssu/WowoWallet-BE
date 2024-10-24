package com.wowo.wowo.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    public static Object getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public static String getId() {
        return ((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
