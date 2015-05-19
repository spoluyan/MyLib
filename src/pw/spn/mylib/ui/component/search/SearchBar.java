package pw.spn.mylib.ui.component.search;

import javafx.scene.control.TextField;
import pw.spn.mylib.Config;
import pw.spn.mylib.service.SearchService;
import pw.spn.mylib.ui.component.CurrentState;
import pw.spn.mylib.util.BundleUtil;
import pw.spn.mylib.util.UIUtil;

public class SearchBar extends TextField {
    public SearchBar(String text) {
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
        UIUtil.showBooks(CurrentState.SEARCH);
    }
}
