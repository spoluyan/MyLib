package pw.spn.mylib.ui.book.status;

import pw.spn.mylib.Messages;
import pw.spn.mylib.domain.BookStatus;

public class AlreadyReadBookStatusButton extends BookStatusButton {

    public AlreadyReadBookStatusButton(long id) {
        super(id, Messages.read());
    }

    @Override
    protected BookStatus getBookStatus() {
        return BookStatus.READ;
    }

}
