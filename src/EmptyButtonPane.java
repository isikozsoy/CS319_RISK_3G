import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public class EmptyButtonPane extends StackPane implements ButtonPaneStrategy {
    private final String BUTTON_STYLE = "-fx-background-color: grey;" +
                                        "-fx-opacity: 0.5;";
    Button button;

    EmptyButtonPane(int width, int height) {
        button = new Button();

        button.setPrefSize(width, height);
        button.setStyle(BUTTON_STYLE);

        this.getChildren().add(button);
    }


    @Override
    public void clickAction(AddPlayersView pane, ArrayList<Player> playerList) {
        //nothing happens when clicked for this button type
    }
}
