package pw.spn.mylib.ui.component;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import org.jsoup.helper.StringUtil;

import pw.spn.mylib.MyLib;
import pw.spn.mylib.domain.Author;
import pw.spn.mylib.domain.Book;
import pw.spn.mylib.domain.BookStatus;
import pw.spn.mylib.service.CatalogService;
import pw.spn.mylib.task.LoadCoverTask;
import pw.spn.mylib.ui.controller.BooksController;
import pw.spn.mylib.util.TaskUtil;

public class BookPane extends GridPane {
    private static final String BOOK_URL = "http://flibusta.net/b/";

    private BooksController booksController;

    public BookPane(BooksController booksController, Book book, boolean showCover) {
        this.booksController = booksController;
        setId(String.valueOf(book.getFlibustaID()));
        getStyleClass().add("book");

        int rowCounter = 0;

        if (showCover) {
            ImageView image = buildImage(book.getImage());
            add(image, 0, rowCounter++);
        }

        Hyperlink hyperLink = buildLink(book.getFlibustaID(), book.getTitle());
        add(hyperLink, 0, rowCounter++);

        String authors = buildAuthors(book.getAuthors());
        Text authorsText = new Text(authors);
        authorsText.setWrappingWidth(380);
        add(authorsText, 0, rowCounter++);

        HBox buttons = generateButtons(book.getFlibustaID(), book.getStatus());
        add(buttons, 0, rowCounter++);
    }

    private String buildAuthors(Set<Author> authors) {
        Set<String> authorsSet = new HashSet<>();
        authors.forEach(a -> {
            String author = a.toString();
            if (!StringUtil.isBlank(author)) {
                authorsSet.add(author);
            }
        });
        return StringUtil.join(authorsSet, ", ");
    }

    private HBox generateButtons(long id, BookStatus bookStatus) {
        BookStatusMenu buttons = new BookStatusMenu();
        buttons.getChildren().forEach(n -> {
            BookStatusButton bookStatusButton = (BookStatusButton) n;
            if (bookStatusButton.getStatus() == bookStatus) {
                bookStatusButton.getStyleClass().add("active");
                bookStatusButton.setDisable(true);
            }
            n.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                CatalogService.getInstance().updateBookStatus(id, bookStatusButton.getStatus());
                booksController.refresh();
            });
        });

        return buttons;
    }

    private ImageView buildImage(String cover) {
        Image image = new Image(getClass().getResourceAsStream("default-cover.png"));
        ImageView view = new ImageView(image);
        view.setId("cover-" + getId());
        view.getStyleClass().add("cover");
        view.setFitHeight(128);
        view.setFitWidth(85);
        if (cover != null) {
            LoadCoverTask loadCoverTask = new LoadCoverTask(getId(), cover);
            loadCoverTask.setOnSucceeded(event -> {
                try {
                    Image loadedCover = loadCoverTask.get();
                    view.setImage(loadedCover);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });
            TaskUtil.runTask(loadCoverTask);
        }
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
