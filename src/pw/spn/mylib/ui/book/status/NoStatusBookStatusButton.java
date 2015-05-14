package pw.spn.mylib.ui.book.status;

import pw.spn.mylib.Messages;
import pw.spn.mylib.domain.BookStatus;

public class NoStatusBookStatusButton extends BookStatusButton {
    public NoStatusBookStatusButton(long id) {
        super(id, Messages.del());
    }

    @Override
    protected BookStatus getBookStatus() {
        return BookStatus.NO_STATUS;
    }
}
