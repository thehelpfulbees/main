# justintkj
###### \main\java\seedu\address\logic\commands\EmailCommand.java
``` java
/**
 * Email a person chosen by index
 */
public class EmailCommand extends Command {

    public static final String COMMAND_WORD = "email";
    public static final String COMMAND_ALIAS = "e";

    public static final String MESSAGE_SUCCESS = "Email sent to :";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Email the person identified "
            + "by the index number used in the last person listing. "
            + "An email will be sent to the chosen person.\n"
            + "Parameters: INDEX (must be a positive integer),SUBJECT,MESSAGE "
            + "[Email Index,Subject,Message]\n"
            + "Example: " + COMMAND_WORD + " 1"
            + ",subjectmessage,textmessage";
    public final Index index;
    public final String subject;
    public final String message;

    public EmailCommand(Index index, String subject, String message) {
        this.index = index;
        this.subject = subject;
        this.message = message;
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToEmail = lastShownList.get(index.getZeroBased());

        try {
            String host = "smtp.gmail.com";
            String user = "cs2103f09b3@gmail.com";
            String pass = "pocketbook";
            String to = personToEmail.getEmail().toString();
            String from = "cs2103f09b3@gmail.com";
            String subject = this.subject;
            String newLine = "";
            for (int i = 0; i < 5; i++) {
                newLine += System.getProperty("line.separator");
            }
            String messageText = this.message + newLine + "This is a generated mail provided by CS2103F09B3 Team.";
            boolean sessionDebug = false;

            Properties props = System.getProperties();

            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");

            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(messageText);

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host, user, pass);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            System.out.println("message send successfully");

        } catch (Exception ex) {
            throw new CommandException("Incorrect Email format");
        }
        return new CommandResult("Email successfully sent to : " + personToEmail.getName());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof BirthdayCommand)) {
            return false;
        }

        // state check
        EmailCommand e = (EmailCommand) other;
        return index.equals(e.index) && subject.equals(e.subject) && message.equals(e.message);
    }
}
```
###### \main\java\seedu\address\logic\commands\exceptions\CommandException.java
``` java
/**
 * Represents an error which occurs during execution of a {@link Command}.
 */
public class CommandException extends Exception {
    public CommandException(String message) {
        super(Sound.FileExist() + message);
        if(!Sound.FileExist().equals(MESSAGE_MISSING_SOUND)) {
            Sound.music();
        }
    }
}
```
###### \main\java\seedu\address\logic\commands\RemarkCommand.java
``` java
/**
 * Edits the remark of the person identified by index number in person listing.
 */

public class RemarkCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "remark";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the remark of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing remark will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_REMARK + "[REMARK]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_REMARK + "Likes to swim.";

    private final Index index;
    private final Remark remark;

    public RemarkCommand (Index index, Remark remark) {
        requireNonNull(index);
        requireNonNull(remark);

        this.index = index;
        this.remark = remark;
    }

    public RemarkCommand (Index index, String remark) {
        requireNonNull(index);
        requireNonNull(remark);

        this.index = index;
        this.remark = new Remark(remark);
    }

    public CommandResult executeUndoableCommand() throws CommandException {
        throw new CommandException(index + " " + remark);
    }

    @Override
     public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof RemarkCommand)) {
            return false;
        }

        // state check
        RemarkCommand e = (RemarkCommand) other;
        return index.equals(e.index) && remark.equals(e.remark);
    }
}
```
###### \main\java\seedu\address\logic\commands\SortCommand.java
``` java
/**
 * Sorts a the list of persons in ascending alphabetical order
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_SORT_SUCCESS = "Sorted in ascending order: ";
    public static final String MESSAGE_SORT_FAILURE = "Invalid command format!";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts the person identified by the index number used in the last person listing.\n"
            + "Parameters: name/num/address/email (the type of sort to be executed)\n"
            + "Example: " + COMMAND_WORD + " name"
            + "Example: " + COMMAND_WORD + " number"
            + "Example: " + COMMAND_WORD + " address"
            + "Example: " + COMMAND_WORD + " remark"
            + "Example: " + COMMAND_WORD + " email"
            + "Example: " + COMMAND_WORD + " birthday";


    private String sortType;

    public SortCommand(String sortType) {
        this.sortType = sortType;
    }

    @Override
    public CommandResult execute() throws CommandException {
        model.sortPerson(sortType);
        return new CommandResult(MESSAGE_SORT_SUCCESS + sortType);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SortCommand // instanceof handles nulls
                && this.sortType.equals(((SortCommand) other).sortType)); // state check
    }
}
```
###### \main\java\seedu\address\logic\parser\AddCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG,
                        PREFIX_REMARK, PREFIX_BIRTHDAY);


        try {
            ProfilePicture picture = new ProfilePicture("default");
            if (args.contains(PREFIX_NAME.toString()) || args.contains(PREFIX_ADDRESS.toString())
                || args.contains(PREFIX_EMAIL.toString()) || args.contains(PREFIX_PHONE.toString())
                || args.contains(PREFIX_REMARK.toString()) || args.contains(PREFIX_TAG.toString())) {
                if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL)) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
                }
                Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).get();
                Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).get();
                Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL)).get();
                Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).get();
                Remark remark = ParserUtil.parseRemark(argMultimap.getValue(PREFIX_REMARK)).get();
                Birthday birthday = ParserUtil.parseBirthday(argMultimap.getValue(PREFIX_BIRTHDAY)).get();
                Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
                ReadOnlyPerson person = new Person(name, phone, email, address, remark, birthday, tagList, picture);
                return new AddCommand(person);
            } else {
                Remark remark = new Remark("");
                Birthday birthday = new Birthday("");
                String[] allArgs = args.split(",");
                if (allArgs.length < 2) {
                    throw new IllegalValueException("invalid add format");
                }
                Name name = new Name(allArgs[0]);
                Email email;
                Phone phone;
                String blocknum;
                String streetnum;
                String unitnum;
                String postalnum = "";

                Pattern emailpattern = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
                Matcher matcher = emailpattern.matcher(args);
                boolean matchFound = matcher.find();
                if (matchFound) {
                    email = new Email(matcher.group(0));
                } else {
                    throw new IllegalValueException("invalid email\n Example: Jason@example.com");
                }
                Pattern block = Pattern.compile("block \\d{1,3}", Pattern.CASE_INSENSITIVE);
                matcher = block.matcher(args);
                Pattern blk = Pattern.compile("blk \\d{1,3}", Pattern.CASE_INSENSITIVE);
                Matcher blkmatcher = blk.matcher(args);
                matchFound = matcher.find();
                if (!matchFound) {
                    matchFound = blkmatcher.find();
                    matcher = blkmatcher;
                }
                if (matchFound) {
                    blocknum = matcher.group(0);
                } else {
                    throw new IllegalValueException("invalid address, Block Number. \nExample: Block 123");
                }
                Pattern street = Pattern.compile("[a-zA-z]+ street \\d{1,2}", Pattern.CASE_INSENSITIVE);
                matcher = street.matcher(args);
                matchFound = matcher.find();
                if (matchFound) {
                    streetnum = matcher.group(0);
                } else {
                    throw new IllegalValueException("invalid address, Street. \nExample: Jurong Street 11");
                }
                Pattern unit = Pattern.compile("#\\d\\d-\\d{1,3}[a-zA-Z]{0,1}", Pattern.CASE_INSENSITIVE);
                matcher = unit.matcher(args);
                matchFound = matcher.find();
                if (matchFound) {
                    unitnum = matcher.group(0);
                } else {
                    throw new IllegalValueException("invalid address, Unit. \n Example: #01-12B");
                }
                Pattern postal = Pattern.compile("singapore \\d{6,6}", Pattern.CASE_INSENSITIVE);
                matcher = postal.matcher(args);
                matchFound = matcher.find();
                if (matchFound) {
                    postalnum = ", " + matcher.group(0);
                }
                Pattern phonepattern = Pattern.compile("\\ {0,1}\\d{8}\\ {0,1}");
                matcher = phonepattern.matcher(args);
                matchFound = matcher.find();
                if (!matchFound) {
                    phonepattern = Pattern.compile("\\,{0,1}\\d{8}\\,{0,1}");
                    matcher = phonepattern.matcher(args);
                    matchFound = matcher.find();
                }
                if (matchFound) {
                    phone = new Phone(matcher.group(0).trim().replace(",",""));
                } else {
                    throw new IllegalValueException("invalid phone number,\n Example: 12345678");
                }
                Pattern birthpattern = Pattern.compile("\\d{1,2}-\\d{1,2}-\\d{4,4}", Pattern.CASE_INSENSITIVE);
                matcher = birthpattern.matcher(args);
                matchFound = matcher.find();
                if (matchFound) {
                    if(Birthday.isValidBirthday(matcher.group(0))) {
                        birthday = new Birthday(matcher.group(0));
                    } else {
                        throw new IllegalValueException("invalid birthday,\n Example: 12-09-1994");
                    }
                }

                Address address = new Address(blocknum + ", " + streetnum + ", " + unitnum + postalnum);
                Set<Tag> tagList = new HashSet<>();
                ReadOnlyPerson person = new Person(name, phone, email, address, remark, birthday, tagList, picture);
                return new AddCommand(person);
            }
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }
```
###### \main\java\seedu\address\logic\parser\AddressBookParser.java
``` java
    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string, adds suggestion to Autocomplete.xml if valid command
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case EmailCommand.COMMAND_WORD:
            Command emailCommand = new EmailCommandParser().parse(arguments);
            try {
                CommandBox.setAddSuggestion(userInput);
            } catch (Exception ex) {
                throw new ParseException(MISSING_AUTOCOMPLETEFILE);
            }
            return emailCommand;

        case SortCommand.COMMAND_WORD:
            return new SortCommandParser().parse(arguments);

        case AddCommand.COMMAND_WORD:
        case AddCommand.COMMAND_ALIAS:
            Command addCommand = new AddCommandParser().parse(arguments);
            try {
                CommandBox.setAddSuggestion(userInput);
            } catch (Exception ex) {
                throw new ParseException(MISSING_AUTOCOMPLETEFILE);
            }
            return addCommand;

        case EditCommand.COMMAND_WORD:
        case EditCommand.COMMAND_ALIAS:
            Command editCommand = new EditCommandParser().parse(arguments);
            try {
                CommandBox.setAddSuggestion(userInput);
            } catch (Exception ex) {
                throw new ParseException(MISSING_AUTOCOMPLETEFILE);
            }
            return editCommand;

        case SelectCommand.COMMAND_WORD:
        case SelectCommand.COMMAND_ALIAS:
            Command selectCommand = new SelectCommandParser().parse(arguments);
            try {
                CommandBox.setAddSuggestion(userInput);
            } catch (Exception ex) {
                throw new ParseException(MISSING_AUTOCOMPLETEFILE);
            }
            return selectCommand;

        case DeleteCommand.COMMAND_WORD:
        case DeleteCommand.COMMAND_ALIAS:
            Command deleteCommand = new DeleteCommandParser().parse(arguments);
            try {
                CommandBox.setAddSuggestion(userInput);
            } catch (Exception ex) {
                throw new ParseException(MISSING_AUTOCOMPLETEFILE);
            }
            return deleteCommand;

        case RemoveTagCommand.COMMAND_WORD:
            Command removeTagCommand = new RemoveTagCommandParser().parse(arguments);
            try {
                CommandBox.setAddSuggestion(userInput);
            } catch (Exception ex) {
                throw new ParseException(MISSING_AUTOCOMPLETEFILE);
            }
            return removeTagCommand;

        case ClearCommand.COMMAND_WORD:
        case ClearCommand.COMMAND_ALIAS:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
        case FindCommand.COMMAND_ALIAS:
            Command findCommand = new FindCommandParser().parse(arguments);
            try {
                CommandBox.setAddSuggestion(userInput);
            } catch (Exception ex) {
                throw new ParseException(MISSING_AUTOCOMPLETEFILE);
            }
            return findCommand;

        case FuzzyfindCommand.COMMAND_WORD:
        case FuzzyfindCommand.COMMAND_ALIAS:
            return new FuzzyfindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
        case ListCommand.COMMAND_ALIAS:
            return new ListCommand();

        case HistoryCommand.COMMAND_WORD:
        case HistoryCommand.COMMAND_ALIAS:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case UndoCommand.COMMAND_WORD:
        case UndoCommand.COMMAND_ALIAS:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
        case RedoCommand.COMMAND_ALIAS:
            return new RedoCommand();

        case RemarkCommand.COMMAND_WORD:
            Command remarkCommand = new RemarkCommandParser().parse(arguments);
            try {
                CommandBox.setAddSuggestion(userInput);
            } catch (Exception ex) {
                throw new ParseException(MISSING_AUTOCOMPLETEFILE);
            }
            return remarkCommand;

        case BirthdayCommand.COMMAND_WORD:
            Command birthdayCommand = new BirthdayCommandParser().parse(arguments);
            try {
                CommandBox.setAddSuggestion(userInput);
            } catch (Exception ex) {
                throw new ParseException(MISSING_AUTOCOMPLETEFILE);
            }
            return birthdayCommand;

        case MapCommand.COMMAND_WORD:
            Command mapCommand = new MapCommandParser().parse(arguments);
            try {
                CommandBox.setAddSuggestion(userInput);
            } catch (Exception ex) {
                throw new ParseException(MISSING_AUTOCOMPLETEFILE);
            }
            return mapCommand;

        case ImageCommand.COMMAND_WORD:
            return new ImageCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
