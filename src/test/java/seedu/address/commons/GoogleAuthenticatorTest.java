package seedu.address.commons;


import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class GoogleAuthenticatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    GoogleAuthenticator authenticator = new GoogleAuthenticator();

    /**
     * Checks if Login url generated is in the valid format
     */
    @Test
    public void execute_obtain_loginUrl() {
        String url =authenticator.getAuthorizationUrl();
        assertTrue(authenticator.getAuthorizationUrl().contains(
                "https://accounts.google.com/o/oauth2/auth?client_id=650819214900"));
    }

    /**
     * Checks if Login is authenticated. In this case it is not and null exception is thrown.
     */
    @Test
    public void execute_get_invalid_token() throws Exception {
        thrown.expect(NullPointerException.class);
        authenticator.getToken();
    }

    /**
     * Checks if Login is authenticated. In this case it is not and null exception is thrown.
     */
    @Test
    public void execute_get_invalid_googlePersonList() throws Exception {
        thrown.expect(NullPointerException.class);
        authenticator.getConnections(null);
    }
}
