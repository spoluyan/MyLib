package pw.spn.mylib.domain;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class Author implements Serializable {
    private static final long serialVersionUID = 2376148192891746322L;

    private final String firstName;
    private final String middleName;
    private final String lastName;

    public Author(final String firstName, final String middleName, final String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(firstName)
                .append(StringUtils.isEmpty(firstName) ? StringUtils.EMPTY : StringUtils.SPACE).append(middleName)
                .append(StringUtils.isEmpty(middleName) ? StringUtils.EMPTY : StringUtils.SPACE).append(lastName)
                .toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Author)) {
            return false;
        }
        return toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }
}
