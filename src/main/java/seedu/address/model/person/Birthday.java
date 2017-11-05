package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import seedu.address.commons.exceptions.IllegalValueException;

//@@author liliwei25
/**
 * Represents a Person's birthday in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidBirthday(String[], LocalDate)}
 */
public class Birthday {

    public static final String MESSAGE_BIRTHDAY_CONSTRAINTS =
            "Birthdays can only contain numbers, and should be in the format dd-mm-yyyy";
    private static final String BIRTHDAY_VALIDATION_REGEX = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1"
            + "|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\"
            + "/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468]"
            + "[048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\"
            + "4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
    private static final int DEFAULT_VALUE = 0;
    private static final String NOT_SET = "Not Set";
    private static final String EMPTY = "";
    private static final String REMOVE = "remove";
    private static final String MESSAGE_LATE_DATE = "Date given should be before today %1$s";
    private static final int MIN_MONTHS = 1;
    private static final int MAX_MONTHS = 12;
    private static final int MIN_DAYS = 1;
    public static final String MESSAGE_WRONG_DATE = "Date entered is in wrong";
    public static final String DASH = "-";
    private static final int DAY_POS = 0;
    private static final int MONTH_POS = 1;
    private static final int YEAR_POS = 2;

    public final String value;
    private final int day;
    private final int month;
    private final int year;

    /**
     * Validates given birthday.
     *
     * @throws IllegalValueException if given birthday string is invalid.
     */
    public Birthday(String birthday) throws IllegalValueException {
        requireNonNull(birthday);
        String trimmedBirthday = birthday.trim();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if (birthday.equals(EMPTY) || birthday.equals(NOT_SET) || birthday.equals(REMOVE)) {
            this.value = NOT_SET;
            day = month = year = DEFAULT_VALUE;
        } else {
            LocalDate inputBirthday;
            try {
                inputBirthday = LocalDate.parse(birthday, formatter);
            } catch (DateTimeParseException dtpe) {
                throw new IllegalValueException(MESSAGE_WRONG_DATE);
            }
            if (!isValidBirthday(birthday.split(DASH), inputBirthday)) {
                throw new IllegalValueException(MESSAGE_WRONG_DATE);
            } else if (!isDateCorrect(inputBirthday)) {
                throw new IllegalValueException(String.format(MESSAGE_LATE_DATE, LocalDate.now().format(formatter)));
            } else {
                this.value = trimmedBirthday;
                this.day = inputBirthday.getDayOfMonth();
                this.month = inputBirthday.getMonthValue();
                this.year = inputBirthday.getYear();
            }
        }
    }

    /**
     * Determines if date entered by user is correct and ensures that it is not after current date
     */
    private static boolean isDateCorrect(LocalDate birthday) {
        return birthday.isBefore(LocalDate.now()) || birthday.equals(LocalDate.now());
    }

    /**
     * Returns true if a given string is a valid person birthday. Requires both the input string and the parsed date
     * since the parsed date will be resolved to the correct values
     */
    public static boolean isValidBirthday(String[] test, LocalDate testBirthday) {
        int day = Integer.parseInt(test[DAY_POS]);
        int month = Integer.parseInt(test[MONTH_POS]);

        return month >= MIN_MONTHS
                && month <= MAX_MONTHS
                && day >= MIN_DAYS
                && day <= testBirthday.lengthOfMonth();
    }

    public static boolean isValidBirthday(String test) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String[] split = test.split(DASH);
        LocalDate testBirthday;
        try {
            testBirthday = LocalDate.parse(test, formatter);
        } catch (DateTimeParseException dtpe) {
            return false;
        }
        return isValidBirthday(split, testBirthday);
    }
    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Birthday // instanceof handles nulls
                && this.value.equals(((Birthday) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

}
