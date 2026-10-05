package com.qa.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public final class TestConfig {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = load();

    private TestConfig() {
    }

    public static Credentials sauceDemoUser() {
        return new Credentials(get("saucedemo.username"), get("saucedemo.password"));
    }

    public static String bookerBaseUri() {
        return get("booker.baseUri");
    }

    public static Credentials bookerAdmin() {
        return new Credentials(get("booker.username"), get("booker.password"));
    }

    private static String get(String key) {
        String value = System.getProperty(key, PROPERTIES.getProperty(key));
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required configuration property: " + key);
        }
        return value;
    }

    private static Properties load() {
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException(CONFIG_FILE + " was not found on the classpath");
            }
            Properties properties = new Properties();
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read " + CONFIG_FILE, e);
        }
    }
}