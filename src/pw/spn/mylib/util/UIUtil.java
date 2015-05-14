package pw.spn.mylib.util;

import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import pw.spn.mylib.Messages;
import pw.spn.mylib.MyLib;
import pw.spn.mylib.service.CatalogService;
import pw.spn.mylib.ui.CurrentState;
import pw.spn.mylib.ui.book.BooksTable;
import pw.spn.mylib.ui.menu.MenuButton;
import pw.spn.mylib.ui.search.SearchBar;
import pw.spn.mylib.ui.search.SearchResultsPane;

public final class UIUtil {
    private UIUtil() {
    }

    public static void enableSearch() {
        SearchBar search = (SearchBar) MyLib.getScene().lookup("#search");
        search.setDisable(false);
        search.setPromptText(Messages.search());
    }

    public static void updateMenuButtonsLabels() {
        CatalogService catalogService = CatalogService.getInstance();
        long goingToReadBooks = catalogService.getGoingToReadBooks().size();
        long readingBooks = catalogService.getReadingBooks().size();
        long readBooks = catalogService.getReadBooks().size();
        
        MenuButton goingToReadButton = getMenuButtonById(MyLib.getScene(), "#going-to-read-btn");
        MenuButton readingButton = getMenuButtonById(MyLib.getScene(), "#reading-btn");
        MenuButton readButton = getMenuButtonById(MyLib.getScene(), "#read-btn");

        goingToReadButton.setText(goingToReadButton.getLabel() + " (" + goingToReadBooks + ")");
        readingButton.setText(readingButton.getLabel() + " (" + readingBooks + ")");
        readButton.setText(readButton.getLabel() + " (" + readBooks + ")");
    }

    public static void showBooks(CurrentState state) {
        getBooksTable().refresh(state);
    }

    public static void refreshBooks() {
        removeSearchResultPane();
        updateMenuButtonsLabels();
        getBooksTable().refresh();
    }

    public static void updateCover(String id, Image image) {
        ImageView view = (ImageView) MyLib.getScene().lookup("#cover-" + id);
        if (view != null) {
            view.setImage(image);
        }
    }

    public static void updateSearchResultPane(SearchResultsPane searchResultsPane) {
        removeSearchResultPane();
        BorderPane root = (BorderPane) MyLib.getScene().getRoot();
        root.getChildren().add(searchResultsPane);
    }

    public static void removeSearchResultPane() {
        BorderPane root = (BorderPane) MyLib.getScene().getRoot();
        Node currentSearchResults = root.lookup("#search-results");
        if (currentSearchResults != null) {
            root.getChildren().remove(currentSearchResults);
        }
    }

    public static void removeSearchResultPaneWithDelay() {
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                Thread.sleep(TimeUnit.MILLISECONDS.toMillis(300));
                Platform.runLater(() -> removeSearchResultPane());
                return null;
            }
        };
        TaskUtil.runTask(task);
    }

    private static BooksTable getBooksTable() {
        return (BooksTable) MyLib.getScene().lookup("#books");
    }

    private static MenuButton getMenuButtonById(Scene scene, String id) {
        return (MenuButton) scene.lookup(id);
    }
}
