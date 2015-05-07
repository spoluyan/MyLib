package pw.spn.mylib.web;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pw.spn.mylib.domain.Book;
import pw.spn.mylib.domain.BookStatus;
import pw.spn.mylib.service.CatalogService;

@RestController
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @RequestMapping(value = "/catalog")
    public Set<Book> getLocalLibrary() {
        return catalogService.getLocalLibrary();
    }

    @RequestMapping(value = "/book/{id}/{status}")
    public void updateBookStatus(@PathVariable long id, @PathVariable String status) {
        BookStatus bookStatus = BookStatus.valueOf(status);
        catalogService.updateBookStatus(id, bookStatus);
    }

    @RequestMapping(value = "/cover/{id}")
    public void updateCover(@PathVariable long id) {
        catalogService.updateCover(id);
    }
}
