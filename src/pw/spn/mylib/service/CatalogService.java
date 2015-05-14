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
import java.util.stream.Collectors;

import pw.spn.mylib.Config;
import pw.spn.mylib.domain.Book;
import pw.spn.mylib.domain.BookStatus;

public class CatalogService {
    private static final CatalogService INSTANCE = new CatalogService();

    private ParserService parserService;

    private Set<Book> remoteLibrary;
    private Set<Book> localLibrary;
    private File localLibraryFile;

    private CatalogService() {
        parserService = ParserService.getInstance();
        localLibraryFile = new File(Config.getConfig().getLocalLibraryFileName());
        localLibrary = loadLocalLibrary();
    }

    public static CatalogService getInstance() {
        return INSTANCE;
    }

    public Set<Book> getLocalLibrary() {
        return localLibrary;
    }

    public Set<Book> getReadBooks() {
        return filterByStatus(BookStatus.READ);
    }

    public Set<Book> getReadingBooks() {
        return filterByStatus(BookStatus.READING);
    }

    public Set<Book> getGoingToReadBooks() {
        return filterByStatus(BookStatus.GOING_TO_READ);
    }

    public Set<Book> filterByStatus(BookStatus status) {
        return localLibrary.stream().filter(book -> book.getStatus() == status).collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    private Set<Book> loadLocalLibrary() {
        if (!localLibraryFile.exists()) {
            return new HashSet<>();
        }
        try (FileInputStream fis = new FileInputStream(localLibraryFile);
                ObjectInputStream in = new ObjectInputStream(fis);) {
            return (Set<Book>) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public Set<Book> getRemoteLibrary() {
        if (remoteLibrary == null) {
            remoteLibrary = parserService.loadRemoteCatalog();
        }
        return remoteLibrary;
    }

    public void updateBookStatus(long bookId, BookStatus bookStatus) {
        Book bb = remoteLibrary.parallelStream().filter(b -> b.getFlibustaID() == bookId).findAny().get();
        bb.setStatus(bookStatus);
        Optional<Book> book = localLibrary.stream().filter(b -> b.getFlibustaID() == bookId).findFirst();
        if (book.isPresent()) {
            if (bookStatus == BookStatus.NO_STATUS) {
                localLibrary.remove(book.get());
            } else {
                book.get().setStatus(bookStatus);
            }
        } else {
            localLibrary.add(bb);
        }
        save(localLibrary);
    }

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
            ex.printStackTrace();
            return;
        }
        try (FileOutputStream fos = new FileOutputStream(localLibraryFile);
                ObjectOutputStream out = new ObjectOutputStream(fos);) {
            out.writeObject(library);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
