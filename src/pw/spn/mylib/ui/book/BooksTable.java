package pw.spn.mylib.ui.book;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import pw.spn.mylib.domain.Book;
import pw.spn.mylib.service.CatalogService;
import pw.spn.mylib.ui.CurrentState;

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
        }
        requestFocus();
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

    private void showBooksData(Set<Book> books) {
        List<Book> booksList = new ArrayList<>(books);
        getChildren().clear();

        BookPane[] bookPanes = new BookPane[books.size()];
        IntStream.range(0, books.size()).forEach(i -> bookPanes[i] = new BookPane(booksList.get(i)));

        int rows = bookPanes.length / 3;
        if (rows * 3 < bookPanes.length) {
            rows++;
        }

        IntStream.range(0, rows).forEach(i -> {
            IntStream.range(0, 3).forEach(j -> {
                int index = i * 3 + j;
                if (index < bookPanes.length) {
                    add(bookPanes[i * 3 + j], j, i);
                }
            });
        });
    }

}
