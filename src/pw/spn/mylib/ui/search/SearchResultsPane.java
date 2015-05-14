package pw.spn.mylib.ui.search;

import javafx.scene.layout.GridPane;

public class SearchResultsPane extends GridPane {
    public SearchResultsPane(double layoutX, double layoutY) {
        setId("search-results");

        setLayoutX(layoutX);
        setLayoutY(layoutY);
    }
}
