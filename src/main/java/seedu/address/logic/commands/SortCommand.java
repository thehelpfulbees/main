package seedu.address.logic.commands;

/**
 * Sorts a the list of persons in alphabetical order
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_SORT_SUCCESS = "Sorted in asceding order: ";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts the person identified by the index number used in the last person listing.\n"
            + "Parameters: name/num/address/email (the type of sort to be executed)\n"
            + "Example: " + COMMAND_WORD + " name"
            + "Example: " + COMMAND_WORD + " num"
            + "Example: " + COMMAND_WORD + " address"
            + "Example: " + COMMAND_WORD + " remark"
            + "Example: " + COMMAND_WORD + " email";


    private String sortType;

    public SortCommand(String sortType) {
        this.sortType = sortType;
    }

    @Override
    public CommandResult execute() {
            model.sortPerson(sortType);
            return new CommandResult(MESSAGE_SORT_SUCCESS + sortType);

    }
}
