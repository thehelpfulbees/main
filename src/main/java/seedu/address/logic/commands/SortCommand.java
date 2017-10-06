package seedu.address.logic.commands;

/**
 * Sorts a the list of persons in alphabetical order
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_SORT_SUCCESS = "Sorted in alphabetical order";

    @Override
    public CommandResult execute() {
        model.sortPerson();
        return new CommandResult(MESSAGE_SORT_SUCCESS);
    }
}
