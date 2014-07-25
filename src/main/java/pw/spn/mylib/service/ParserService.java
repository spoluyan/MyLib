package pw.spn.mylib.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pw.spn.mylib.domain.Author;
import pw.spn.mylib.domain.Book;

public class ParserService {

    private static final Logger LOG = LoggerFactory.getLogger(ParserService.class);

    private static final URI CATALOG_URL = URI.create("http://www.flibusta.net/catalog/catalog.zip");
    private static final String BOOK_URL = "http://flibusta.net/b/";
    private static final String SEPARATOR = ";";
    private static final Set<String> ACCEPTED_LANGUAGES = new HashSet<>(Arrays.asList("", "be", "en", "ru", "uk"));
    private static final String COVER_TEXT = "Cover image";
    private static final String COVER_TAG = "img";
    private static final String ATTR_TITLE = "title";
    private static final String ATTR_ALT = "alt";
    private static final String ATTR_SRC = "src";

    public Set<Book> loadRemoteCatalog() {
        LOG.info("Retreiving last catalog from {}.", CATALOG_URL);
        try {
            final byte[] catalog = IOUtils.toByteArray(CATALOG_URL);
            LOG.info("Catalog size is {} bytes.", catalog.length);

            LOG.info("Unpacking catalog.");
            final List<String> content = unzip(catalog);
            LOG.info("Books in catalog: {}.", content.size());

            return parse(content);
        } catch (IOException | RuntimeException e) {
            LOG.error("Exception occured during parsing.", e);
        }
        return Collections.emptySet();
    }

    public String loadImage(final long id) {
        try {
            final Document doc = Jsoup.connect(BOOK_URL + id).get();
            final Optional<Element> img = doc.getElementsByTag(COVER_TAG).stream()
                    .filter(i -> COVER_TEXT.equals(i.attr(ATTR_TITLE)) && COVER_TEXT.equals(i.attr(ATTR_ALT)))
                    .findAny();
            if (img.isPresent()) {
                return img.get().attr(ATTR_SRC);
            }
        } catch (final IOException ex) {
            LOG.info("Unable to load cover for book with id {}", id);
        }
        return null;
    }

    private List<String> unzip(final byte[] catalog) throws IOException {
        final File tempFile = File.createTempFile("" + System.currentTimeMillis(), "tmp");
        try (ByteArrayInputStream bais = new ByteArrayInputStream(catalog);
                ZipInputStream zis = new ZipInputStream(bais);
                FileOutputStream fos = new FileOutputStream(tempFile);) {
            zis.getNextEntry();
            IOUtils.copy(zis, fos);
            zis.closeEntry();
        } catch (final IOException ex) {
            LOG.error("Error during parsing catalog.", ex);
        }
        try (FileInputStream fis = new FileInputStream(tempFile);) {
            return IOUtils.readLines(fis);
        } catch (final IOException ex) {
            LOG.error("Error during parsing catalog.", ex);
        }
        return Collections.emptyList();
    }

    private Set<Book> parse(final List<String> content) {
        final List<Book> books = new ArrayList<>();
        final Map<Long, Integer> parsedBooksMap = new HashMap<>();
        for (int i = 1; i < content.size(); i++) {
            final String[] line = content.get(i).split(SEPARATOR);
            if (line.length == 9) {
                final String language = line[5].trim();

                if (ACCEPTED_LANGUAGES.contains(language.toLowerCase())) {
                    final String lastName = line[0].trim();
                    final String firstName = line[1].trim();
                    final String middleName = line[2].trim();
                    final String title = line[3].trim();
                    final String year = line[6].trim();
                    final String flibustaID = line[8].trim();

                    final Author author = new Author(firstName, middleName, lastName);
                    final Book book = new Book(title, safeCast(year), Long.parseLong(flibustaID));
                    int index = parsedBooksMap.getOrDefault(book.getFlibustaID(), -1);
                    if (index == -1) {
                        books.add(book);
                        index = books.size() - 1;
                        parsedBooksMap.put(book.getFlibustaID(), index);
                    }
                    books.get(index).getAuthors().add(author);
                }
            }
        }
        return new HashSet<>(books);
    }

    private int safeCast(final String year) {
        return year.isEmpty() ? 0 : Integer.parseInt(year);
    }
}
