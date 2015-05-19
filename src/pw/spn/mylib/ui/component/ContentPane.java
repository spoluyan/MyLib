package pw.spn.mylib.ui.component;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import pw.spn.mylib.ui.component.book.BooksTable;
import pw.spn.mylib.ui.component.menu.AlreadyReadButton;
import pw.spn.mylib.ui.component.menu.GoingToReadButton;
import pw.spn.mylib.ui.component.menu.ReadingButton;
import pw.spn.mylib.ui.component.menu.SearchButton;

public class ContentPane extends GridPane {
    public ContentPane() {
        setId("content");

        add(new GoingToReadButton(), 0, 0);
        add(new ReadingButton(), 1, 0);
        add(new AlreadyReadButton(), 2, 0);
        add(new SearchButton(), 3, 0);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setId("scroll");
        scrollPane.setContent(new BooksTable());
        add(scrollPane, 0, 1, 4, 1);
    }
}
