package com.example.demo.util;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class Utility {
    public static boolean checkIsJwtTokenValid(final String token) {
        if (Objects.nonNull(token)) {
            return JwtUtil.isValid(token);
        }
        return false;
    }

}
