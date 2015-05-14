package pw.spn.mylib.ui.book;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import pw.spn.mylib.domain.Book;
import pw.spn.mylib.service.CatalogService;
import pw.spn.mylib.service.SearchService;
import pw.spn.mylib.ui.CurrentState;
import pw.spn.mylib.ui.search.SearchBar;

public class BooksTable extends GridPane {
    private CurrentState currentState;

    public BooksTable() {
        setId("books");
        setAlignment(Pos.CENTER);
    }

    public void refresh(CurrentState state) {
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

    private void appendBooksData(Collection<Book> books, int startRow) {
        List<Book> booksList = new ArrayList<>(books);
        getChildren().remove(startRow, getChildren().size());
        if (startRow == 0) {
            getChildren().clear();
        }

        BookPane[] bookPanes = new BookPane[books.size()];
        IntStream.range(0, books.size()).forEach(i -> bookPanes[i] = new BookPane(booksList.get(i), startRow == 0));

        int rows = bookPanes.length / 3;
        if (rows * 3 < bookPanes.length) {
            rows++;
        }

        IntStream.range(0, rows).forEach(i -> {
            IntStream.range(0, 3).forEach(j -> {
                int index = i * 3 + j;
                if (index < bookPanes.length) {
                    add(bookPanes[i * 3 + j], j, startRow + i);
                }
            });
        });
    }

    private void showSearch() {
        getChildren().clear();
        Pair<String, List<Book>> lastSearch = SearchService.getInstance().getLastSearch();
        SearchBar searchBar = new SearchBar(lastSearch.getKey());
        add(searchBar, 0, 0, 3, 1);
        appendBooksData(lastSearch.getValue(), 1);
        searchBar.requestFocus();
    }
}
