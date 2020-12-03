import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerButtonPane extends StackPane implements ButtonPaneStrategy {
    private Button minusButton;
    private Button rectangleColorButton;
    private TextField nameField;
    private Button colorChangeButton;
    private int buttonId;
    public Player player;

    private final String BUTTON_STYLE = "-fx-opacity:0.5;-fx-background-color:blue;";
    private final String ICON_BUTTON_STYLE = "-fx-background-color: transparent;";

    ObservableList<String> options =
            FXCollections.observableArrayList("Asia", "Australia", "Europe", "North America", "Africa",
                    "South America"
            );
    final ComboBox comboBox = new ComboBox(options);

    PlayerButtonPane(int buttonId, int width, int height, ArrayList<Player> playerList) {
        minusButton = new Button();
        rectangleColorButton = new Button();
        colorChangeButton = new Button();
        nameField = new TextField("Player" + buttonId);

        minusButton.setStyle(ICON_BUTTON_STYLE);
        colorChangeButton.setStyle(ICON_BUTTON_STYLE);

        this.buttonId = buttonId;
        minusButton.setGraphic(new ImageView(new Image("icons/minus_icon.png")));
        colorChangeButton.setGraphic(new ImageView(new Image("icons/color_icon.png")));
        minusButton.setMaxSize(20,20);
        colorChangeButton.setMaxSize(20, 20);
        rectangleColorButton.setMaxSize(width, height);
        rectangleColorButton.setPrefSize(width, height);
        rectangleColorButton.setStyle(BUTTON_STYLE);

        setAlignment(minusButton, Pos.TOP_LEFT);

        comboBox.setStyle(ICON_BUTTON_STYLE);

        this.getChildren().addAll(rectangleColorButton, minusButton, nameField, comboBox);
        this.player = new Player(nameField.getText(), buttonId, (String) comboBox.getValue(), 6);
    }


    @Override
    public void clickAction(AddPlayersView pane, ArrayList<Player> playerList) {
        minusButton.setOnMouseClicked(e -> {
            playerList.remove(player);
            pane.addPlayerButtons();
        });
        minusButton.setOnMouseEntered(e -> {
            minusButton.setEffect( new DropShadow());
        });
        minusButton.setOnMouseExited(e -> {
            minusButton.setEffect( null);
        });
    }
}
