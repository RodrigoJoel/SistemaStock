
// ARCHIVO: ConfigService.java
package com.lobo24.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigService {
    private static final String CONFIG_FILE = "config.properties";
    private static final String USER_PREFERENCES_FILE = "user_preferences.properties";
    private static Properties configProperties = new Properties();
    private static Properties userPreferences = new Properties();

    static {
        loadConfiguration();
        loadUserPreferences();
    }

    private static void loadConfiguration() {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            configProperties.load(fis);
        } catch (IOException e) {
            // Si no existe el archivo, crear uno con valores por defecto
            configProperties.setProperty("server.url", "localhost");
            configProperties.setProperty("server.port", "3306");
            configProperties.setProperty("database.name", "lobo");
            saveConfiguration();
        }
    }

    private static void saveConfiguration() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            configProperties.store(fos, "System Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadUserPreferences() {
        try (FileInputStream fis = new FileInputStream(USER_PREFERENCES_FILE)) {
            userPreferences.load(fis);
        } catch (IOException e) {
            // Si no existe el archivo, crear uno con valores por defecto
            userPreferences.setProperty("idioma", "es");
            userPreferences.setProperty("servidor", "Local");
            saveUserPreferences();
        }
    }

    private static void saveUserPreferences() {
        try (FileOutputStream fos = new FileOutputStream(USER_PREFERENCES_FILE)) {
            userPreferences.store(fos, "User Preferences");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getServerUrl() {
        return configProperties.getProperty("server.url", "localhost");
    }

    public static void setServerUrl(String serverUrl) {
        configProperties.setProperty("server.url", serverUrl);
        saveConfiguration();
    }

    public static String getUserPreference(String key, String defaultValue) {
        return userPreferences.getProperty(key, defaultValue);
    }

    public static void saveUserPreference(String key, String value) {
        userPreferences.setProperty(key, value);
        saveUserPreferences();
    }
}