```
###### \main\java\seedu\address\logic\parser\EmailCommandParser.java
``` java
/**
 * Parses input arguments and creates a new EmailCommand object
 */
public class EmailCommandParser implements Parser<EmailCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the EmailCommand
     * and returns a Email object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EmailCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String[] splitArgs = args.trim().split(" ");

        Index index;
        String subject;
        String message;
        try {
            index = ParserUtil.parseIndex(splitArgs[0]);
            String[] messages = args.trim().split(",");
            if (messages.length < 3) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
            }
            subject = (messages[1]);
            message = (messages[2]);
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
        }
        return new EmailCommand(index, subject, message);
    }
}
```
###### \main\java\seedu\address\logic\parser\exceptions\ParseException.java
``` java
/**
 * Represents a parse error encountered by a parser.
 */
public class ParseException extends IllegalValueException {

    public ParseException(String message) {
        super(Sound.FileExist() + message);
        if(!Sound.FileExist().equals(MESSAGE_MISSING_SOUND)) {
            Sound.music();
        }
    }

    public ParseException(String message, Throwable cause) {
        super(Sound.FileExist() + message, cause);
        if(!Sound.FileExist().equals(MESSAGE_MISSING_SOUND)) {
            Sound.music();
        }
    }
}
```
###### \main\java\seedu\address\logic\parser\RemarkCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AddCommand object
 */
public class RemarkCommandParser implements Parser<RemarkCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the RemarkCommand
     * and returns a RemarkCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_REMARK);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
        }
        Remark remark = new Remark(argMultimap.getValue(PREFIX_REMARK).orElse(""));
        return new RemarkCommand(index, remark);
    }
}
```
###### \main\java\seedu\address\logic\parser\SortCommandParser.java
``` java
/**
 * Parses input arguments and creates a new SortCommand object
 */
public class SortCommandParser implements Parser<SortCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand
     * and returns an SortCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortCommand parse(String args) throws ParseException {
        try {
            String toSort = ParserUtil.parseSortType(args);
            return new SortCommand(toSort);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \main\java\seedu\address\model\AddressBook.java
``` java
    //// list overwrite operations
    public void sortPersons(String sortType) {
        persons.sort(sortType);
    }
