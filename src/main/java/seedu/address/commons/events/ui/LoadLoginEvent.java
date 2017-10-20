package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;


/**
 * Represents a selection change in the Person List Panel
 */
public class LoadLoginEvent extends BaseEvent {


    private final String authenticationURL;

    public LoadLoginEvent(String authenticationURL) {
        this.authenticationURL = authenticationURL;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public String getAuthenticationURL() {
        return authenticationURL;
    }
}
