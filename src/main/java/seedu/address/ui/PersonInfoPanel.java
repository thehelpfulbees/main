package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.ReadOnlyPerson;

public class PersonInfoPanel extends UiPart<Region> {

    private static final String FXML = "PersonInfoPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonInfoPanel.class);

    @FXML
    private ImageView profileImage;
    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label birthday;
    @FXML
    private Label remark;
    @FXML
    private FlowPane tags;

    public PersonInfoPanel(ReadOnlyPerson person) {
        super(FXML);
        setConnections(person);
        registerAsAnEventHandler(this);
    }

    public PersonInfoPanel() {
        super(FXML);
        setDefaultConnections();
        registerAsAnEventHandler(this);
    }

    public void updateConnections(ReadOnlyPerson person) {
        setConnections(person);
    }

    private void setDefaultConnections() {
        name.setText("");
        phone.setText("");
        address.setText("");
        email.setText("");
        birthday.setText("");
        remark.setText("");
        tags.setAccessibleText("");
        profileImage.setImage(new Image("profiles/default.png"));
    }

    private void setConnections(ReadOnlyPerson person) {
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        birthday.setText(person.getBirthday().value);
        remark.setText(person.getRemark().value);
        tags.getChildren().clear();
        person.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        try {
            profileImage.setImage(new Image(person.getPicture().getLocation()));
        } catch (IllegalArgumentException iae) {
            profileImage.setImage(new Image("profiles/default.png"));
        }
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        setConnections(event.getNewSelection().person);
    }
}
