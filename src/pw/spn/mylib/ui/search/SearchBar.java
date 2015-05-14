package pw.spn.mylib.ui.search;

import java.util.List;
import java.util.stream.IntStream;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import pw.spn.mylib.Messages;
import pw.spn.mylib.domain.Book;
import pw.spn.mylib.service.SearchService;
import pw.spn.mylib.util.UIUtil;

public class SearchBar extends TextField {
    private static final int MIN_SEARCH_TEXT_LENGTH = 3;

    public SearchBar() {
        setId("search");
        setPromptText(Messages.loading());
        setFocused(false);
        setDisable(true);
        addEventsHandlers();
    }

    private void addEventsHandlers() {
        textProperty().addListener((observable, oldValue, newValue) -> doSearch());

        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                doSearch();
            } else {
                UIUtil.removeSearchResultPaneWithDelay();
            }
        });

        addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (KeyCode.ESCAPE == e.getCode()) {
                UIUtil.removeSearchResultPane();
            }
        });
    }

    private void doSearch() {
        String q = getText();
        if (q.length() >= MIN_SEARCH_TEXT_LENGTH) {
            List<Book> searchResult = SearchService.getInstance().searchBooks(q);
            if (searchResult.size() > 0) {
                drawResult(searchResult);
            } else {
                UIUtil.removeSearchResultPane();
            }
        }
    }

    private void drawResult(List<Book> searchResult) {
        SearchResultsPane searchResultsPane = new SearchResultsPane(getLayoutX(), getLayoutY() + getHeight());
        IntStream.range(0, searchResult.size()).forEach(i -> {
            Book book = searchResult.get(i);
            SearchBookPane searchBookPane = new SearchBookPane(book);
            searchResultsPane.add(searchBookPane, 0, i);
        });
        UIUtil.updateSearchResultPane(searchResultsPane);
    }
}
