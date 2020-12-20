import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Button;

public class AlliancePane extends VBox{
    private Button sendAllianceRequest;
    private Button cancelAlliance;
    private Button backButton;

    public AlliancePane()
    {
        String styleBackground = "-fx-background-color: #ff6666;" +
                                 "-fx-border-color: #00ccff";

        ImageView backImg = new ImageView(new Image("icons/back_arrow_icon.png"));
        ImageView sendImg = new ImageView(new Image("icons/send_icon.png"));
        ImageView cancelImg = new ImageView(new Image("icons/cancel_icon.png"));

        backImg.setFitHeight(50);
        backImg.setFitWidth(50);

        sendImg.setFitHeight(50);
        sendImg.setFitWidth(50);

        cancelImg.setFitHeight(50);
        cancelImg.setFitWidth(50);

        backButton = new Button("Back");
        sendAllianceRequest = new Button("Send Alliance Request");
        cancelAlliance = new Button("Cancel Alliance");
        //sendAllianceRequest.setStyle("-fx-background-color:" + "color=red" + ";" +
           // "-fx-text-fill:white;");
        backButton.setGraphic(backImg);
        sendAllianceRequest.setGraphic(sendImg);
        cancelAlliance.setGraphic(cancelImg);

        sendAllianceRequest.setPrefSize(400, 75);
        backButton.setPrefSize(400, 75);
        cancelAlliance.setPrefSize(400, 75);

        sendAllianceRequest.setFont(Font.font("Snap ITC", 30));
        backButton.setFont(Font.font("Snap ITC", 30));
        cancelAlliance.setFont(Font.font("Snap ITC", 30));

        sendAllianceRequest.setStyle(styleBackground);
        backButton.setStyle(styleBackground);
        cancelAlliance.setStyle(styleBackground);

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