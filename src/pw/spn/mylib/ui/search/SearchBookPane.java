package pw.spn.mylib.ui.search;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import pw.spn.mylib.domain.Book;
import pw.spn.mylib.util.BookPaneUtil;

public class SearchBookPane extends GridPane {
    public SearchBookPane(Book book) {
        setId(String.valueOf(book.getFlibustaID()));
        getStyleClass().add("book-search");

        add(new Text(book.getTitle() + " (" + book.getFlibustaID() + ")"), 0, 0);

        String authors = BookPaneUtil.buildAuthors(book.getAuthors());
        add(new Text("(" + authors + ")"), 0, 1);

        HBox buttons = BookPaneUtil.generateButtons(book.getFlibustaID(), book.getStatus());
        add(buttons, 0, 3);
    }
}
