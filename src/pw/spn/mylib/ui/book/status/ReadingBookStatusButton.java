package pw.spn.mylib.ui.book.status;

import pw.spn.mylib.domain.BookStatus;
import pw.spn.mylib.util.BundleUtil;

public class ReadingBookStatusButton extends BookStatusButton {
    public ReadingBookStatusButton(long id) {
        super(id, BundleUtil.getMessage("reading"));
    }

    @Override
    protected BookStatus getBookStatus() {
        return BookStatus.READING;
    }
}
