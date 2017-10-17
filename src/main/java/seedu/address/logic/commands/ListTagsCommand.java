package seedu.address.logic.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.tag.Tag;

/**
 * List all tags in the addressbook to the user
 */
public class ListTagsCommand extends Command {
    public static final String COMMAND_WORD = "listtags";
    public static final String COMMAND_WORD_ALIAS1 = "lt";
    public static final String COMMAND_WORD_ALIAS2 = "ltags";

    public static final String MESSAGE_SUCCESS = "You have the following tags: ";


    @Override
    public CommandResult execute() throws CommandException {
        return new CommandResult(MESSAGE_SUCCESS + getListOfAllTagsInString());
    }

    private String getListOfAllTagsInString() {

        ArrayList<Tag> listOfAllTags = getAllTagsWithNoDuplicates();

        ArrayList<String> listOfAllTagNames = getAllTagNamesWithNoDuplicates(listOfAllTags);

        sortListOfAllTagNamesAlphabetically(listOfAllTagNames);

        String tagNamesInListOfAllTags = getSortedTagNamesAsString(listOfAllTagNames);


        return tagNamesInListOfAllTags;
    }

    private String getSortedTagNamesAsString(ArrayList<String> listOfAllTagNames) {
        String tagNamesInListOfAllTags = "";
        for (String tagName : listOfAllTagNames) {
            tagNamesInListOfAllTags = tagNamesInListOfAllTags + "[" + tagName + "]" + " ";
        }

        return tagNamesInListOfAllTags;
    }

    private void sortListOfAllTagNamesAlphabetically(ArrayList<String> listOfAllTagNames) {
        Collections.sort(listOfAllTagNames);
    }

    private ArrayList<String> getAllTagNamesWithNoDuplicates(ArrayList<Tag> listOfAllTags) {
        ArrayList<String> listOfAllTagNames = new ArrayList<String>();

        for (Tag tag : listOfAllTags) {
            listOfAllTagNames.add(tag.getTagName());
        }
        return listOfAllTagNames;
    }

    private ArrayList<Tag> getAllTagsWithNoDuplicates() {
        ArrayList<Tag> listOfAllTags = new ArrayList<Tag>();

        for (ReadOnlyPerson p : model.getAddressBook().getPersonList()) {
            for (Tag tag : p.getTags()) {
                if (!listOfAllTags.contains(tag)) {
                    listOfAllTags.add(tag);
                }
            }
        }
        return listOfAllTags;
    }
}
