package pw.spn.mylib.domain;

import java.io.Serializable;

import pw.spn.mylib.util.StringUtil;

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
                .append(StringUtil.isEmpty(firstName) ? StringUtil.EMPTY : StringUtil.SPACE).append(middleName)
                .append(StringUtil.isEmpty(middleName) ? StringUtil.EMPTY : StringUtil.SPACE).append(lastName)
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
