package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

public class ImageCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "image";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Changes the profile picture of the specified person in the addressbook.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_IMAGE_SUCCESS = "Changed Profile Picture: %1$s";

    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    public static final String MESSAGE_IMAGE_NOT_FOUND = "Specified image is not found";

    public final Index index;

    public ImageCommand(Index index) {
        this.index = index;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();
        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToEdit = lastShownList.get(index.getZeroBased());

        try {
            model.changeImage(personToEdit);
            ReadOnlyPerson edited = lastShownList.get(index.getZeroBased());
            model.updatePerson(personToEdit, edited);
        } catch (PersonNotFoundException | DuplicatePersonException pnfe) {
            assert false : "The target person cannot be missing";
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_IMAGE_SUCCESS, personToEdit));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ImageCommand // instanceof handles nulls
                && this.index.equals(((ImageCommand) other).index)); // state check
    }
}
