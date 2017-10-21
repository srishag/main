package seedu.address.testutil;

import com.google.api.services.people.v1.model.*;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Email;
import seedu.address.model.person.GoogleID;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GoogleContactBuilder {
    public String DEFAULT_NAME ;
    public String DEFAULT_PHONE;
    public String DEFAULT_EMAIL;
    public String DEFAULT_ADDRESS;
    public String DEFAULT_GOOGLEID;

    public GoogleContactBuilder(String name, String phone, String email, String address, String googleID){
        this.DEFAULT_NAME = name;
        this.DEFAULT_PHONE = phone;
        this.DEFAULT_EMAIL = email;
        this.DEFAULT_ADDRESS = address;
        this.DEFAULT_GOOGLEID = googleID;
    }
    public Person buildGooglePerson(){
        Person contactToCreate = new Person();

        List names = new ArrayList();
        List email = new ArrayList();
        List phone = new ArrayList();
        List address = new ArrayList();

        names.add(new Name().setDisplayName(DEFAULT_NAME));
        email.add(new EmailAddress().setValue(DEFAULT_EMAIL));
        phone.add(new PhoneNumber().setValue(DEFAULT_PHONE));
        address.add(new Address().setStreetAddress(DEFAULT_ADDRESS));

        return contactToCreate.setNames(names).setEmailAddresses(email).setPhoneNumbers(phone).
                setAddresses(address).setResourceName(DEFAULT_GOOGLEID);
    }

    public seedu.address.model.person.Person buildAddressBookPerson(){
        seedu.address.model.person.Person person = null;
        try{
        seedu.address.model.person.Name name = new seedu.address.model.person.Name(DEFAULT_NAME);
        Phone phone = new Phone(DEFAULT_PHONE);
        Email email = new Email(DEFAULT_EMAIL);
        seedu.address.model.person.Address address = new seedu.address.model.person.Address(DEFAULT_ADDRESS);
        GoogleID ID = new GoogleID(DEFAULT_GOOGLEID.substring(8));

        Tag tag = new Tag("GoogleContact");
        Set<Tag> Tags = new HashSet<>();
        Tags.add(tag);
        person = new seedu.address.model.person.Person(name, phone, email, address, Tags, ID);
        } catch (IllegalValueException e){}

        return person;
    }
}
