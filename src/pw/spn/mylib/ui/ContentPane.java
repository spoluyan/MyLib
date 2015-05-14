package pw.spn.mylib.ui;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import pw.spn.mylib.ui.book.BooksTable;
import pw.spn.mylib.ui.menu.AlreadyReadButton;
import pw.spn.mylib.ui.menu.GoingToReadButton;
import pw.spn.mylib.ui.menu.ReadingButton;

public class ContentPane extends GridPane {
    public ContentPane() {
        setId("content");

        add(new GoingToReadButton(), 0, 0);
        add(new ReadingButton(), 1, 0);
        add(new AlreadyReadButton(), 2, 0);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setId("scroll");
        scrollPane.setContent(new BooksTable());
        add(scrollPane, 0, 1, 3, 1);
    }
}
