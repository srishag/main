package seedu.address.logic.commands.exceptions;

//@@author PhuaJunJie
/**
 * Represents an error which occurs during authentication process
 */
public class GoogleAuthException extends Exception {
    public GoogleAuthException(String message) {
        super(message);
    }
}
