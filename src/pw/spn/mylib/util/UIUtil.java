package pw.spn.mylib.util;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pw.spn.mylib.Messages;
import pw.spn.mylib.MyLib;
import pw.spn.mylib.service.CatalogService;
import pw.spn.mylib.ui.CurrentState;
import pw.spn.mylib.ui.book.BooksTable;
import pw.spn.mylib.ui.menu.MenuButton;
import pw.spn.mylib.ui.menu.SearchButton;

public final class UIUtil {
    private UIUtil() {
    }

    public static void enableSearch() {
        SearchButton search = (SearchButton) MyLib.getScene().lookup("#search-btn");
        search.setDisable(false);
        search.setText(Messages.search());
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
        updateMenuButtonsLabels();
        getBooksTable().refresh();
    }

    public static void updateCover(String id, Image image) {
        ImageView view = (ImageView) MyLib.getScene().lookup("#cover-" + id);
        if (view != null) {
            view.setImage(image);
        }
    }

    private static BooksTable getBooksTable() {
        return (BooksTable) MyLib.getScene().lookup("#books");
    }

    private static MenuButton getMenuButtonById(Scene scene, String id) {
        return (MenuButton) scene.lookup(id);
    }
}
