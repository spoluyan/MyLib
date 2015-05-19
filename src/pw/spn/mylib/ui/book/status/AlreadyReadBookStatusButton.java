package pw.spn.mylib.ui.book.status;

import pw.spn.mylib.domain.BookStatus;
import pw.spn.mylib.util.BundleUtil;

public class AlreadyReadBookStatusButton extends BookStatusButton {

    public AlreadyReadBookStatusButton(long id) {
        super(id, BundleUtil.getMessage("read"));
    }

    @Override
    protected BookStatus getBookStatus() {
        return BookStatus.READ;
    }

}
