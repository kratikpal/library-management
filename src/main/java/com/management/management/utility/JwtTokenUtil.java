package com.management.management.utility;

import com.management.management.Constants.HttpConstants;

public class JwtTokenUtil {
    public static String removeBearer(String token) {
        return token.substring(HttpConstants.BEARER.length());
    }
}
