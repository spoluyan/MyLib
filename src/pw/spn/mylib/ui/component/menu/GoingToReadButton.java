package pw.spn.mylib.ui.component.menu;

import pw.spn.mylib.ui.component.CurrentState;
import pw.spn.mylib.util.BundleUtil;
import pw.spn.mylib.util.UIUtil;

public class GoingToReadButton extends MenuButton {
    public GoingToReadButton() {
        super(BundleUtil.getMessage("going-to-read"), "going-to-read-btn");

        getStyleClass().add(ACTIVE_CLASS);
    }

    @Override
    protected void onClickInternal() {
        UIUtil.showBooks(CurrentState.GOING_TO_READ);
    }
}
