package pw.spn.mylib.ui.component.menu;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public abstract class MenuButton extends Button {
    protected static final String ACTIVE_CLASS = "active";

    private String label;

    public MenuButton(String label, String id) {
        super(label);
        this.label = label;
        setId(id);
        getStyleClass().add("menu-button");

        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> onClick(e));
    }

    private void onClick(MouseEvent e) {
        if (getStyleClass().contains(ACTIVE_CLASS)) {
            return;
        }
        getScene().lookup("." + ACTIVE_CLASS).getStyleClass().remove(ACTIVE_CLASS);
        getStyleClass().add(ACTIVE_CLASS);
        onClickInternal();
    }

    public String getLabel() {
        return label;
    }

    protected abstract void onClickInternal();
}
