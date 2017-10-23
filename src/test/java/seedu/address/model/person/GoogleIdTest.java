package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GoogleIdTest {

    @Test
    public void GoogleIdTest() {
        // invalid GoogleId
        assertFalse(GoogleId.isValidGoogleId("")); // empty string
        assertFalse(GoogleId.isValidGoogleId("f2134324")); // alphanumeric
        assertFalse(GoogleId.isValidGoogleId(" ")); // spaces only
        assertFalse(GoogleId.isValidGoogleId("2344-344234")); // special symbols


        // valid GoogleId
        assertTrue(GoogleId.isValidGoogleId("11111"));
        assertTrue(GoogleId.isValidGoogleId("1234567890")); // one character
    }
}
