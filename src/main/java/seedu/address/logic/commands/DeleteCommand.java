package seedu.address.logic.commands;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Deletes a person identified using it's last displayed index from the address book.
 */
public class DeleteCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "delete";
    public static final String COMMAND_ALIAS = "d";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person(s): %1$s";
    private static final String MESSAGE_MISSING_PERSON = "The target person cannot be missing";

    public final Index[] targetIndex;

    public DeleteCommand(Index[] targetIndex) {
        this.targetIndex = targetIndex;
    }

    //@@author liliwei25
    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();
        StringJoiner joiner = new StringJoiner(", ");
        for (int i = targetIndex.length - 1; i >= 0; i--) {
            if (targetIndex[i].getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            ReadOnlyPerson personToDelete = lastShownList.get(targetIndex[i].getZeroBased());

            try {
                model.deletePerson(personToDelete);
                joiner.add(personToDelete.getName().toString());
            } catch (PersonNotFoundException pnfe) {
                assert false : MESSAGE_MISSING_PERSON;
            }
        }

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, joiner.toString()));
    }
    //@@author
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && Arrays.equals(this.targetIndex, ((DeleteCommand) other).targetIndex)); // state check
    }
}
