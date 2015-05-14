package pw.spn.mylib;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pw.spn.mylib.service.CatalogService;
import pw.spn.mylib.ui.CurrentState;
import pw.spn.mylib.ui.RootPane;
import pw.spn.mylib.util.TaskUtil;
import pw.spn.mylib.util.UIUtil;

public class MyLib extends Application {
    private static MyLib application;

    private static Scene scene;

    public MyLib() {
        application = this;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setMinWidth(1170);
        primaryStage.setTitle("MyLib v" + AppVersion.getVersion());
        Scene scene = new Scene(new RootPane(), Config.getConfig().getAppWidth(), Config.getConfig().getAppHeight());
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        MyLib.scene = scene;
        primaryStage.setScene(scene);
        primaryStage.show();

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                CatalogService.getInstance().getRemoteLibrary();
                Platform.runLater(() -> UIUtil.enableSearch());
                return null;
            }

        };
        TaskUtil.runTask(task);
        UIUtil.updateMenuButtonsLabels();
        UIUtil.showBooks(CurrentState.GOING_TO_READ);
    }

    public static void openBrowser(String uri) {
        application.getHostServices().showDocument(uri);
    }

    public static Scene getScene() {
        return scene;
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("http.agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:37.0) Gecko/20100101 Firefox/37.0");
        launch(args);
    }
}
