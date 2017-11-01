package seedu.address.model.person;

import seedu.address.commons.exceptions.IllegalValueException;

//@@author thehelpfulbees

/**
 * Counts number of times a person has been searched for
 * Guarantees: immutable;
 */
public class NumTimesSearched {

    public int value = 0; //num times searched

    /**
     * Validates given Favourite.
     *
     * @throws IllegalValueException if given favourite string is invalid.
     */
    public NumTimesSearched() throws IllegalValueException {
        this.value = 0;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NumTimesSearched // instanceof handles nulls
                && this.value == ((NumTimesSearched) other).value); // state check
    }
}
