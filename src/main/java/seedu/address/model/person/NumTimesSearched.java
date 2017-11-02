package seedu.address.model.person;

import seedu.address.commons.exceptions.IllegalValueException;

//@@author thehelpfulbees
/**
 * Counts number of times a person has been searched for
 * Guarantees: immutable;
 */
public class NumTimesSearched {

    public static int STARTING_VALUE = 0;

    public int value = STARTING_VALUE; //num times searched

    /**
     * Validates given Favourite.
     *
     * @throws IllegalValueException if given favourite string is invalid.
     */
    public NumTimesSearched(int initialValue) throws IllegalValueException {

        this.value = initialValue;
    }

    public NumTimesSearched() {
        this.value = STARTING_VALUE;
    }

    public void incrementValue() {
        value ++;
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
