package pw.spn.mylib;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pw.spn.mylib.util.BundleUtil;

public class MyLib extends Application {
    private static MyLib application;

    public MyLib() {
        application = this;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        setUpPrimaryState(primaryStage);
        primaryStage.setScene(buildUI());
        primaryStage.show();
    }

    private Scene buildUI() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/MyLib.fxml"), BundleUtil.getBundle());
        BorderPane root = loader.<BorderPane> load();
        Scene scene = new Scene(root, Config.getConfig().getAppWidth(), Config.getConfig().getAppHeight());
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

    public static void main(String[] args) throws IOException {
        System.setProperty("http.agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:37.0) Gecko/20100101 Firefox/37.0");
        launch(args);
    }
}
