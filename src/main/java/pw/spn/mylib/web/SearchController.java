package pw.spn.mylib.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pw.spn.mylib.domain.Book;
import pw.spn.mylib.service.SearchService;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/search/{q}")
    public List<Book> search(@PathVariable String q) {
        return searchService.searchBooks(q.toLowerCase());
    }
}
