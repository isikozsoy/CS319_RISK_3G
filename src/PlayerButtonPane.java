import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class PlayerButtonPane extends StackPane {
    private final int WIDTH = 432;
    private final int HEGHT = 202;

    private Button minusButton;
    private Button rectangleColorButton;
    private TextField nameField;
    private Button getColorButton;
    private int buttonId;
    public Player player;

    PlayerButtonPane(int buttonId, int locX, int locY) {
        minusButton = new Button();
        rectangleColorButton = new Button();
        nameField = new TextField("Player" + buttonId);

        this.getChildren().addAll(rectangleColorButton, minusButton, nameField);

        this.buttonId = buttonId;

        rectangleColorButton.setLayoutX(locX);
        rectangleColorButton.setLayoutY(locY);
        minusButton.setLayoutX(locX + 5);
        minusButton.setLayoutY(locY - 5);

        rectangleColorButton.setMaxWidth(WIDTH);
        rectangleColorButton.setMaxHeight(HEGHT);

        ObservableList<String> options =
                FXCollections.observableArrayList("Asia", "Australia", "Europe", "North America", "Africa",
                                                "South America"
                );
        final ComboBox comboBox = new ComboBox(options);

    }


}
