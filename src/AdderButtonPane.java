import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public class AdderButtonPane extends StackPane implements ButtonPaneStrategy{
    final private String BUTTON_STYLE = "-fx-background-color: transparent;" +
            "-fx-opacity: 0.5;-fx-max-width: 432;-fx-max-height: 202;";
    final private String BUTTON_STYLE_HOVERED = "-fx-background-color: white;" +
            "-fx-opacity: 0.5;-fx-max-width: 432;-fx-max-height: 202;";
    private Button adderButton;

    AdderButtonPane( int width, int height) {
        adderButton = new Button();
        adderButton.setMaxSize(width, height);
        adderButton.setStyle(BUTTON_STYLE);
        adderButton.setGraphic( new ImageView( new Image("icons/plus_icon.png")));

        this.getChildren().add(adderButton);
    }

    @Override
    public void clickAction(AddPlayersView pane, ArrayList<Player> playerList) {
        adderButton.setOnMouseClicked(e -> {
            playerList.add(new Player());
            pane.addPlayerButtons();
        });
        adderButton.setOnMouseEntered(e -> {
            adderButton.setStyle(BUTTON_STYLE_HOVERED);
        });
        adderButton.setOnMouseExited(e -> {
            adderButton.setStyle(BUTTON_STYLE);
        });
    }
}
