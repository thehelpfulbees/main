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
public class Birthday implements Comparable {

    public static final String MESSAGE_BIRTHDAY_CONSTRAINTS =
            "Birthdays can only contain numbers, and should be in the format dd-mm-yyyy";
    public static final String MESSAGE_WRONG_DATE = "Date entered is wrong";
    public static final String MESSAGE_LATE_DATE = "Date given should be before today %1$s";
    private static final String DASH = "-";
    private static final int DEFAULT_VALUE = 0;
    private static final String NOT_SET = "Not Set";
    private static final String EMPTY = "";
    private static final String REMOVE = "remove";
    private static final int MIN_MONTHS = 1;
    private static final int MAX_MONTHS = 12;
    private static final int MIN_DAYS = 1;
    private static final int DAY_POS = 0;
    private static final int MONTH_POS = 1;
    private static final String DATE_FORMAT = "dd-MM-yyyy";

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

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
     * Ensures that the date entered is before current date
     *
     * @param birthday {@code LocalDate} containing input by user
     * @return True when birthday entered by user is before or on current date
     */
    public static boolean isDateCorrect(LocalDate birthday) {
        return birthday.isBefore(LocalDate.now()) || birthday.equals(LocalDate.now());
    }

    /**
     * Returns true if a given string is a valid person birthday. Requires both the input string and the parsed date
     * since the parsed date will be resolved to the correct values by {@code DateTimeFormatter}
     *
     * @param test Input birthday String by user
     * @param testBirthday Parsed birthday from input by user (will be resolved to closest correct date)
     * @return True when date entered by user is valid
     */
    public static boolean isValidBirthday(String[] test, LocalDate testBirthday) {
        int day = Integer.parseInt(test[DAY_POS]);
        int month = Integer.parseInt(test[MONTH_POS]);

        return month >= MIN_MONTHS
                && month <= MAX_MONTHS
                && day >= MIN_DAYS
                && day <= testBirthday.lengthOfMonth();
    }

    /**
     * Calls {@code isValidBirthday} with parsed values when input is only one String
     *
     * @param test Single String input
     * @return True when {@code isValidBirthday} verifies date entered by user
     */
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

    @Override
    public int compareTo(Object o) {
        Birthday comparedBirthday = (Birthday) o;
        return this.value.compareTo(comparedBirthday.toString());
    }
}
