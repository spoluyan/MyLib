package pw.spn.mylib.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import pw.spn.mylib.Config;
import pw.spn.mylib.domain.Author;
import pw.spn.mylib.domain.Book;

public class ParserService {
    private static final ParserService INSTANCE = new ParserService();

    private static final String CATALOG_URL = "http://www.flibusta.net/catalog/catalog.zip";
    private static final String BOOK_URL = "http://flibusta.net/b/";

    private static final String SEPARATOR = ";";
    private static final String COVER_TEXT = "Cover image";
    private static final String COVER_TAG = "img";
    private static final String ATTR_TITLE = "title";
    private static final String ATTR_ALT = "alt";
    private static final String ATTR_SRC = "src";

    private ParserService() {
    }

    public static ParserService getInstance() {
        return INSTANCE;
    }

    public Set<Book> loadRemoteCatalog() {
        try {
            byte[] catalog = IOUtils.toByteArray(URI.create(CATALOG_URL));

            List<String> content = unzip(catalog);

            return parse(content);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
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
            ex.printStackTrace();
        }
        try (FileInputStream fis = new FileInputStream(tempFile);) {
            return IOUtils.readLines(fis, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    private Set<Book> parse(List<String> content) {
        List<Book> books = new ArrayList<>();
        Map<Long, Integer> parsedBooksMap = new HashMap<>();
        Set<String> acceptedLanguagesSet = Config.getConfig().getAcceptedLanguages();
        IntStream.range(1, content.size()).forEach(i -> {
            String[] line = content.get(i).split(SEPARATOR);
            if (line.length == 9) {
                String language = line[5].trim();
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
        });
        return new HashSet<>(books);
    }

    private int safeCast(String year) {
        return year.isEmpty() ? 0 : Integer.parseInt(year);
    }

    public String loadImage(long bookId) {
        try {
            Document doc = Jsoup.connect(BOOK_URL + bookId).get();
            Optional<Element> img = doc.getElementsByTag(COVER_TAG).stream()
                    .filter(i -> COVER_TEXT.equals(i.attr(ATTR_TITLE)) && COVER_TEXT.equals(i.attr(ATTR_ALT)))
                    .findAny();
            if (img.isPresent()) {
                return img.get().attr(ATTR_SRC);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
