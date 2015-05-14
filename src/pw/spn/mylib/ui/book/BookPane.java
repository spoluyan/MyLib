package pw.spn.mylib.ui.book;

import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import pw.spn.mylib.MyLib;
import pw.spn.mylib.domain.Book;
import pw.spn.mylib.util.BookPaneUtil;
import pw.spn.mylib.util.TaskUtil;

public class BookPane extends GridPane {
    private static final String BOOK_URL = "http://flibusta.net/b/";

    public BookPane(Book book) {
        setId(String.valueOf(book.getFlibustaID()));
        getStyleClass().add("book");

        ImageView image = buildImage(book.getImage());
        add(image, 0, 0);

        Hyperlink hyperLink = buildLink(book.getFlibustaID(), book.getTitle());
        add(hyperLink, 0, 1);

        String authors = BookPaneUtil.buildAuthors(book.getAuthors());
        Text authorsText = new Text(authors);
        authorsText.setWrappingWidth(380);
        add(authorsText, 0, 2);

        HBox buttons = BookPaneUtil.generateButtons(book.getFlibustaID(), book.getStatus());
        add(buttons, 0, 3);
    }

    private ImageView buildImage(String cover) {
        Image image = new Image(getClass().getResourceAsStream("default-cover.png"));
        if (cover != null) {
            LoadCoverTask task = new LoadCoverTask(getId(), cover);
            TaskUtil.runTask(task);
        }
        ImageView view = new ImageView(image);
        view.setId("cover-" + getId());
        view.getStyleClass().add("cover");
        view.setFitHeight(128);
        view.setFitWidth(85);
        return view;
    }

    private Hyperlink buildLink(long flibustaID, String title) {
        Hyperlink link = new Hyperlink(title);
        link.getStyleClass().add("link");
        link.setVisited(false);
        link.setOnAction(e -> MyLib.openBrowser(BOOK_URL + flibustaID));
        return link;
    }
}
