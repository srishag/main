package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a person's Facebook address
 * Guarantees: immutable; is always valid
 */
public class FacebookAddress {

    public static final String MESSAGE_FACEBOOKADDRESS_CONSTRAINTS =
            "Person Facebook address can take any values, can even be blank";

    public final String value;

    public FacebookAddress(String facebookAddress) {
        requireNonNull(facebookAddress);
        this.value = facebookAddress;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this //short circuit if same object
                || (other instanceof FacebookAddress //instanceof handles nulls
                && this.value.equals(((FacebookAddress) other).value));

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
