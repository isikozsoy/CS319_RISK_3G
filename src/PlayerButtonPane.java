import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class PlayerButtonPane extends StackPane {
    private final int WIDTH = 432;
    private final int HEGHT = 202;

    private Button minusButton;
    private Button rectangleColorButton;
    private TextField nameField;
    private Button colorChangeButton;
    private int buttonId;
    public Player player;
    ObservableList<String> options =
            FXCollections.observableArrayList("Asia", "Australia", "Europe", "North America", "Africa",
                    "South America"
            );
    final ComboBox comboBox = new ComboBox(options);

    PlayerButtonPane(int buttonId, int locX, int locY) {
        minusButton = new Button();
        rectangleColorButton = new Button();
        colorChangeButton = new Button();
        nameField = new TextField("Player" + buttonId);

        this.buttonId = buttonId;
        minusButton.setGraphic(new ImageView(new Image("icons/minus_icon.png")));
        colorChangeButton.setGraphic(new ImageView(new Image("icons/color_icon.png")));

        rectangleColorButton.setLayoutX(locX);
        rectangleColorButton.setLayoutY(locY);
        minusButton.setLayoutX(locX + 5);
        minusButton.setLayoutY(locY - 5);

        rectangleColorButton.setMaxWidth(WIDTH);
        rectangleColorButton.setMaxHeight(HEGHT);

        this.getChildren().addAll(rectangleColorButton, minusButton, nameField, comboBox);
        player = new Player(nameField.getText(), buttonId, (String) comboBox.getValue(), 6);
    }


}
