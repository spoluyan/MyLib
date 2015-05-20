package pw.spn.mylib.ui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.IntStream;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import pw.spn.mylib.domain.Book;
import pw.spn.mylib.service.CatalogService;
import pw.spn.mylib.service.SearchService;
import pw.spn.mylib.ui.State;
import pw.spn.mylib.ui.component.BookPane;
import pw.spn.mylib.ui.component.SearchBar;

public class BooksController implements Initializable {
    @FXML
    private GridPane books;

    private State currentState;

    private MenuController menuController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refresh(State.GOING_TO_READ);
    }

    public void refresh(State state) {
        if (menuController != null) {
            menuController.updateMenuButtonsLabels();
        }
        currentState = state;
        switch (state) {
        case GOING_TO_READ:
            showGoingToReadBooks();
            break;
        case READ:
            showReadBooks();
            break;
        case READING:
            showReadingBooks();
            break;
        case SEARCH:
            showSearch();
            break;
        }
    }

    public void refresh() {
        refresh(currentState);
    }

    private void showReadBooks() {
        Set<Book> books = CatalogService.getInstance().getReadBooks();
        showBooksData(books);
    }

    private void showReadingBooks() {
        Set<Book> books = CatalogService.getInstance().getReadingBooks();
        showBooksData(books);
    }

    private void showGoingToReadBooks() {
        Set<Book> books = CatalogService.getInstance().getGoingToReadBooks();
        showBooksData(books);
    }

    private void showBooksData(Collection<Book> books) {
        appendBooksData(books, 0);
    }

    private void appendBooksData(Collection<Book> booksData, int startRow) {
        List<Book> booksList = new ArrayList<>(booksData);
        if (startRow == 0) {
            books.getChildren().clear();
        } else {
            books.getChildren().remove(startRow, books.getChildren().size());
        }

        BookPane[] bookPanes = new BookPane[booksData.size()];
        IntStream.range(0, booksData.size()).forEach(
                i -> bookPanes[i] = new BookPane(this, booksList.get(i), startRow == 0));

        int rows = bookPanes.length / 3;
        if (rows * 3 < bookPanes.length) {
            rows++;
        }

        IntStream.range(0, rows).forEach(i -> {
            IntStream.range(0, 3).forEach(j -> {
                int index = i * 3 + j;
                if (index < bookPanes.length) {
                    books.add(bookPanes[i * 3 + j], j, startRow + i);
                }
            });
        });
    }

    private void showSearch() {
        books.getChildren().clear();
        Pair<String, List<Book>> lastSearch = SearchService.getInstance().getLastSearch();
        SearchBar searchBar = new SearchBar(this, lastSearch.getKey());
        books.add(searchBar, 0, 0, 3, 1);
        appendBooksData(lastSearch.getValue(), 1);
        searchBar.requestFocus();
    }

    public void setMenuController(MenuController menuController) {
        this.menuController = menuController;
    }
}
