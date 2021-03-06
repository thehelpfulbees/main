package guitests.guihandles;

import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

//@@author liliwei25
/**
 * A handler for the {@code InfoPanel} of the UI.
 */
public class PersonInfoPanelHandle extends NodeHandle<Node> {
    public static final String INFO_PANEL_ID = "#personInfoPanel";

    private static final String NAME_FIELD_ID = "#name";
    private static final String ADDRESS_FIELD_ID = "#address";
    private static final String PHONE_FIELD_ID = "#phone";
    private static final String EMAIL_FIELD_ID = "#email";
    private static final String BIRTHDAY_FIELD_ID = "#birthday";
    private static final String REMARK_FIELD_ID = "#remark";
    private static final String TAGS_FIELD_ID = "#tags";


    private final Label nameLabel;
    private final Label addressLabel;
    private final Label phoneLabel;
    private final Label emailLabel;
    private final Label birthdayLabel;
    private final Label remarkLabel;
    private final List<Label> tagLabels;


    public PersonInfoPanelHandle(Node cardNode) {
        super(cardNode);

        this.nameLabel = getChildNode(NAME_FIELD_ID);
        this.addressLabel = getChildNode(ADDRESS_FIELD_ID);
        this.phoneLabel = getChildNode(PHONE_FIELD_ID);
        this.emailLabel = getChildNode(EMAIL_FIELD_ID);
        this.birthdayLabel = getChildNode(BIRTHDAY_FIELD_ID);
        this.remarkLabel = getChildNode(REMARK_FIELD_ID);

        Region tagsContainer = getChildNode(TAGS_FIELD_ID);
        this.tagLabels = tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
    }

    public String getName() {
        return nameLabel.getText();
    }

    public String getAddress() {
        return addressLabel.getText();
    }

    public String getPhone() {
        return phoneLabel.getText();
    }

    public String getEmail() {
        return emailLabel.getText();
    }

    public String getBirthday() {
        return birthdayLabel.getText();
    }

    public String getRemark() {
        return remarkLabel.getText();
    }

    public List<String> getTags() {
        return tagLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }
}
