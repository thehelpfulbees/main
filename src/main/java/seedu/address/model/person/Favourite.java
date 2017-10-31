package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a person importance in the addressBook
 * Guarantees: immutable;
 */
public class Favourite {

    public static final String MESSAGE_FAVOURITE_CONSTRAINTS =
            "Person favourite should only be true or false, and it should not be blank";

    public Boolean value = false;

    /**
     * Validates given Favourite.
     *
     * @throws IllegalValueException if given favourite string is invalid.
     */
    public Favourite(String input) throws IllegalValueException {
        requireNonNull(input);
        String trimmedinput = input.trim();
        if (!isValidInput(trimmedinput)) {
            throw new IllegalValueException(MESSAGE_FAVOURITE_CONSTRAINTS);
        }
        if(trimmedinput.equals("true") && !value) {
            this.value = true;
        } else {
            this.value = false;
        }
    }

    /**
     * Inverses the current state of Favourite
     */
    public void Inverse() {
        value = false;
    }
    /**
     * Returns true if a given string is a valid person name.
     */
    public boolean isValidInput(String input) {
        if(input.toLowerCase().equals("true") || input.toLowerCase().equals("false")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        if(value) {
            return "true";
        } else {
            return "false";
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Favourite // instanceof handles nulls
                && this.value.equals(((Favourite) other).value)); // state check
    }
}
