package pw.spn.mylib.ui.component;

import javafx.scene.control.TextField;
import pw.spn.mylib.Config;
import pw.spn.mylib.service.SearchService;
import pw.spn.mylib.ui.State;
import pw.spn.mylib.ui.controller.BooksController;
import pw.spn.mylib.util.BundleUtil;

public class SearchBar extends TextField {
    private BooksController booksController;

    public SearchBar(BooksController booksController, String text) {
        this.booksController = booksController;
        setId("search");
        setPromptText(BundleUtil.getMessage("search"));
        setText(text);
        addEventsHandlers();

        positionCaret(text.length());
    }

    private void addEventsHandlers() {
        textProperty().addListener((observable, oldValue, newValue) -> doSearch());
    }

    private void doSearch() {
        String q = getText();
        if (q.length() >= Config.getConfig().getMinSearchQueryLength()) {
            SearchService.getInstance().searchBooks(q);
            drawResult();
        }
    }

    private void drawResult() {
        booksController.refresh(State.SEARCH);
    }
}
