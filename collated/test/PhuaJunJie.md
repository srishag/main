# PhuaJunJie
###### \java\seedu\address\commons\GoogleAuthenticatorTest.java
``` java
public class GoogleAuthenticatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private GoogleAuthenticator authenticator = new GoogleAuthenticator();

    /**
     * Checks if Login url generated is in the valid format
     */
    @Test
    public void execute_obtain_loginUrl() {
        assertTrue(authenticator.getAuthorizationUrl().contains(
                "https://accounts.google.com/o/oauth2/auth?client_id"));
    }

    /**
     * Checks if Login is authenticated. In this case it is not and null exception is thrown.
     */
    @Test
    public void execute_get_invalidToken() throws Exception {
        thrown.expect(GoogleAuthException.class);
        authenticator.getToken();
    }

    /**
     * Checks if Login is authenticated. In this case it is not and null exception is thrown.
     */
    @Test
    public void execute_get_invalidGooglePersonList() throws Exception {
        thrown.expect(NullPointerException.class);
        authenticator.getConnections(null);
    }
}
```
###### \java\seedu\address\logic\commands\ExportCommandTest.java
``` java
public class ExportCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model;
    private List<Person> googleList;

    @Before
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());
        googleList = new ArrayList<>();
    }

    /**
     * Checks if Login is authenticated. In this case it is not and GoogleAuthException is thrown.
     */
    @Test
    public void execute_require_login() throws Exception {
        thrown.expect(GoogleAuthException.class);
        new ExportCommand();
    }

    /**
     * Test for exporting an empty addressbook. Throws Command exception for empty addressbook
     */
    @Test
    public void execute_commandFailure_noContacts() throws Exception {
        thrown.expect(CommandException.class);
        ExportCommand command = prepareCommand(this.model);
        command.execute();
    }

    /**
     * Test for exporting an empty addressbook when authentication is lost.
     */
    @Test
    public void execute_commandFailure_noAuthenticationToExport() throws Exception {
        thrown.expect(GoogleAuthException.class);
        model.addPerson(BERNICE);
        ExportCommand command = prepareCommand(this.model);
        command.execute();
    }


    /**
     * Test for no importing of contacts to addressbook as all contacts in addressbook are already in google contacts.
     */
    @Test
    public void execute_commandFailure_allGoogleContacts() throws Exception {
        model.addPerson(TypicalGoogleContactsList.FREEDYADDRESSBOOK);

        ExportCommand command = prepareCommand(this.model);
        String expectedMessage = "0 contact/s exported!     All contacts can be now found in google contact";
        assertCommandFailure(command, expectedMessage, model);
    }

    /**
     * Test for converting a addressbook contact into a google contact
     */
    @Test
    public void execute_updateAddressBookContactToGoogle() throws Exception {
        model.addPerson(BERNICE);

        ExportCommand command = prepareCommand(this.model);
        Person aliceGoogleContact = command.createGoogleContact(BERNICE);
        assertEquals(aliceGoogleContact, TypicalGoogleContactsList.BERNICE);
    }
    /**
     * Test for changes in contact after being exported to google
     */
    @Test
    public void execute_exportContactChanges() throws Exception {
        ExportCommand command = prepareCommand(this.model);
        ReadOnlyPerson editedAlice =
                command.getNewAddressBookContact(BERNICE, TypicalGoogleContactsList.BERNICEWITHGOOGLEID);
        assertEquals(editedAlice, TypicalGoogleContactsList.BERNICEADDRESSBOOK);
    }

    /**
     * Test detailed messages for export command.
     */
    @Test
    public void execute_exportDetailedMessages() throws Exception {
        ExportCommand command = prepareCommand(this.model);
        String messageSuccess = command.setCommandMessage(1, 0);
        String messageFailure = command.setCommandMessage(0, 1);
        assertEquals(messageSuccess, "1 contact/s exported!     "
                + "All contacts can be now found in google contact");
        assertEquals(messageFailure, "0 contact/s exported!     1 contact/s failed to export!");
    }

    /**
     * Preparing import command by using a test-only constructor (Using a stub google person list)
     */
    private ExportCommand prepareCommand(Model model) throws CommandException {
        GoogleContactsBuilder builder = null;
        ExportCommand command = new ExportCommand(builder);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts if a command unsuccessfully executed
     */
    private void assertCommandFailure(Command command, String expectedMessage, Model modelstub)
            throws CommandException, GoogleAuthException {
        CommandResult commandResult = command.execute();
        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(modelstub.getAddressBook(), model.getAddressBook());
    }
}
```
###### \java\seedu\address\logic\commands\FindAlphabetCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code FindAlphabetCommand}.
 */