```
###### \main\java\seedu\address\model\Model.java
``` java
    /**Sorts all the people in the current database*/
    void sortPerson(String sortType);
```
###### \main\java\seedu\address\model\person\UniquePersonList.java
``` java
    /**
     * Sorts the internalList as declared by the arguments
     */
    public void sort(String sortType) {
        if (sortType.equals(SORTNAME_ARG)) {
            Collections.sort(internalList, (Person p1, Person p2) ->
                p1.getName().toString().compareTo(p2.getName().toString()));
        } else if (sortType.equals(SORTNUM_ARG)) {
            Collections.sort(internalList, (Person p1, Person p2) ->
                p1.getPhone().toString().compareTo(p2.getPhone().toString()));
        } else if (sortType.equals(SORTADD_ARG)) {
            Collections.sort(internalList, (Person p1, Person p2) ->
                p1.getAddress().toString().compareTo(p2.getAddress().toString()));
        } else if (sortType.equals(SORTEMAIL_ARG)) {
            Collections.sort(internalList, (Person p1, Person p2) ->
                p1.getEmail().toString().compareTo(p2.getEmail().toString()));
        } else if (sortType.equals(SORTREMARK_ARG)) {
            Collections.sort(internalList, (Person p1, Person p2) ->
                p1.getRemark().toString().compareTo(p2.getRemark().toString()));
        } else if (sortType.equals(SORTBIRTHDAY_ARG)) {
            Collections.sort(internalList, (Person p1, Person p2) ->
                p1.getBirthday().toString().compareTo(p2.getBirthday().toString()));
        }
    }
