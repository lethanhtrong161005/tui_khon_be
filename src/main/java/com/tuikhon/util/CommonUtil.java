package com.tuikhon.util;

import java.security.SecureRandom;

/**
 * Utility class providing common helper methods across the application.
 */
public final class CommonUtil {

    private CommonUtil() {
        // Utility class
    }

    /**
     * Generates a random 6-digit numeric verification code.
     *
     * @return 6-digit string representation of code.
     */
    public static String generateOtpCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
