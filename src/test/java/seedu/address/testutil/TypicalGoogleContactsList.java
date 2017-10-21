package seedu.address.testutil;

import com.google.api.services.people.v1.model.*;

public class TypicalGoogleContactsList {

        public Person FreddyGoogle = new GoogleContactBuilder("Freddy", "91234567",
                "freddy@hotmail.com", "Simei Blk 1 avenue 2", "1234567891011121310").buildGooglePerson();

        public Person MayGoogle = new GoogleContactBuilder("May", "91234567",
                "may", "Simei Blk 15 avenue 21", "1234567891011121311").buildGooglePerson();

        public Person FreddySyncGoogle = new GoogleContactBuilder("Freddy", "90000000",
                "freddy@hotmail.com", "Simei Blk 1 avenue 2", "1234567891011121310").buildGooglePerson();


        public seedu.address.model.person.Person FreddyAddressBook = new GoogleContactBuilder("Freddy", "91234567",
                "freddy@hotmail.com", "Simei Blk 1 avenue 2", "1234567891011121310").buildAddressBookPerson();

        public seedu.address.model.person.Person MayAddressBook = new GoogleContactBuilder("May", "91234567",
                "may@hotmail.com", "Simei Blk 15 avenue 21", "1234567891011121311").buildAddressBookPerson();


        public seedu.address.model.person.Person FreddySyncAddressBook = new GoogleContactBuilder("Freddy", "90000000",
                "freddy@hotmail.com", "Simei Blk 1 avenue 2", "1234567891011121310").buildAddressBookPerson();

}
