package pw.spn.mylib.util;

import java.util.Locale;
import java.util.ResourceBundle;

import pw.spn.mylib.Config;

public final class BundleUtil {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("pw.spn.mylib.bundle.messages", new Locale(
            Config.getConfig().getAppLanguage()));

    private BundleUtil() {
    }

    public static ResourceBundle getBundle() {
        return BUNDLE;
    }

    public static String getMessage(String key) {
        return BUNDLE.getString(key);
    }
}
