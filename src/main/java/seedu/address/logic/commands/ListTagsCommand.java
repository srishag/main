package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.tag.Tag;

/**
 * List all tags in the addressbook to the user
 */
public class ListTagsCommand extends Command {
    public static final String COMMAND_WORD = "listtags";

    public static final String MESSAGE_SUCCESS = "You have the following tags: ";

    private static List<Tag> listOfAllTags;
    private static String namesInListOfAllTags;


    @Override
    public CommandResult execute() throws CommandException {

        listOfAllTags = new ArrayList<Tag>();
        namesInListOfAllTags = "";

      for (ReadOnlyPerson p : model.getAddressBook().getPersonList()){
          for (Tag tag : p.getTags()){
              if (!listOfAllTags.contains(tag)) {
                  listOfAllTags.add(tag);
                  namesInListOfAllTags = namesInListOfAllTags + " " + tag.getTagName();
              }
          }
      }

        return new CommandResult(MESSAGE_SUCCESS + namesInListOfAllTags);
    }
}
