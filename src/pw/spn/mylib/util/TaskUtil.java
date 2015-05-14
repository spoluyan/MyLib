package pw.spn.mylib.util;

import javafx.concurrent.Task;

public final class TaskUtil {
    private TaskUtil() {
    }

    public static void runTask(Task<?> task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
