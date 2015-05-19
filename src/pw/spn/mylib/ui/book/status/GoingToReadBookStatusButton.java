package pw.spn.mylib.ui.book.status;

import pw.spn.mylib.domain.BookStatus;
import pw.spn.mylib.util.BundleUtil;

public class GoingToReadBookStatusButton extends BookStatusButton {
    public GoingToReadBookStatusButton(long id) {
        super(id, BundleUtil.getMessage("going-to-read"));
    }

    @Override
    protected BookStatus getBookStatus() {
        return BookStatus.GOING_TO_READ;
    }
}
