package pw.spn.mylib;

public final class AppVersion {
    private AppVersion() {
    }

    private static final String VERSION = "3.0.1";

    public static String getVersion() {
        return VERSION;
    }
}
