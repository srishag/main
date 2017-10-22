package seedu.address.testutil;

import com.google.api.services.people.v1.model.Person;

/**
 * List of Stub Google contacts and its version of the addressbook contact
 */
public class TypicalGoogleContactsList {
    public Person freddyGoogle = new GoogleContactBuilder("Freddy", "91234567",
            "freddy@hotmail.com", "Simei Blk 1 avenue 2",
            "1234567891011121310").buildGooglePerson();
    public Person mayGoogle = new GoogleContactBuilder("May", "91234567",
            "may", "Simei Blk 15 avenue 21",
            "1234567891011121311").buildGooglePerson();
    public Person freddySyncGoogle = new GoogleContactBuilder("Freddy", "90000000",
            "freddy@hotmail.com", "Simei Blk 1 avenue 2",
            "1234567891011121310").buildGooglePerson();

    public seedu.address.model.person.Person freddyAddressBook = new GoogleContactBuilder("Freddy",
            "91234567", "freddy@hotmail.com", "Simei Blk 1 avenue 2",
            "1234567891011121310").buildAddressBookPerson();
    public seedu.address.model.person.Person mayAddressBook = new GoogleContactBuilder("May",
            "91234567",
            "may@hotmail.com", "Simei Blk 15 avenue 21",
            "1234567891011121311").buildAddressBookPerson();
    public seedu.address.model.person.Person freddySyncAddressBook = new GoogleContactBuilder("Freddy",
            "90000000", "freddy@hotmail.com", "Simei Blk 1 avenue 2",
            "1234567891011121310").buildAddressBookPerson();

}
