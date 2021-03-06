package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_DUPLICATE_PERSON;
import static seedu.address.commons.core.Messages.MESSAGE_MISSING_PERSON;
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
            + ": Changes the profile picture of the specified person in the address book.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    static final String MESSAGE_IMAGE_SUCCESS = "Changed Profile Picture: %1$s";
    static final String DEFAULT = "default";

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

        ReadOnlyPerson personToEdit = updateAddressBook(lastShownList);
        return new CommandResult(String.format(MESSAGE_IMAGE_SUCCESS, personToEdit));
    }

    /**
     * Updates address book with new {@code Person} with new profile picture
     *
     * @param lastShownList List of all current {@code Person} in address book
     * @return Edited {@code Person}
     * @throws CommandException when a duplicate of the new person is found in the address book
     */
    private ReadOnlyPerson updateAddressBook(List<ReadOnlyPerson> lastShownList) throws CommandException {
        ReadOnlyPerson personToEdit = lastShownList.get(index.getZeroBased());
        ReadOnlyPerson editedPerson;
        try {
            editedPerson = updateDisplayPicture(lastShownList, personToEdit);
            model.updatePerson(personToEdit, editedPerson);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            assert false : MESSAGE_MISSING_PERSON;
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateListToShowAll();
        return personToEdit;
    }

    /**
     * Updates profile picture of {@code Person} according to selected mode (remove/ edit)
     *
     * @param lastShownList List of all current {@code Person} in address book
     * @param personToEdit Selected {@code Person} to edit
     * @return {@code Person} with new profile picture
     * @throws PersonNotFoundException When selected person is not found in address book
     */
    private ReadOnlyPerson updateDisplayPicture(List<ReadOnlyPerson> lastShownList, ReadOnlyPerson personToEdit)
            throws PersonNotFoundException {
        ReadOnlyPerson editedPerson;
        if (remove) {
            editedPerson = removeDisplayPicture(personToEdit);
        } else {
            editedPerson = selectDisplayPicture(lastShownList, personToEdit);
        }
        return editedPerson;
    }

    /**
     * Opens file browser to choose new image
     *
     * @param lastShownList List of all current {@code Person} in address book
     * @param personToEdit Selected {@code Person} to edit
     * @return {@code Person} with new profile picture
     * @throws PersonNotFoundException When selected person is not found in address book
     */
    private ReadOnlyPerson selectDisplayPicture(List<ReadOnlyPerson> lastShownList, ReadOnlyPerson personToEdit)
            throws PersonNotFoundException {
        ReadOnlyPerson editedPerson;
        model.changeImage(personToEdit);
        editedPerson = lastShownList.get(index.getZeroBased());
        return editedPerson;
    }

    /**
     * Removes profile picture of selected person and set it to default
     *
     * @param personToEdit Selected {@code Person} to edit
     * @return {@code Person} with default profile picture
     */
    private Person removeDisplayPicture(ReadOnlyPerson personToEdit) {
        return new Person(personToEdit.getName(), personToEdit.getPhone(),
                            personToEdit.getEmail(), personToEdit.getAddress(), personToEdit.getRemark(),
                            personToEdit.getBirthday(), personToEdit.getTags(), new ProfilePicture(DEFAULT),
                            personToEdit.getFavourite());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ImageCommand // instanceof handles nulls
                && this.index.equals(((ImageCommand) other).index)
                && this.remove == (((ImageCommand) other).remove)); // state check
    }
}
