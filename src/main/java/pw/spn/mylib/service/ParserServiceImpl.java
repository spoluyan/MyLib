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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pw.spn.mylib.domain.Author;
import pw.spn.mylib.domain.Book;

@Service
public class ParserServiceImpl implements ParserService {
    private static final Log LOG = LogFactory.getLog(ParserServiceImpl.class);

    private static final String SEPARATOR = ";";
    private static final String COVER_TEXT = "Cover image";
    private static final String COVER_TAG = "img";
    private static final String ATTR_TITLE = "title";
    private static final String ATTR_ALT = "alt";
    private static final String ATTR_SRC = "src";

    @Value("${catalog.url}")
    private String catalogURL;

    @Value("${accepted-languages}")
    private String[] acceptedLanguages;

    @Value("${book.url}")
    private String bookURL;

    @Override
    public Set<Book> loadRemoteCatalog() {
        LOG.info("Retreiving last catalog from " + catalogURL);
        try {
            byte[] catalog = IOUtils.toByteArray(URI.create(catalogURL));

            LOG.info("Unpacking catalog.");
            List<String> content = unzip(catalog);

            return parse(content);
        } catch (IOException | RuntimeException e) {
            LOG.error("Exception occured during parsing.", e);
        }
        return Collections.emptySet();
    }

    private List<String> unzip(byte[] catalog) throws IOException {
        File tempFile = File.createTempFile("" + System.currentTimeMillis(), "tmp");
        try (ByteArrayInputStream bais = new ByteArrayInputStream(catalog);
                ZipInputStream zis = new ZipInputStream(bais);
                FileOutputStream fos = new FileOutputStream(tempFile);) {
            zis.getNextEntry();
            IOUtils.copy(zis, fos);
            zis.closeEntry();
        } catch (IOException ex) {
            LOG.error("Error during parsing catalog.", ex);
        }
        try (FileInputStream fis = new FileInputStream(tempFile);) {
            return IOUtils.readLines(fis);
        } catch (IOException ex) {
            LOG.error("Error during parsing catalog.", ex);
        }
        return Collections.emptyList();
    }

    private Set<Book> parse(List<String> content) {
        List<Book> books = new ArrayList<>();
        Map<Long, Integer> parsedBooksMap = new HashMap<>();
        for (int i = 1; i < content.size(); i++) {
            String[] line = content.get(i).split(SEPARATOR);
            if (line.length == 9) {
                String language = line[5].trim();

                Set<String> acceptedLanguagesSet = new HashSet<>(Arrays.asList(acceptedLanguages));
                if (acceptedLanguagesSet.contains(language.toLowerCase())) {
                    String lastName = line[0].trim();
                    String firstName = line[1].trim();
                    String middleName = line[2].trim();
                    String title = line[3].trim();
                    String year = line[6].trim();
                    String flibustaID = line[8].trim();

                    Author author = new Author(firstName, middleName, lastName);
                    Book book = new Book(title, safeCast(year), Long.parseLong(flibustaID));
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

    private int safeCast(String year) {
        return year.isEmpty() ? 0 : Integer.parseInt(year);
    }

    @Override
    public String loadImage(long bookId) {
        try {
            Document doc = Jsoup.connect(bookURL + bookId).get();
            Optional<Element> img = doc.getElementsByTag(COVER_TAG).stream()
                    .filter(i -> COVER_TEXT.equals(i.attr(ATTR_TITLE)) && COVER_TEXT.equals(i.attr(ATTR_ALT)))
                    .findAny();
            if (img.isPresent()) {
                return img.get().attr(ATTR_SRC);
            }
        } catch (IOException ex) {
            LOG.info("Unable to load cover for book with id " + bookId);
        }
        return null;
    }

}
