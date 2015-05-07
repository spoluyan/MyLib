package pw.spn.mylib.service;

import java.util.Set;

import pw.spn.mylib.domain.Book;
import pw.spn.mylib.domain.BookStatus;

public interface CatalogService {
    Set<Book> getLocalLibrary();

    Set<Book> getRemoteLibrary();

    void updateBookStatus(long bookId, BookStatus bookStatus);

    void updateCover(long bookId);
}