```
###### \main\java\seedu\address\ui\CommandBox.java
``` java
/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";
    public static final String AUTOCOMPLETE_FILE_NAME = "Autocomplete.xml";
    private static String[] possibleSuggestion = {"add", "clear", "list",
        "edit", "find", "delete", "select", "history", "undo", "redo", "exit", "sort", "sort name",
        "sort num", "sort email", "sort address", "sort remark", "exit"};
    private static ArrayList<String> mainPossibleSuggestion = new ArrayList<String>(Arrays.asList(possibleSuggestion));
    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private final Logic logic;
    private ListElementPointer historySnapshot;

    @FXML
    private TextField commandTextField;
    private ArrayList<String> prevText = new ArrayList<String>();
    private AutoCompletionBinding autocompletionbinding;
    public CommandBox(Logic logic) {
        super(FXML);
        this.logic = logic;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
        historySnapshot = logic.getHistorySnapshot();
        try {
            XMLDecoder e = new XMLDecoder(new FileInputStream("Autocomplete.xml"));
            mainPossibleSuggestion = ((ArrayList<String>) e.readObject());
            e.close();
        } catch (Exception ex) {
            try {
                File file = new File("Autocomplete.xml");
                file.createNewFile();
            } catch (IOException ioe) {
                raise(new DataSavingExceptionEvent(ex));
            }
        }
        autocompletionbinding = TextFields.bindAutoCompletion(commandTextField, mainPossibleSuggestion);
    }
