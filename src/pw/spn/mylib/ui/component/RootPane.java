package pw.spn.mylib.ui.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;

public class RootPane extends BorderPane {
    public RootPane() {
        setId("root");
        setCenter(new ContentPane());

        BorderPane.setAlignment(getCenter(), Pos.CENTER);
        BorderPane.setMargin(getCenter(), new Insets(40));
    }
}