public class FindAlphabetCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsAlphabetsPredicate firstPredicate =
                new NameContainsAlphabetsPredicate(Collections.singletonList("first"));
        NameContainsAlphabetsPredicate secondPredicate =
                new NameContainsAlphabetsPredicate(Collections.singletonList("second"));

        FindAlphabetCommand findFirstCommand = new FindAlphabetCommand(firstPredicate);
        FindAlphabetCommand findSecondCommand = new FindAlphabetCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindAlphabetCommand findFirstCommandCopy = new FindAlphabetCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }
    //Test when no input by user
    @Test
    public void executeZeroAlphabetsNoPersonFound() {
        String expectedMessage = String.format(MESSAGE_NO_ALPHABET_LISTED_OVERVIEW);
        FindAlphabetCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }
    //Test when user inputs partial names
    @Test
    public void executeMultipleAlphabetsMultiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_ALPHABET_LISTED_OVERVIEW, 2);
        FindAlphabetCommand command = prepareCommand("Ku");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, FIONA));
    }
    //Test when user inputs full names
    @Test
    public void executeMultipleKeywordsMultiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_ALPHABET_LISTED_OVERVIEW, 3);
        FindAlphabetCommand command = prepareCommand("Kurz Elle Kunz");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, ELLE, FIONA));
    }

    /**
     * Parses {@code userInput} into a {@code FindCommand}.
     */
    private FindAlphabetCommand prepareCommand(String userInput) {
        FindAlphabetCommand command =
                new FindAlphabetCommand(new NameContainsAlphabetsPredicate(Arrays.asList(userInput.split("\\s+"))));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<ReadOnlyPerson>} is equal to {@code expectedList}<br>
     */
    private void assertCommandSuccess(FindAlphabetCommand command, String expectedMessage,
                                      List<ReadOnlyPerson> expectedList) {
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());

    }
}
```
###### \java\seedu\address\logic\commands\ImportCommandTest.java
``` java
public class ImportCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model;
    private List<Person> googleList;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        googleList = new ArrayList<>();
    }

    /**
     * Checks if Login is authenticated. In this case it is not and GoogleAuthException is thrown.
     */
    @Test
    public void execute_require_login() throws Exception {
        thrown.expect(GoogleAuthException.class);
        new ImportCommand();
    }
    /**
     * Checks if Google contacts is empty. In this case it is empty and null exception is thrown.
     */
    @Test
    public void execute_empty_googleContacts() throws Exception {
        thrown.expect(NullPointerException.class);
        new ImportCommand(googleList).executeUndoableCommand();
    }

    /**
     * Test for normal importing of a google contact
     */
    @Test
    public void execute_commandSuccess_imported() throws Exception {
        googleList.add(TypicalGoogleContactsList.FREDDYGOOGLE);
        ImportCommand command = prepareCommand(googleList, this.model);

        Model modelstub = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        modelstub.addPerson(TypicalGoogleContactsList.FREEDYADDRESSBOOK);
        String expectedMessage = "1 contact/s imported!     0 contact/s failed to import!" + "\n";

        assertCommandSuccess(command, expectedMessage, modelstub);
    }

    /**
     * Test for invalid importing of a google contact due to invalid attributes
     */
    @Test
    public void execute_commandFailure_contactInvalidFormat() throws Exception {
        googleList.add(TypicalGoogleContactsList.MAYGOOGLE);
        ImportCommand command = prepareCommand(googleList, this.model);

        String expectedMessage = "0 contact/s imported!     1 contact/s failed to import!" + "\n"
                + "Contacts already existed : 0     Contacts not in the correct format : 1" + "\n"
                + "Please check the format of the following google contacts :  May";

        assertCommandFailure(command, expectedMessage, this.model);
    }

    /**
     * Test for importing a contact that already exists in the addressbook
     */
    @Test
    public void execute_commandFailure_contactExists() throws Exception {
        model.addPerson(TypicalGoogleContactsList.FREEDYADDRESSBOOK);
        googleList.add(TypicalGoogleContactsList.FREDDYGOOGLE);
        ImportCommand command = prepareCommand(googleList, model);

        String expectedMessage = "0 contact/s imported!     1 contact/s failed to import!" + "\n"
                + "Contacts already existed : 1     Contacts not in the correct format : 0" + "\n";

        assertCommandFailure(command, expectedMessage, model);
    }

    /**
     * Preparing import command by using a test-only constructor (Using a stub google person list)
     */
    private ImportCommand prepareCommand(List<Person> personList, Model model) throws CommandException {
        ImportCommand command = new ImportCommand(personList);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts if a command successfully executed
     */
    private void assertCommandSuccess(Command command, String expectedMessage, Model modelstub)
            throws CommandException, GoogleAuthException {
        CommandResult commandResult = command.execute();
        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(modelstub.getAddressBook(), model.getAddressBook());
    }

    /**
     * Asserts if a command unsuccessfully executed
     */
    private void assertCommandFailure(Command command, String expectedMessage, Model modelstub)
            throws CommandException, GoogleAuthException {
        CommandResult commandResult = command.execute();
        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(modelstub.getAddressBook(), model.getAddressBook());
    }
}
```
###### \java\seedu\address\logic\commands\SyncCommandTest.java
``` java
public class SyncCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model;
    private List<Person> googleList;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        googleList = new ArrayList<>();
    }

    /**
     * Checks if Login is authenticated. In this case it is not.
     */
    @Test
    public void execute_require_login() throws Exception {
        thrown.expect(GoogleAuthException.class);
        new ImportCommand();
    }

    /**
     * Test for normal Syncing of a single google contact
     */
    @Test
    public void execute_syncSuccess() throws Exception {
        model.addPerson(TypicalGoogleContactsList.FREEDYADDRESSBOOK);
        googleList.add(TypicalGoogleContactsList.FREDDYSYNGOOGLE);
        SyncCommand command = prepareCommand(googleList, model);

        Model modelStub = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        modelStub.addPerson(TypicalGoogleContactsList.FREDDYSYNADDRESSBOOK);
        String expectedMessage = "1 contact/s Synced!     0 contact/s failed to Sync!";

        assertCommandSuccess(command, expectedMessage, modelStub);
    }

    /**
     * Test for invalid Syncing of a google contact due to invalid attributes
     */
    @Test
    public void execute_syncFailure_contactInvalidFormat() throws Exception {

        model.addPerson(TypicalGoogleContactsList.MAYADDRESSBOOK);
        googleList.add(TypicalGoogleContactsList.MAYGOOGLE);
        SyncCommand command = prepareCommand(googleList, model);

        String expectedMessage = "0 contact/s Synced!     1 contact/s failed to Sync!" + "\n"
                + "Please check the format of the following google contacts : May";

        assertCommandFailure(command, expectedMessage, model);
    }

    /**
     * Test for syncing a contact that is of no difference than the one in the addressbook
     */
    @Test
    public void execute_commandFailure_contactSimilar() throws Exception {
        model.addPerson(TypicalGoogleContactsList.FREEDYADDRESSBOOK);
        googleList.add(TypicalGoogleContactsList.FREDDYGOOGLE);
        SyncCommand command = prepareCommand(googleList, model);

        String expectedMessage = "0 contact/s Synced!     0 contact/s failed to Sync!";
        assertCommandFailure(command, expectedMessage, model);
    }

    /**
     * Test for syncing a contact that is no longer in the addressbook. The contact will lose its google contacts
     * status
     */
    @Test
    public void execute_commandFailure_contactNoLongerExists() throws Exception {
        model.addPerson(TypicalGoogleContactsList.FREEDYADDRESSBOOK);
        SyncCommand command = prepareCommand(googleList, model);


        String expectedMessage = "1 contact/s Synced!     0 contact/s failed to Sync!";
        Model modelStub = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        modelStub.addPerson(TypicalGoogleContactsList.FREEDYNOTGOOGLEADDRESSBOOK);

        assertCommandFailure(command, expectedMessage, modelStub);
    }

    /**
     * Preparing import command by using a test-only constructor (Using a stub google person list)
     */
    private SyncCommand prepareCommand(List<Person> personList, Model model) throws CommandException {
        SyncCommand command = new SyncCommand(personList);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts if a command successfully executed
     */
    private void assertCommandSuccess(Command command, String expectedMessage, Model modelstub)
            throws CommandException, GoogleAuthException {
        CommandResult commandResult = command.execute();
        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(modelstub.getAddressBook(), model.getAddressBook());
    }

    /**
     * Asserts if a command unsuccessfully executed
     */
    private void assertCommandFailure(Command command, String expectedMessage, Model modelstub)
            throws CommandException, GoogleAuthException {
        CommandResult commandResult = command.execute();
        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(modelstub.getAddressBook(), model.getAddressBook());
    }
}
```
###### \java\seedu\address\logic\parser\FindAlphabetCommandParserTest.java
``` java
public class FindAlphabetCommandParserTest {

