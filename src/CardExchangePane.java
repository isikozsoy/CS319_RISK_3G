import javafx.scene.layout.VBox;
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

    CardExchangePane() {
        exchangeInfo = new Text("Choose one of the options below to gain soldier.");
        infantryCount = new Text("-");
        cavalryCount = new Text("-");
        cannonCount = new Text("-");
        jokerCount =  new Text("-");
        option1Button = new Button("option1");
        option2Button = new Button("option2");
        option3Button = new Button("option3");
        option4Button = new Button("option4");
        backButton = new Button("Back");
        this.getChildren().addAll(exchangeInfo, infantryCount, cavalryCount, cannonCount, jokerCount);
        this.getChildren().addAll(option1Button, option2Button, option3Button, option4Button, backButton);
    }

    public Button getBackButton() {
        return backButton;
    }

    public void updatePlayerCards(Player player) {
        infantryCount.setText("Infantry Count: " + player.getCards()[0]);
        cavalryCount.setText("Cavalry Count: " + player.getCards()[1]);
        cannonCount.setText("Cannon Count: " + player.getCards()[2]);
        jokerCount.setText("Joker Count: " + player.getCards()[3]);
    }

    public void setPlayerCards(Player player) {
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
