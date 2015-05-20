package pw.spn.mylib.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import pw.spn.mylib.service.CatalogService;
import pw.spn.mylib.task.LoadRemoteCatalogTask;
import pw.spn.mylib.ui.component.MenuButton;
import pw.spn.mylib.util.TaskUtil;

public class MenuController implements Initializable {
    private static final String ACTIVE_CLASS = "active";

    @FXML
    private MenuButton goingToReadMenuButton;

    @FXML
    private MenuButton readingMenuButton;

    @FXML
    private MenuButton readMenuButton;

    @FXML
    private MenuButton searchMenuButton;

    private BooksController booksController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadRemoteCatalog(resources);
        addEventHandlers();
        updateMenuButtonsLabels();
    }

    public void setBooksController(BooksController booksController) {
        this.booksController = booksController;
    }

    private void loadRemoteCatalog(ResourceBundle resources) {
        LoadRemoteCatalogTask loadRemoteCatalogTask = new LoadRemoteCatalogTask();
        loadRemoteCatalogTask.setOnSucceeded(event -> {
            searchMenuButton.setDisable(false);
            searchMenuButton.setText(resources.getString("search"));
        });
        TaskUtil.runTask(loadRemoteCatalogTask);
    }

    private void addEventHandlers() {
        goingToReadMenuButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> onMenuClick(goingToReadMenuButton));
        readingMenuButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> onMenuClick(readingMenuButton));
        readMenuButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> onMenuClick(readMenuButton));
        searchMenuButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> onMenuClick(searchMenuButton));
    }

    public void updateMenuButtonsLabels() {
        CatalogService catalogService = CatalogService.getInstance();
        long goingToReadBooks = catalogService.getGoingToReadBooks().size();
        long readingBooks = catalogService.getReadingBooks().size();
        long readBooks = catalogService.getReadBooks().size();

        goingToReadMenuButton.setText(goingToReadMenuButton.getLabel() + " (" + goingToReadBooks + ")");
        readingMenuButton.setText(readingMenuButton.getLabel() + " (" + readingBooks + ")");
        readMenuButton.setText(readMenuButton.getLabel() + " (" + readBooks + ")");
    }

    private void onMenuClick(MenuButton menuButton) {
        if (menuButton.getStyleClass().contains(ACTIVE_CLASS)) {
            return;
        }
        menuButton.getScene().lookup("." + ACTIVE_CLASS).getStyleClass().remove(ACTIVE_CLASS);
        menuButton.getStyleClass().add(ACTIVE_CLASS);
        booksController.refresh(menuButton.getState());
    }

}
