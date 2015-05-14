package pw.spn.mylib.ui.book.status;

import pw.spn.mylib.Messages;
import pw.spn.mylib.domain.BookStatus;

public class ReadingBookStatusButton extends BookStatusButton {
    public ReadingBookStatusButton(long id) {
        super(id, Messages.reading());
    }

    @Override
    protected BookStatus getBookStatus() {
        return BookStatus.READING;
    }
}
