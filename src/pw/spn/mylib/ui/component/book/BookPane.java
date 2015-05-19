package pw.spn.mylib.ui.component.book;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import org.jsoup.helper.StringUtil;

import pw.spn.mylib.MyLib;
import pw.spn.mylib.domain.Author;
import pw.spn.mylib.domain.Book;
import pw.spn.mylib.domain.BookStatus;
import pw.spn.mylib.ui.book.status.AlreadyReadBookStatusButton;
import pw.spn.mylib.ui.book.status.GoingToReadBookStatusButton;
import pw.spn.mylib.ui.book.status.NoStatusBookStatusButton;
import pw.spn.mylib.ui.book.status.ReadingBookStatusButton;
import pw.spn.mylib.util.TaskUtil;

public class BookPane extends GridPane {
    private static final String BOOK_URL = "http://flibusta.net/b/";

    public BookPane(Book book, boolean showCover) {
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
        HBox buttons = new HBox();
        buttons.setSpacing(10);
        buttons.getStyleClass().add("status-buttons");

        AlreadyReadBookStatusButton alreadyReadBookStatusButton = new AlreadyReadBookStatusButton(id);
        ReadingBookStatusButton readingBookStatusButton = new ReadingBookStatusButton(id);
        GoingToReadBookStatusButton goingToReadBookStatusButton = new GoingToReadBookStatusButton(id);
        NoStatusBookStatusButton noStatusBookStatusButton = new NoStatusBookStatusButton(id);

        switch (bookStatus) {
        case GOING_TO_READ:
            goingToReadBookStatusButton.setActive();
            break;
        case NO_STATUS:
            noStatusBookStatusButton.setActive();
            break;
        case READ:
            alreadyReadBookStatusButton.setActive();
            break;
        case READING:
            readingBookStatusButton.setActive();
            break;
        }

        buttons.getChildren().addAll(alreadyReadBookStatusButton, readingBookStatusButton, goingToReadBookStatusButton,
                noStatusBookStatusButton);

        return buttons;
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
