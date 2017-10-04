package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's remark in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidRemark(String)}
 */
public class Remark {

    public static final String MESSAGE_REMARKS_CONSTRAINTS =
            "Person remarks can take any values, and it can be blank";

    public final String value;

    /**
     * Validates given remark.
     *
     * @throws IllegalValueException if given remark string is invalid.
     */
    public Remark(String remark) throws IllegalValueException {
        requireNonNull(remark);
        if (!isValidRemark(remark)) {
            throw new IllegalValueException(MESSAGE_REMARKS_CONSTRAINTS);
        }
        this.value = remark;
    }

    /**
     * Returns true if a given string is a valid person remark.
     */
    public static boolean isValidRemark(String test) {
        return test != null;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Remark // instanceof handles nulls
                && this.value.equals(((Remark) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
