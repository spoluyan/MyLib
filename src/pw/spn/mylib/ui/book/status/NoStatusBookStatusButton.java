package pw.spn.mylib.ui.book.status;

import pw.spn.mylib.domain.BookStatus;
import pw.spn.mylib.util.BundleUtil;

public class NoStatusBookStatusButton extends BookStatusButton {
    public NoStatusBookStatusButton(long id) {
        super(id, BundleUtil.getMessage("del"));
    }

    @Override
    protected BookStatus getBookStatus() {
        return BookStatus.NO_STATUS;
    }
}
