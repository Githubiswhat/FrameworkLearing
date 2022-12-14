package com.example.util;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomStringGenerator {

    public static String getRandomAlphabetic(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static String getRandomNumeric(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    public static String getRandomAlphanumeric(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

}
