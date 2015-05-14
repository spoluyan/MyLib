package pw.spn.mylib.ui.book.status;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import pw.spn.mylib.domain.BookStatus;
import pw.spn.mylib.service.CatalogService;
import pw.spn.mylib.util.UIUtil;

public abstract class BookStatusButton extends Button {
    private long id;

    public BookStatusButton(long id, String label) {
        super(label);
        this.id = id;
        getStyleClass().add("status-button");
        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> onClick(e));
    }

    public void setActive() {
        getStyleClass().add("active");
        setDisable(true);
    }

    private void onClick(MouseEvent e) {
        UIUtil.removeSearchResultPane();
        CatalogService.getInstance().updateBookStatus(id, getBookStatus());
        UIUtil.refreshBooks();
    }

    protected abstract BookStatus getBookStatus();
}
