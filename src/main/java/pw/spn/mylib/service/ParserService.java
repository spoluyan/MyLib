package pw.spn.mylib.service;

import java.util.Set;

import pw.spn.mylib.domain.Book;

public interface ParserService {
    Set<Book> loadRemoteCatalog();

    String loadImage(long bookId);
}
