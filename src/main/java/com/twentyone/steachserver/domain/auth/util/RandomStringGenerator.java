package com.twentyone.steachserver.domain.auth.util;

import java.security.SecureRandom;

public class RandomStringGenerator {
    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int STRING_LENGTH = 30;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String getNew() {
        StringBuilder stringBuilder = new StringBuilder(STRING_LENGTH);
        for (int i = 0; i < STRING_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(ALLOWED_CHARACTERS.length());
            stringBuilder.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }
}
