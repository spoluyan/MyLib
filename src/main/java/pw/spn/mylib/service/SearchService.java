package pw.spn.mylib.service;

import java.util.List;

import pw.spn.mylib.domain.Book;

public interface SearchService {
    List<Book> searchBooks(String query);
}
