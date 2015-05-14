package pw.spn.mylib;

import java.io.IOException;
import java.util.Properties;

public final class Messages {
    private static final String LANG_EN = "en";
    private static final String LANG_RU = "ru";
    private static final String LANG_DEFAULT = LANG_RU;

    private static Messages messages = new Messages();

    private String read;
    private String goingToTead;
    private String reading;
    private String del;
    private String loading;
    private String search;

    private Messages() {
        loadMessages();
    }

    private void loadMessages() {
        Properties properties = new Properties();
        String lang = Config.getConfig().getAppLanguage().toLowerCase();
        String fileName = "messages_";
        switch (lang) {
        case LANG_EN:
            fileName += LANG_EN;
            break;
        case LANG_RU:
            fileName += LANG_RU;
            break;
        default:
            fileName += LANG_DEFAULT;
            break;
        }
        fileName += ".properties";
        try {
            properties.load(getClass().getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to load messages file.");
        }
        read = properties.getProperty("read");
        goingToTead = properties.getProperty("going-to-read");
        reading = properties.getProperty("reading");
        del = properties.getProperty("del");
        loading = properties.getProperty("loading");
        search = properties.getProperty("search");
    }

    public static String read() {
        return messages.read;
    }

    public static String goingToTead() {
        return messages.goingToTead;
    }

    public static String reading() {
        return messages.reading;
    }

    public static String del() {
        return messages.del;
    }

    public static String loading() {
        return messages.loading;
    }

    public static String search() {
        return messages.search;
    }
}
