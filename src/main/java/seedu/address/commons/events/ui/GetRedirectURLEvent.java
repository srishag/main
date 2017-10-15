package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;


/**
 * Represents a selection change in the Person List Panel
 */
public class GetRedirectURLEvent extends BaseEvent {


    private String reDirectURL;

    public GetRedirectURLEvent() {
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    //Setter of redirect URL
    public void setRedirectURL(String reDirectURL) {
        this.reDirectURL = reDirectURL;
    }

    //Getter
    public String getReDirectURL(){
        return reDirectURL;
    }
}
