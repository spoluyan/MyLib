package pw.spn.mylib.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Book implements Serializable {
    private static final long serialVersionUID = -6164445094536932020L;

    private final String title;
    private final int year;
    private final long flibustaID;
    private BookStatus status;
    private String image;
    private final Set<Author> authors;

    public Book(final String title, final int year, final long flibustaID) {
        this.title = title;
        this.year = year;
        this.flibustaID = flibustaID;
        this.status = BookStatus.NO_STATUS;
        this.authors = new HashSet<>();
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Book)) {
            return false;
        }
        final Book book = (Book) obj;
        return flibustaID == book.getFlibustaID();
    }

    @Override
    public int hashCode() {
        return Long.valueOf(flibustaID).hashCode();
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public long getFlibustaID() {
        return flibustaID;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(final BookStatus status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public Set<Author> getAuthors() {
        return authors;
    }
}
