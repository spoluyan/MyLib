package pw.spn.mylib.ui.component;

import javafx.scene.control.Button;
import pw.spn.mylib.domain.BookStatus;

public class BookStatusButton extends Button {
    private BookStatus status;

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}
