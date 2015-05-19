package pw.spn.mylib.ui.component.menu;

import pw.spn.mylib.ui.component.CurrentState;
import pw.spn.mylib.util.BundleUtil;
import pw.spn.mylib.util.UIUtil;

public class SearchButton extends MenuButton {
    public SearchButton() {
        super(BundleUtil.getMessage("loading"), "search-btn");
        setDisable(true);
    }

    @Override
    protected void onClickInternal() {
        UIUtil.showBooks(CurrentState.SEARCH);
    }
}
