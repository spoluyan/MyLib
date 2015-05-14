package pw.spn.mylib.ui;

import pw.spn.mylib.ui.search.SearchBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;

public class RootPane extends BorderPane {
    public RootPane() {
        setId("root");
        setTop(new SearchBar());
        setCenter(new ContentPane());

        BorderPane.setAlignment(getTop(), Pos.CENTER);
        BorderPane.setMargin(getTop(), new Insets(40));
        BorderPane.setAlignment(getCenter(), Pos.CENTER);
    }
}
