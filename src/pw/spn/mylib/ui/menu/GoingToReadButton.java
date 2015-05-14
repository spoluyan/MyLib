package pw.spn.mylib.ui.menu;

import pw.spn.mylib.Messages;
import pw.spn.mylib.ui.CurrentState;
import pw.spn.mylib.util.UIUtil;

public class GoingToReadButton extends MenuButton {
    public GoingToReadButton() {
        super(Messages.goingToTead(), "going-to-read-btn");

        getStyleClass().add(ACTIVE_CLASS);
    }

    @Override
    protected void onClickInternal() {
        UIUtil.showBooks(CurrentState.GOING_TO_READ);
    }
}
