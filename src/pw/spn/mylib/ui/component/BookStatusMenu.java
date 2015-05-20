package pw.spn.mylib.ui.component;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import pw.spn.mylib.util.BundleUtil;

public class BookStatusMenu extends HBox {
    public BookStatusMenu() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../BookStatusMenu.fxml"), BundleUtil.getBundle());
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
