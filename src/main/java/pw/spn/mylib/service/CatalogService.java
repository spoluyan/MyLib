package pw.spn.mylib.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pw.spn.mylib.domain.Book;
import pw.spn.mylib.domain.BookStatus;

public class CatalogService {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogService.class);

    private final File catalog;

    public CatalogService(final File catalog) {
        this.catalog = catalog;
    }

    @SuppressWarnings("unchecked")
    public Set<Book> load() {
        if (!catalog.exists()) {
            return new HashSet<>();
        }
        try (FileInputStream fis = new FileInputStream(catalog); ObjectInputStream in = new ObjectInputStream(fis);) {
            return (Set<Book>) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            LOG.error("Unable to load catalog.", ex);
        }
        return new HashSet<>();
    }

    public Set<Book> update(final Set<Book> library, final long id, final BookStatus status,
            final Set<Book> remoteBookLibrary) {
        final Optional<Book> book = library.stream().filter(b -> b.getFlibustaID() == id).findFirst();
        if (book.isPresent()) {
            if (status == BookStatus.NO_STATUS) {
                library.remove(book.get());
            } else {
                book.get().setStatus(status);
            }
        } else {
            final Book bb = remoteBookLibrary.parallelStream().filter(b -> b.getFlibustaID() == id).findAny().get();
            bb.setStatus(status);
            library.add(bb);
        }
        save(library);
        return library;
    }

    public Set<Book> update(final Set<Book> library, final long id, final String cover) {
        if (cover != null) {
            final Optional<Book> book = library.stream().filter(b -> b.getFlibustaID() == id).findFirst();
            if (book.isPresent()) {
                book.get().setImage(cover);
                save(library);
            }
        }
        return library;
    }

    private void save(final Set<Book> data) {
        catalog.delete();
        try {
            final File dirs = catalog.getParentFile();
            dirs.mkdirs();
            catalog.createNewFile();
        } catch (final IOException ex) {
            LOG.error("Unable to save catalog.", ex);
            return;
        }
        try (FileOutputStream fos = new FileOutputStream(catalog); ObjectOutputStream out = new ObjectOutputStream(fos);) {
            out.writeObject(data);
        } catch (final IOException ex) {
            LOG.error("Unable to save catalog.", ex);
        }
    }
}
