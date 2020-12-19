import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

import java.awt.*;

public class CardExchangePane extends VBox {
    private Text exchangeInfo;
    private Text infantryCount;
    private Text cavalryCount;
    private Text cannonCount;
    private Text jokerCount;

    private Button option1Button;
    private Button option2Button;
    private Button option3Button;
    private Button option4Button;
    private Button backButton;

    private Player curPlayer;

    CardExchangePane() {
        String styleBackground = "-fx-background-color: #ff6666;" +
                "-fx-border-color: #00ccff";

        exchangeInfo = new Text("Choose one of the options below to gain soldier.");
        infantryCount = new Text("-");
        cavalryCount = new Text("-");
        cannonCount = new Text("-");
        jokerCount =  new Text("-");

        option1Button = new Button();
        option2Button = new Button();
        option3Button = new Button();
        option4Button = new Button();
        backButton = new Button("Back");

        option1Button.setPrefSize(400, 75);
        option2Button.setPrefSize(400, 75);
        option3Button.setPrefSize(400, 75);
        option4Button.setPrefSize(400, 75);
        backButton.setPrefSize(400, 75);

        option1Button.setFont(javafx.scene.text.Font.font("Snap ITC", 30));
        option2Button.setFont(javafx.scene.text.Font.font("Snap ITC", 30));
        option3Button.setFont(Font.font("Snap ITC", 30));
        option4Button.setFont(Font.font("Snap ITC", 30));
        backButton.setFont(Font.font("Snap ITC", 30));

        option1Button.setStyle(styleBackground);
        option2Button.setStyle(styleBackground);
        option3Button.setStyle(styleBackground);
        option4Button.setStyle(styleBackground);
        backButton.setStyle(styleBackground);

        ImageView backImg = new ImageView(new Image("icons/back_arrow_icon.png"));
        backImg.setFitHeight(50);
        backImg.setFitWidth(50);
        backButton.setGraphic(backImg);

        ImageView infantryImg = new ImageView(new Image("icons/infantry_icon.png"));
        infantryImg.setFitHeight(50);
        infantryImg.setFitWidth(125);
        option1Button.setGraphic(infantryImg);

        ImageView cavalryImg = new ImageView(new Image("icons/cavalry_icon.png"));
        cavalryImg.setFitHeight(50);
        cavalryImg.setFitWidth(125);
        option2Button.setGraphic(cavalryImg);

        ImageView cannonImg = new ImageView(new Image("icons/cannon_icon.png"));
        cannonImg.setFitHeight(40);
        cannonImg.setFitWidth(125);
        option3Button.setGraphic(cannonImg);

        ImageView jokerImg = new ImageView(new Image("icons/combine_icon.png"));
        jokerImg.setFitHeight(50);
        jokerImg.setFitWidth(125);
        option4Button.setGraphic(jokerImg);

        this.getChildren().addAll(exchangeInfo, infantryCount, cavalryCount, cannonCount, jokerCount);
        this.getChildren().addAll(option1Button, option2Button, option3Button, option4Button, backButton);
    }

    public Button getBackButton() {
        return backButton;
    }

    public Player getCurPlayer() {
        return curPlayer;
    }

    public void updatePlayerCards(Player player) {
        infantryCount.setText("Infantry Count: " + player.getCards()[0]);
        cavalryCount.setText("Cavalry Count: " + player.getCards()[1]);
        cannonCount.setText("Cannon Count: " + player.getCards()[2]);
        jokerCount.setText("Joker Count: " + player.getCards()[3]);
    }

    public void setPlayerCards(Player player) {
        curPlayer = player;
        exchangeInfo.setText("Choose one of the options below to gain soldier.");
        infantryCount.setText("Infantry Count: " + player.getCards()[0]);
        cavalryCount.setText("Cavalry Count: " + player.getCards()[1]);
        cannonCount.setText("Cannon Count: " + player.getCards()[2]);
        jokerCount.setText("Joker Count: " + player.getCards()[3]);
        option1Button.setOnMouseClicked(e -> {
            if(player.exchangeInfantry()) {
                exchangeInfo.setText("Successful exchange.");
                updatePlayerCards(player);
            }
            else exchangeInfo.setText("You do not have enough number of troops.");
        });
        option2Button.setOnMouseClicked(e -> {
            if(player.exchangeCavalry()) {
                updatePlayerCards(player);
                exchangeInfo.setText("Successful exchange.");
            }
            else exchangeInfo.setText("You do not have enough number of troops.");
        });
        option3Button.setOnMouseClicked(e -> {
            if(player.exchangeCannon()) {
                updatePlayerCards(player);
                exchangeInfo.setText("Successful exchange.");
            }
            else exchangeInfo.setText("You do not have enough number of troops.");
        });
        option4Button.setOnMouseClicked(e -> {
            if(player.exchangeMixed()) {
                updatePlayerCards(player);
                exchangeInfo.setText("Successful exchange.");
            }
            else exchangeInfo.setText("You do not have enough number of troops.");
        });
    }
}
