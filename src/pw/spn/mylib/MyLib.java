package pw.spn.mylib;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import pw.spn.mylib.task.LoadRemoteCatalogTask;
import pw.spn.mylib.ui.component.CurrentState;
import pw.spn.mylib.ui.component.RootPane;
import pw.spn.mylib.util.BundleUtil;
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
        setUpPrimaryState(primaryStage);
        MyLib.scene = buildUI();
        primaryStage.setScene(scene);
        primaryStage.show();

        TaskUtil.runTask(new LoadRemoteCatalogTask());
        UIUtil.updateMenuButtonsLabels();
        UIUtil.showBooks(CurrentState.GOING_TO_READ);
    }

    private Scene buildUI() {
        Scene scene = new Scene(new RootPane(), Config.getConfig().getAppWidth(), Config.getConfig().getAppHeight());
        scene.getStylesheets().add(getClass().getResource("ui/application.css").toExternalForm());
        return scene;
    }

    private void setUpPrimaryState(Stage primaryStage) {
        primaryStage.setMinWidth(1170);
        primaryStage.setTitle("MyLib v" + AppVersion.getVersion());
        primaryStage.setMaximized(true);
        primaryStage.setFullScreenExitHint(BundleUtil.getMessage("full-screen-exit-hint"));
        Image applicationIcon = new Image(getClass().getResourceAsStream("icon.png"));
        primaryStage.getIcons().add(applicationIcon);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.F11) {
                if (primaryStage.isFullScreen()) {
                    primaryStage.setFullScreen(false);
                } else {
                    primaryStage.setFullScreen(true);
                }
            }
        });
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
