
// ARCHIVO: LanguageManager.java
package com.lobo24.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LanguageManager {
    private static final Map<String, Properties> languageResources = new HashMap<>();
    private static String currentLanguage = "es"; // Español por defecto

    static {
        // Cargar los idiomas disponibles
        loadLanguage("es");
        loadLanguage("en");
        loadLanguage("fr");
        loadLanguage("pt");
    }

    private static void loadLanguage(String langCode) {
        Properties props = new Properties();
        try (InputStream is = LanguageManager.class.getResourceAsStream("/languages/" + langCode + ".properties")) {
            if (is != null) {
                props.load(is);
                languageResources.put(langCode, props);
            } else {
                System.err.println("No se pudo cargar el archivo de idioma: " + langCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setCurrentLanguage(String langCode) {
        if (languageResources.containsKey(langCode)) {
            currentLanguage = langCode;
        } else {
            System.err.println("Idioma no soportado: " + langCode);
        }
    }

    public static String getString(String key) {
        Properties props = languageResources.get(currentLanguage);
        if (props != null && props.containsKey(key)) {
            return props.getProperty(key);
        }

        // Si no encuentra la clave en el idioma actual, usar español como fallback
        props = languageResources.get("es");
        if (props != null && props.containsKey(key)) {
            return props.getProperty(key);
        }

        // Si tampoco está en español, devolver la clave
        return key;
    }
}