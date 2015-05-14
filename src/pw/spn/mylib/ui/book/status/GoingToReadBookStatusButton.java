package pw.spn.mylib.ui.book.status;

import pw.spn.mylib.Messages;
import pw.spn.mylib.domain.BookStatus;

public class GoingToReadBookStatusButton extends BookStatusButton {
    public GoingToReadBookStatusButton(long id) {
        super(id, Messages.goingToTead());
    }

    @Override
    protected BookStatus getBookStatus() {
        return BookStatus.GOING_TO_READ;
    }
}
