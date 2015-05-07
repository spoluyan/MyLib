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

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import pw.spn.mylib.domain.Book;
import pw.spn.mylib.domain.BookStatus;

@Service
@Lazy(false)
public class CatalogServiceImpl implements CatalogService {
    private static final Log LOG = LogFactory.getLog(CatalogServiceImpl.class);

    @Autowired
    private ParserService parserService;

    @Value("${catalog.file}")
    private String localLibraryFileName;
    private Set<Book> remoteLibrary;
    private Set<Book> localLibrary;

    private File localLibraryFile;

    @PostConstruct
    private void loadCatalogs() {
        localLibraryFile = new File(localLibraryFileName);
        localLibrary = loadLocalLibrary();
        remoteLibrary = parserService.loadRemoteCatalog();
    }

    @Override
    public Set<Book> getLocalLibrary() {
        return localLibrary;
    }

    @SuppressWarnings("unchecked")
    private Set<Book> loadLocalLibrary() {
        File localLibraryFile = new File(localLibraryFileName);
        if (!localLibraryFile.exists()) {
            return new HashSet<>();
        }
        try (FileInputStream fis = new FileInputStream(localLibraryFile);
                ObjectInputStream in = new ObjectInputStream(fis);) {
            return (Set<Book>) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            LOG.error("Unable to load catalog.", ex);
        }
        return new HashSet<>();
    }

    @Override
    public Set<Book> getRemoteLibrary() {
        return remoteLibrary;
    }

    @Override
    public void updateBookStatus(long bookId, BookStatus bookStatus) {
        Optional<Book> book = localLibrary.stream().filter(b -> b.getFlibustaID() == bookId).findFirst();
        if (book.isPresent()) {
            if (bookStatus == BookStatus.NO_STATUS) {
                localLibrary.remove(book.get());
            } else {
                book.get().setStatus(bookStatus);
            }
        } else {
            final Book bb = remoteLibrary.parallelStream().filter(b -> b.getFlibustaID() == bookId).findAny().get();
            bb.setStatus(bookStatus);
            localLibrary.add(bb);
        }
        save(localLibrary);
    }

    @Override
    public void updateCover(long bookId) {
        String cover = parserService.loadImage(bookId);
        if (cover != null) {
            Optional<Book> book = localLibrary.stream().filter(b -> b.getFlibustaID() == bookId).findFirst();
            if (book.isPresent()) {
                book.get().setImage(cover);
                save(localLibrary);
            }
        }
    }

    private void save(Set<Book> library) {
        localLibraryFile.delete();
        try {
            File dirs = localLibraryFile.getParentFile();
            dirs.mkdirs();
            localLibraryFile.createNewFile();
        } catch (IOException ex) {
            LOG.error("Unable to save catalog.", ex);
            return;
        }
        try (FileOutputStream fos = new FileOutputStream(localLibraryFile);
                ObjectOutputStream out = new ObjectOutputStream(fos);) {
            out.writeObject(library);
        } catch (final IOException ex) {
            LOG.error("Unable to save catalog.", ex);
        }
    }
}
