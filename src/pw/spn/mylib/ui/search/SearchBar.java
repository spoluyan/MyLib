package pw.spn.mylib.ui.search;

import javafx.scene.control.TextField;
import pw.spn.mylib.Messages;
import pw.spn.mylib.service.SearchService;
import pw.spn.mylib.ui.CurrentState;
import pw.spn.mylib.util.UIUtil;

public class SearchBar extends TextField {
    private static final int MIN_SEARCH_TEXT_LENGTH = 3;

    public SearchBar(String text) {
        setId("search");
        setPromptText(Messages.search());
        setText(text);
        addEventsHandlers();

        positionCaret(text.length());
    }

    private void addEventsHandlers() {
        textProperty().addListener((observable, oldValue, newValue) -> doSearch());
    }

    private void doSearch() {
        String q = getText();
        if (q.length() >= MIN_SEARCH_TEXT_LENGTH) {
            SearchService.getInstance().searchBooks(q);
            drawResult();
        }
    }

    private void drawResult() {
        UIUtil.showBooks(CurrentState.SEARCH);
    }
}
