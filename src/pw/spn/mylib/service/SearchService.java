package pw.spn.mylib.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javafx.util.Pair;
import pw.spn.mylib.Config;
import pw.spn.mylib.domain.Book;
import pw.spn.mylib.util.StringUtil;

public class SearchService {
    private static final SearchService INSTANCE = new SearchService();

    private int searchLimit;
    private Pair<String, List<Book>> lastSearch;

    private SearchService() {
        searchLimit = Config.getConfig().getSearchLimit();
        lastSearch = new Pair<>(StringUtil.EMPTY, Collections.emptyList());
    }

    public static SearchService getInstance() {
        return INSTANCE;
    }

    public void searchBooks(String query) {
        CatalogService catalogService = CatalogService.getInstance();
        Set<Book> remoteBookLibrary = catalogService.getRemoteLibrary();
        Set<Book> localLibrary = catalogService.getLocalLibrary();
        List<Book> result = new ArrayList<>();
        Map<Integer, Set<Book>> searchMap = new HashMap<>(3);
        searchMap.put(0, new HashSet<>());
        searchMap.put(1, new HashSet<>());
        searchMap.put(2, new HashSet<>());
        remoteBookLibrary
                .parallelStream()
                .filter(b -> b.getTitle().toLowerCase().contains(query)
                        || String.valueOf(b.getFlibustaID()).equals(query)).forEach(b -> {
                    if (String.valueOf(b.getFlibustaID()).equals(query)) {
                        searchMap.get(0).add(b);
                    } else {
                        if (b.getTitle().toLowerCase().startsWith(query)) {
                            searchMap.get(1).add(b);
                        } else {
                            searchMap.get(2).add(b);
                        }
                    }
                });
        searchMap.get(0).forEach(b -> addBook(result, b, localLibrary));
        if (result.size() < searchLimit) {
            searchMap.get(1).forEach(b -> addBook(result, b, localLibrary));
        }
        if (result.size() < searchLimit) {
            searchMap.get(2).forEach(b -> addBook(result, b, localLibrary));
        }
        lastSearch = new Pair<>(query, result);
    }

    public Pair<String, List<Book>> getLastSearch() {
        return lastSearch;
    }

    private void addBook(List<Book> result, Book b, Set<Book> localLibrary) {
        if (result.size() == searchLimit) {
            return;
        }
        Optional<Book> localBook = localLibrary.stream().filter(book -> book.getFlibustaID() == b.getFlibustaID())
                .findFirst();
        if (localBook.isPresent()) {
            b.setStatus(localBook.get().getStatus());
        }
        result.add(b);
    }
}