    private FindAlphabetCommandParser parser = new FindAlphabetCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindAlphabetCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindAlphabetCommand expectedFindCommand =
                new FindAlphabetCommand(new NameContainsAlphabetsPredicate(Arrays.asList("Fiona", "Amelia")));
        assertParseSuccess(parser, "Fiona Amelia", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Fiona \n \t Amelia  \t", expectedFindCommand);
    }

}
```
###### \java\seedu\address\model\person\GoogleIdTest.java
``` java
public class GoogleIdTest {

    @Test
    public void isValidId() {
        // invalid GoogleId
        assertFalse(GoogleId.isValidGoogleId("")); // empty string
        assertFalse(GoogleId.isValidGoogleId(" ")); // spaces only


        // valid GoogleId
        assertTrue(GoogleId.isValidGoogleId("1234567890")); // numbers
        assertTrue(GoogleId.isValidGoogleId("not GoogleContact")); // Letters
        assertTrue(GoogleId.isValidGoogleId("f2134324")); // alphanumeric

    }
}
```
###### \java\seedu\address\model\person\NameContainsAlphabetsPredicateTest.java
``` java
public class NameContainsAlphabetsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateAlphabetList = Collections.singletonList("first");
        List<String> secondPredicateAlphabetList = Arrays.asList("first", "second");

        NameContainsAlphabetsPredicate firstPredicate = new NameContainsAlphabetsPredicate(firstPredicateAlphabetList);
        NameContainsAlphabetsPredicate secondPredicate =
                new NameContainsAlphabetsPredicate(secondPredicateAlphabetList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NameContainsAlphabetsPredicate firstPredicateCopy =
                new NameContainsAlphabetsPredicate(firstPredicateAlphabetList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void testNameContainsAlphabetsReturnsTrue() {
        // One Alphabet
        NameContainsAlphabetsPredicate predicate = new NameContainsAlphabetsPredicate(Collections.singletonList("A"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Multiple Separated Alphabets
        predicate = new NameContainsAlphabetsPredicate(Arrays.asList("A", "B"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Only One Matching Alphabet Sequence
        predicate = new NameContainsAlphabetsPredicate(Arrays.asList("Bob", "Car"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol").build()));

        // Mixed-Case Alphabets
        predicate = new NameContainsAlphabetsPredicate(Arrays.asList("bO", "aLIC"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Full Keywords
        predicate = new NameContainsAlphabetsPredicate(Collections.singletonList("Alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").build()));
    }

    @Test
    public void testNameDoesNotContainAlphabetsReturnsFalse() {
        // Zero Keywords
        NameContainsAlphabetsPredicate predicate = new NameContainsAlphabetsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Non-matching Alphabets in Sequence
        predicate = new NameContainsAlphabetsPredicate(Arrays.asList("Dob"));
        assertFalse(predicate.test(new PersonBuilder().withName("Bob").build()));

        // Keywords match phone, email and address, but does not match name
        predicate = new NameContainsAlphabetsPredicate(Arrays.asList("12345", "alice@email.com", "Main", "Street"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345")
                .withEmail("alice@email.com").withAddress("Main Street").build()));
    }
}
```
