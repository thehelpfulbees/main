package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ProfilePicture;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

//@@author liliwei25
/**
 * Command to add/edit/remove image of Person
 */
public class ImageCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "image";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Changes the profile picture of the specified person in the addressbook.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_IMAGE_SUCCESS = "Changed Profile Picture: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "Duplicate person in addressbook";
    public static final String DEFAULT = "default";
    private static final String MESSAGE_MISSING_PERSON = "The target person cannot be missing";

    public final Index index;
    public final boolean remove;

    public ImageCommand(Index index, boolean remove) {
        requireNonNull(index);

        this.index = index;
        this.remove = remove;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToEdit = lastShownList.get(index.getZeroBased());
        try {
            if (remove) {
                Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(),
                        personToEdit.getEmail(), personToEdit.getAddress(), personToEdit.getRemark(),
                        personToEdit.getBirthday(), personToEdit.getTags(), new ProfilePicture(DEFAULT),
                        personToEdit.getFavourite());
                model.updatePerson(personToEdit, editedPerson);
            } else {
                model.changeImage(personToEdit);
                ReadOnlyPerson edited = lastShownList.get(index.getZeroBased());
                model.updatePerson(personToEdit, edited);
            }
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            assert false : MESSAGE_MISSING_PERSON;
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateListToShowAll();
        return new CommandResult(String.format(MESSAGE_IMAGE_SUCCESS, personToEdit));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ImageCommand // instanceof handles nulls
                && this.index.equals(((ImageCommand) other).index)); // state check
    }
}
