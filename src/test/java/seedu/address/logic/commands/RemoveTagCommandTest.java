package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.logic.commands.CommandTestUtil.BIRTHDAY_ALICE;
import static seedu.address.logic.commands.CommandTestUtil.BIRTHDAY_AMY;
import static seedu.address.logic.commands.CommandTestUtil.BIRTHDAY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showFirstPersonOnly;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.function.Predicate;

import com.sun.org.apache.regexp.internal.RE;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Birthday;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

public class RemoveTagCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() throws Exception {
        RemoveTagCommand removeTagCommand = prepareCommand(new Tag(VALID_TAG_FRIEND));

        assertCommandFailure(removeTagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() throws Exception {
        showFirstPersonOnly(model);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        RemoveTagCommand removeTagCommand = prepareCommand(new Tag(VALID_TAG_FRIEND));

        assertCommandFailure(removeTagCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicatePerson_failure() throws Exception {
        RemoveTagCommand removeTagCommand = prepareCommandForDuplicateException(new Tag(VALID_TAG_FRIEND));

        thrown.expect(CommandException.class);
        thrown.expectMessage(RemoveTagCommand.MESSAGE_DUPLICATE_PERSON);

        removeTagCommand.execute();
    }

    @Test
    public void execute_missingPerson_failure() throws Exception {
        RemoveTagCommand removeTagCommand = prepareCommandForNotFoundException(new Tag(VALID_TAG_FRIEND));

        thrown.expect(AssertionError.class);
        thrown.expectMessage(RemoveTagCommand.MESSAGE_MISSING_PERSON);

        removeTagCommand.execute();
    }

    @Test
    public void equals() throws Exception {
        final RemoveTagCommand standardCommand = new RemoveTagCommand(new Tag(VALID_TAG_FRIEND));

        // same values -> returns true
        RemoveTagCommand commandWithSameValues = new RemoveTagCommand(new Tag(VALID_TAG_FRIEND));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new RemoveTagCommand(new Tag(VALID_TAG_HUSBAND))));
    }

    /**
     * Returns an {@code BirthdayCommand} with parameters {@code index} and {@code birthday}
     */
    private RemoveTagCommand prepareCommand(Tag target) {
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(target);
        removeTagCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return removeTagCommand;
    }

    /**
     * Returns an {@code BirthdayCommand} with parameters {@code index} and {@code birthday}
     * to test {@code DuplicatePersonException}
     */
    private RemoveTagCommand prepareCommandForDuplicateException(Tag target) {
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(target);
        removeTagCommand.setData(new ModelStubThrowingDuplicatePersonException(), new CommandHistory(),
                new UndoRedoStack());
        return removeTagCommand;
    }

    /**
     * Returns an {@code BirthdayCommand} with parameters {@code index} and {@code birthday}
     * to test {@code PersonNotFoundException}
     */
    private RemoveTagCommand prepareCommandForNotFoundException(Tag target) {
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(target);
        removeTagCommand.setData(new ModelStubThrowingPersonNotFoundException(), new CommandHistory(),
                new UndoRedoStack());
        return removeTagCommand;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(ReadOnlyPerson person) throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public void updateListToShowAll() {
            fail("Thi method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deletePerson(ReadOnlyPerson target) throws PersonNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void sortPerson(String target) {
            fail("This method should not be called.");
        }

        @Override
        public void updatePerson(ReadOnlyPerson target, ReadOnlyPerson editedPerson)
                throws DuplicatePersonException , PersonNotFoundException{
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<ReadOnlyPerson> getFilteredPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<ReadOnlyPerson> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void removeTag(Tag tag) {
            fail("This method should not be called.");
        }

        @Override
        public void mapPerson(ReadOnlyPerson target) {
            fail("This method should not be called.");
        }

        @Override
        public void changeImage(ReadOnlyPerson target) {
            fail("This method should not be called.");
        }
    }

    /**
     * A Model stub that always throw a DuplicatePersonException when trying to edit birthday.
     */
    private class ModelStubThrowingDuplicatePersonException extends RemoveTagCommandTest.ModelStub {
        @Override
        public ObservableList<ReadOnlyPerson> getFilteredPersonList() {
            return model.getFilteredPersonList();
        }

        @Override
        public void updatePerson(ReadOnlyPerson target, ReadOnlyPerson editedPerson)
                throws DuplicatePersonException {
            throw new DuplicatePersonException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that always throw a DuplicatePersonException when trying to edit birthday.
     */
    private class ModelStubThrowingPersonNotFoundException extends RemoveTagCommandTest.ModelStub {
        @Override
        public ObservableList<ReadOnlyPerson> getFilteredPersonList() {
            return model.getFilteredPersonList();
        }

        @Override
        public void updatePerson(ReadOnlyPerson target, ReadOnlyPerson editedPerson)
                throws PersonNotFoundException {
            throw new PersonNotFoundException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }
}
