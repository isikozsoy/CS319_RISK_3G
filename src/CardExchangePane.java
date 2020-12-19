import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

import java.awt.*;

public class CardExchangePane extends VBox {
    private javafx.scene.control.Label exchangeInfo;
    private javafx.scene.control.Label infantryCount;
    private javafx.scene.control.Label cavalryCount;
    private javafx.scene.control.Label cannonCount;
    private javafx.scene.control.Label jokerCount;

    private Button option1Button;
    private Button option2Button;
    private Button option3Button;
    private Button option4Button;
    private Button backButton;

    private Player curPlayer;

    CardExchangePane() {
        //Style of the buttons
        String styleBackground = "-fx-background-color: #ff6666;" +
                "-fx-border-color: #00ccff";

        //panes for texts and buttons
        VBox texts = new VBox();
        VBox buttons = new VBox();

        //texts
        exchangeInfo = new Label("Choose one of the options below to gain soldiers:");
        infantryCount = new Label("-");
        cavalryCount = new Label("-");
        cannonCount = new Label("-");
        jokerCount =  new Label("-");

        exchangeInfo.setFont(javafx.scene.text.Font.font("Snap ITC", 15));
        infantryCount.setFont(javafx.scene.text.Font.font("Snap ITC", 30));
        cavalryCount.setFont(javafx.scene.text.Font.font("Snap ITC", 30));
        cannonCount.setFont(javafx.scene.text.Font.font("Snap ITC", 30));
        jokerCount.setFont(javafx.scene.text.Font.font("Snap ITC", 30));

        ImageView infantrySingle = new ImageView(new Image("icons/infantry_single_icon.png"));
        infantrySingle.setFitHeight(50);
        infantrySingle.setFitWidth(50);
        infantryCount.setGraphic(infantrySingle);

        ImageView cavalrySingle = new ImageView(new Image("icons/cavalry_single_icon.png"));
        cavalrySingle.setFitHeight(50);
        cavalrySingle.setFitWidth(50);
        cavalryCount.setGraphic(cavalrySingle);

        ImageView cannonSingle = new ImageView(new Image("icons/cannon_single_icon.png"));
        cannonSingle.setFitHeight(50);
        cannonSingle.setFitWidth(50);
        cannonCount.setGraphic(cannonSingle);

        ImageView joker = new ImageView(new Image("icons/combine_icon.png"));
        joker.setFitHeight(50);
        joker.setFitWidth(125);
        jokerCount.setGraphic(joker);


        //buttons
        option1Button = new Button();
        option2Button = new Button();
        option3Button = new Button();
        option4Button = new Button();
        backButton = new Button("Back");

        //set size for buttons
        option1Button.setPrefSize(200, 75);
        option2Button.setPrefSize(200, 75);
        option3Button.setPrefSize(200, 75);
        option4Button.setPrefSize(200, 75);
        backButton.setPrefSize(200, 75);

        //set fonts of the buttons
        option1Button.setFont(javafx.scene.text.Font.font("Snap ITC", 30));
        option2Button.setFont(javafx.scene.text.Font.font("Snap ITC", 30));
        option3Button.setFont(Font.font("Snap ITC", 30));
        option4Button.setFont(Font.font("Snap ITC", 30));
        backButton.setFont(Font.font("Snap ITC", 30));

        //setting background for the buttons
        option1Button.setStyle(styleBackground);
        option2Button.setStyle(styleBackground);
        option3Button.setStyle(styleBackground);
        option4Button.setStyle(styleBackground);
        backButton.setStyle(styleBackground);

        //adding images for the buttons
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
        ///////////////////////////////////////////////////////////////////////////////

        //set style for the texts pane
        texts.setStyle(styleBackground);
        texts.setMaxSize(400,300);

        //adding buttons and texts to the child panes
        texts.getChildren().addAll(infantryCount, cavalryCount, cannonCount, jokerCount, exchangeInfo);
        buttons.getChildren().addAll(option1Button, option2Button, option3Button, option4Button, backButton);

        //setting alignment for the child panes
        texts.setAlignment(Pos.CENTER);
        buttons.setAlignment(Pos.CENTER);

        //adding child panes to the main pane
        this.getChildren().addAll(texts, buttons);
    }

    public Button getBackButton() {
        return backButton;
    }

    public Player getCurPlayer() {
        return curPlayer;
    }

    public void updatePlayerCards(Player player) {
        infantryCount.setText(": " + player.getCards()[0]);
        cavalryCount.setText(": " + player.getCards()[1]);
        cannonCount.setText(": " + player.getCards()[2]);
        jokerCount.setText(": " + player.getCards()[3]);
    }

    public void setPlayerCards(Player player) {
        curPlayer = player;
        exchangeInfo.setText("Choose one of the options below to gain soldier.");
        infantryCount.setText(": " + player.getCards()[0]);
        cavalryCount.setText(": " + player.getCards()[1]);
        cannonCount.setText(": " + player.getCards()[2]);
        jokerCount.setText(": " + player.getCards()[3]);
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
