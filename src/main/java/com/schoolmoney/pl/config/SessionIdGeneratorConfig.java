package com.schoolmoney.pl.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.session.SessionIdGenerator;

import java.util.concurrent.ThreadLocalRandom;

public class SessionIdGeneratorConfig implements SessionIdGenerator {
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @NotNull
    @Override
    public String generate() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(36);

        for (int i = 0; i < 36; i++) {
            sb.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
        }

        return sb.toString();
    }
}