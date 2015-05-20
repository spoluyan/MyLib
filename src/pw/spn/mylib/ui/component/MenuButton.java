package pw.spn.mylib.ui.component;

import javafx.scene.control.Button;
import pw.spn.mylib.ui.State;

public class MenuButton extends Button {
    protected static final String ACTIVE_CLASS = "active";

    private String label;
    private State state;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
