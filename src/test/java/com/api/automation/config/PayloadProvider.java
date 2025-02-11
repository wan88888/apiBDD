package com.api.automation.config;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PayloadProvider {

    public static String loadPayload(String fileName) {
        // 统一处理schema路径
        if (!fileName.contains("/")) {
            fileName = "schema/" + fileName;
        }
        return readFileAsString(fileName);
    }

    private static String readFileAsString(String fileName) {
        try {
            InputStream inputStream = PayloadProvider.class.getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new RuntimeException("Could not find file: " + fileName);
            }
            try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
                return scanner.useDelimiter("\\A").next();
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not find file: " + fileName, e);
        }
    }
}
