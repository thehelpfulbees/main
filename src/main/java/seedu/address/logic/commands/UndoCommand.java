package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Undo the previous {@code UndoableCommand}.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String COMMAND_ALIAS = "u";

    public static final String MESSAGE_SUCCESS = "Undo success!";
    public static final String MESSAGE_FAILURE = "No more commands to undo!";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Undo Commands identified by the index number used. If no index number, assumed to be 1.\n"
            + "Parameters: (POSITIVE INDEX)\n"
            + "Example: " + COMMAND_WORD + ""
            + "Example: " + COMMAND_WORD + " 1";

    private Index numUndo;

    public UndoCommand(Index numUndo) {
        this.numUndo = numUndo;
    }

    //@@author justintkj
    @Override
    public CommandResult execute() throws CommandException {
        requireAllNonNull(model, undoRedoStack);

        if (numUndo.getOneBased() > undoRedoStack.getUndoStackSize()) {
            throw new CommandException("Maximum undo size: " + undoRedoStack.getUndoStackSize());
        }

        for (int i = 0; i < numUndo.getOneBased(); i++) {
            if (!undoRedoStack.canUndo()) {
                throw new CommandException(MESSAGE_FAILURE);
            }

            undoRedoStack.popUndo().undo();
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public void setData(Model model, CommandHistory commandHistory, UndoRedoStack undoRedoStack) {
        this.model = model;
        this.undoRedoStack = undoRedoStack;
    }
}
