package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's Google ID in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidGoogleID(String)}
 */
public class GoogleID{


    public static final String MESSAGE_GOOGLEID_CONSTRAINTS =
            "GoogleID can only contain numbers, and should be at least 3 digits long";
    public static final String GOOGLEID_VALIDATION_REGEX = "[^\\s].*";
    public final String value;

    /**
     * Validates given phone number.
     *
     * @throws IllegalValueException if given phone string is invalid.
     */
    public GoogleID(String ID) throws IllegalValueException {
        requireNonNull(ID);
        String trimmedID = ID.trim();
        if (!isValidGoogleID(trimmedID)) {
            throw new IllegalValueException(MESSAGE_GOOGLEID_CONSTRAINTS);
        }
        this.value = trimmedID;
    }

    /**
     * Returns true if a given string is a valid person Google ID.
     */
    public static boolean isValidGoogleID(String test) {
        return test.matches(GOOGLEID_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof GoogleID // instanceof handles nulls
                && this.value.equals(((GoogleID) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
