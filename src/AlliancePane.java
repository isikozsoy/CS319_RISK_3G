import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

public class AlliancePane extends VBox{
    private Button sendAllianceRequest;
    private Button cancelAlliance;
    private Button backButton;

    public AlliancePane()
    {
        backButton = new Button("Back");
        sendAllianceRequest = new Button("Send Alliance Request");
        cancelAlliance = new Button("Cancel Alliance");

        this.getChildren().add(backButton);
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getSendAllianceRequestButton() {
        return sendAllianceRequest;
    }

    public Button getCancelAllianceButton() {
        return cancelAlliance;
    }

    public void addSendAllianceButton()
    {
        this.getChildren().addAll(sendAllianceRequest);
    }

    public void addCancelAllianceButton()
    {
        this.getChildren().addAll(cancelAlliance);
    }

    public void removeSendAllianceButton()
    {
        this.getChildren().removeAll(sendAllianceRequest);
    }

    public void removeCancelAllianceButton()
    {
        this.getChildren().removeAll(cancelAlliance);
    }
}