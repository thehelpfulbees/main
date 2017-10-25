package seedu.address.logic.commands;

import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.TagsContainKeywordPredicate;

import java.util.function.Predicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final String COMMAND_ALIAS = "f";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final Predicate<ReadOnlyPerson> predicate;

    public FindCommand(Predicate<ReadOnlyPerson> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand) // other is FindCommand AND...
                && (((this.predicate instanceof NameContainsKeywordsPredicate) //it has a matching Name...Predicate OR
                && (((FindCommand) other).predicate) instanceof NameContainsKeywordsPredicate)
                && (this.predicate.equals(((FindCommand) other).predicate)))
                || (((this.predicate instanceof TagsContainKeywordPredicate) //it has a matching Tag...Predicate
                && (((FindCommand) other).predicate) instanceof TagsContainKeywordPredicate)
                && (this.predicate.equals(((FindCommand) other).predicate))); // state check
    }
}
