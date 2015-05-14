package pw.spn.mylib;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.IntStream;

import pw.spn.mylib.util.StringUtil;

public final class Config {
    private static final Config INSTANCE = new Config();

    private final Properties properties;

    private final double appWidth;
    private final double appHeight;
    private final String localLibraryFileName;
    private final Set<String> acceptedLanguages;
    private final String appLanguage;
    private final int searchLimit;
    private final int minSearchQueryLength;

    private Config() {
        properties = loadPropertiesFromFile();

        appWidth = loadDoubleProperty("app.width", 1280);
        appHeight = loadDoubleProperty("app.height", 1024);
        localLibraryFileName = properties.getProperty("local.library.file", "/tmp/mylib.ser");
        acceptedLanguages = loadSetProperty("accepted-languages", ",be,en,ru,uk");
        appLanguage = properties.getProperty("app.lang", "ru");
        searchLimit = loadIntProperty("search.limit", 20);
        minSearchQueryLength = loadIntProperty("search.min-query-length", 3);
    }

    private Set<String> loadSetProperty(String propertyKey, String defaultValue) {
        String value = properties.getProperty(propertyKey);
        if (StringUtil.isEmpty(propertyKey)) {
            value = defaultValue;
        }
        String[] values = value.split(",");
        Set<String> result = new HashSet<>();
        IntStream.range(0, values.length).forEach(i -> result.add(values[i].toLowerCase()));
        return result;
    }

    private int loadIntProperty(String propertyKey, int defaultValue) {
        String value = properties.getProperty(propertyKey);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double loadDoubleProperty(String propertyKey, double defaultValue) {
        String value = properties.getProperty(propertyKey);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private Properties loadPropertiesFromFile() {
        Properties properties = new Properties();
        InputStream stream = null;
        try {
            stream = new FileInputStream("mylib.properties");
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to load mylib.properties");
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
        return properties;
    }

    public static Config getConfig() {
        return INSTANCE;
    }

    public double getAppWidth() {
        return appWidth;
    }

    public double getAppHeight() {
        return appHeight;
    }

    public String getLocalLibraryFileName() {
        return localLibraryFileName;
    }

    public Set<String> getAcceptedLanguages() {
        return acceptedLanguages;
    }

    public String getAppLanguage() {
        return appLanguage;
    }

    public int getSearchLimit() {
        return searchLimit;
    }

    public int getMinSearchQueryLength() {
        return minSearchQueryLength;
    }
}
