package pw.spn.mylib.ui.menu;

import pw.spn.mylib.ui.CurrentState;
import pw.spn.mylib.util.BundleUtil;
import pw.spn.mylib.util.UIUtil;

public class ReadingButton extends MenuButton {
    public ReadingButton() {
        super(BundleUtil.getMessage("reading"), "reading-btn");
    }

    @Override
    protected void onClickInternal() {
        UIUtil.showBooks(CurrentState.READING);
    }
}