```
###### \main\java\seedu\address\ui\CommandBox.java
``` java

    /**
     * Adds in a valid command string into autocomplete.xml storage
     * @param commandWord
     * @throws CommandException if autocomplete.xml cannot be made.
     */
    public static void setAddSuggestion(String commandWord) throws CommandException {
        if (!mainPossibleSuggestion.contains(commandWord)) {
            try {
                mainPossibleSuggestion.add(commandWord);
                XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(AUTOCOMPLETE_FILE_NAME)));
                e.writeObject(mainPossibleSuggestion);
                e.close();
            } catch (Exception ex) {
                try {
                    File file = new File("Autocomplete.xml");
                    file.createNewFile();
                } catch (IOException ioe) {
                    throw new CommandException("Unable to create file Autocomplete.xml");
                }
            }
        }
    }
```
###### \test\java\seedu\address\logic\commands\RemarkCommandTest.java
``` java
public class RemarkCommandTest {

    @Test
    public void equals() {
        final RemarkCommand standardCommand = new RemarkCommand(INDEX_FIRST_PERSON, VALID_REMARK_AMY);
        // same values -> returns true
        RemarkCommand commandWithSameValues = new RemarkCommand(INDEX_FIRST_PERSON, VALID_REMARK_AMY);
        assertTrue(standardCommand.equals(commandWithSameValues));
        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));
        // null -> returns false
        assertFalse(standardCommand.equals(null));
        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));
        // different index -> returns false
        assertFalse(standardCommand.equals(new RemarkCommand(INDEX_SECOND_PERSON, VALID_REMARK_AMY)));
        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new RemarkCommand(INDEX_FIRST_PERSON, VALID_REMARK_BOB)));
    }
}
```
###### \test\java\seedu\address\logic\commands\SortCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code SortCommand}.
 */
public class SortCommandTest {

    public static final String SORT_NAME_ARG = "name";
    public static final String SORT_EMAIL_ARG = "email";
    public static final String SORT_ADDRESS_ARG = "address";
    public static final String SORT_NUM_ARG = "number";
    public static final String SORT_NUM_ARG_CAMELCASE = "NuMBeR";
    private Model model;
    private Model expectedModel;
    private SortCommand sortCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }
    @Test
    public void execute_showsSameList() {
        //Sort name -> command parsed successful
        sortCommand = new SortCommand(SORT_NAME_ARG);
        sortCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        assertCommandSuccess(sortCommand, model, SortCommand.MESSAGE_SORT_SUCCESS + SORT_NAME_ARG, expectedModel);

        //Sort email -> command parsed successful
        expectedModel = new ModelManager(getSortedEmailAddressBook(), new UserPrefs());
        sortCommand = new SortCommand(SORT_EMAIL_ARG);
        sortCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        assertCommandSuccess(sortCommand, model, SortCommand.MESSAGE_SORT_SUCCESS + SORT_EMAIL_ARG, expectedModel);

        //Sort address -> command parsed successful
        expectedModel = new ModelManager(getSortedAddressAddressBook(), new UserPrefs());
        sortCommand = new SortCommand(SORT_ADDRESS_ARG);
        sortCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        assertCommandSuccess(sortCommand, model, SortCommand.MESSAGE_SORT_SUCCESS + SORT_ADDRESS_ARG, expectedModel);

        //Sort number -> command parsed successful
        expectedModel = new ModelManager(getSortedNumAddressBook(), new UserPrefs());
        sortCommand = new SortCommand(SORT_NUM_ARG);
        sortCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        assertCommandSuccess(sortCommand, model, SortCommand.MESSAGE_SORT_SUCCESS + SORT_NUM_ARG, expectedModel);

        //Sort number CamelCase -> command parsed successful
        expectedModel = new ModelManager(getSortedNumAddressBook(), new UserPrefs());
        sortCommand = new SortCommand(SORT_NUM_ARG);
        sortCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        assertCommandSuccess(sortCommand, model, SortCommand.MESSAGE_SORT_SUCCESS + SORT_NUM_ARG, expectedModel);

    }
}

```
###### \test\java\seedu\address\logic\parser\RemarkCommandParserTest.java
``` java
public class RemarkCommandParserTest {
    private RemarkCommandParser parser = new RemarkCommandParser();

    @Test
    public void parse_indexSpecified_failure() throws Exception {
        final String remark = "Some remark";

        //Have remarks
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_REMARK + " " + remark;
        RemarkCommand expectedCommand = new RemarkCommand(INDEX_FIRST_PERSON, remark);
        assertParseSuccess(parser, userInput, expectedCommand);

        //No remarks
        userInput = targetIndex.getOneBased() + " " + PREFIX_REMARK;
        expectedCommand = new RemarkCommand(INDEX_FIRST_PERSON, "");
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_noFieldSpecified_failure() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);

        // nothing at all
        assertParseFailure(parser, RemarkCommand.COMMAND_WORD, expectedMessage);
    }

}
```
###### \test\java\seedu\address\logic\parser\SortCommandParserTest.java
``` java
//hah
public class SortCommandParserTest {
    private SortCommandParser parser = new SortCommandParser();

    //Tests for valid argument, sort number
    @Test
    public void parse_Number_returnsSortCommand() {
        try {
            SortCommand newCommand = parser.parse("number");
            if (newCommand.equals(new SortCommand("number"))) {
                assert true;
            } else {
                assert false;
            }
        } catch (ParseException pe) {
            assert false;
        }
    }
    //Tests for valid argument, sort name
    @Test
    public void parse_Name_returnsSortCommand() {
        try {
            SortCommand newCommand = parser.parse("name");
            if (newCommand.equals(new SortCommand("name"))) {
                assert true;
            } else {
                assert false;
            }
        } catch (ParseException pe) {
            assert false;
        }
    }
    //Tests for valid argument, sort address
    @Test
    public void parse_Address_returnsSortCommand() {
        try {
            SortCommand newCommand = parser.parse("address");
            if (newCommand.equals(new SortCommand("address"))) {
                assert true;
            } else {
                assert false;
            }
        } catch (ParseException pe) {
            assert false;
        }
    }
    //Tests for valid argument, sort email
    @Test
    public void parse_Email_returnsSortCommand() {
        try {
            SortCommand newCommand = parser.parse("email");
            if (newCommand.equals(new SortCommand("email"))) {
                assert true;
            } else {
                assert false;
            }
        } catch (ParseException pe) {
            assert false;
        }
    }
    //Tests for valid argument, sort birthday
    @Test
    public void parse_Birthday_returnsSortCommand() {
        try {
            SortCommand newCommand = parser.parse("birthday");
            if (newCommand.equals(new SortCommand("birthday"))) {
                assert true;
            } else {
                assert false;
            }
        } catch (ParseException pe) {
            assert false;
        }
    }
    //Tests for valid argument, sort remark
    @Test
    public void parse_Remark_returnsSortCommand() {
        try {
            SortCommand newCommand = parser.parse("remark");
            if (newCommand.equals(new SortCommand("remark"))) {
                assert true;
            } else {
                assert false;
            }
        } catch (ParseException pe) {
            assert false;
        }
    }
    //Tests for valid argument, sort number with CamelCase
    @Test
    public void parse_CamelCase_returnsSortCommand() {
        try {
            SortCommand newCommand = parser.parse("NuMbEr");
            if (newCommand.equals(new SortCommand("number"))) {
                assert true;
            } else {
                assert false;
            }
        } catch (ParseException pe) {
            assert false;
        }
    }

    //Tests for invalid Argument
    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    //Tests for invalid Argument , Empty
    @Test
    public void parse_emptyInvalidArgs_throwsParseException() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    //Tests for invalid Argument , multiple valid argument
    @Test
    public void parse_multipleInvalidArgs_throwsParseException() {
        assertParseFailure(parser, "number name", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }
}
```
