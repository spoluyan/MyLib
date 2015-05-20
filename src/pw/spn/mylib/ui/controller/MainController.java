package pw.spn.mylib.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class MainController implements Initializable {
    @FXML
    private MenuController menuController;

    @FXML
    private BooksController booksPaneController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuController.setBooksController(booksPaneController);
        booksPaneController.setMenuController(menuController);
    }

}
