package pw.spn.mylib.ui.menu;

import pw.spn.mylib.Messages;
import pw.spn.mylib.ui.CurrentState;
import pw.spn.mylib.util.UIUtil;

public class SearchButton extends MenuButton {
    public SearchButton() {
        super(Messages.loading(), "search-btn");
        setDisable(true);
    }

    @Override
    protected void onClickInternal() {
        UIUtil.showBooks(CurrentState.SEARCH);
    }
}
