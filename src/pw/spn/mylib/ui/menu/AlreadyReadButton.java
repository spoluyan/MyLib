package pw.spn.mylib.ui.menu;

import pw.spn.mylib.ui.CurrentState;
import pw.spn.mylib.util.BundleUtil;
import pw.spn.mylib.util.UIUtil;

public class AlreadyReadButton extends MenuButton {
    public AlreadyReadButton() {
        super(BundleUtil.getMessage("read"), "read-btn");
    }

    @Override
    protected void onClickInternal() {
        UIUtil.showBooks(CurrentState.READ);
    }
}
