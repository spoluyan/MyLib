package pw.spn.mylib.util;

public final class StringUtil {
    private StringUtil() {
    }

    public static final String EMPTY = "";
    public static final String SPACE = " ";

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0 || string.trim().equals(EMPTY);
    }
}
