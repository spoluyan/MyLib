package pw.spn.mylib.util;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.layout.HBox;

import org.jsoup.helper.StringUtil;

import pw.spn.mylib.domain.Author;
import pw.spn.mylib.domain.BookStatus;
import pw.spn.mylib.ui.book.status.AlreadyReadBookStatusButton;
import pw.spn.mylib.ui.book.status.GoingToReadBookStatusButton;
import pw.spn.mylib.ui.book.status.NoStatusBookStatusButton;
import pw.spn.mylib.ui.book.status.ReadingBookStatusButton;

public final class BookPaneUtil {
    private BookPaneUtil() {
    }

    public static String buildAuthors(Set<Author> authors) {
        Set<String> authorsSet = new HashSet<>();
        authors.forEach(a -> {
            String author = a.toString();
            if (!StringUtil.isBlank(author)) {
                authorsSet.add(author);
            }
        });
        return StringUtil.join(authorsSet, ", ");
    }

    public static HBox generateButtons(long id, BookStatus bookStatus) {
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
}
