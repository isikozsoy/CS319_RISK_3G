import javafx.scene.layout.*;
import javafx.scene.control.Button;

public class AddPlayersView extends StackPane {
    private final String ICON_BUTTON_STYLE = "-fx-background-color: transparent;";

    AddPlayersView( int width, int height) {
        setPrefSize( width, height);
    }

    private void addPlayerButton() {
        Button plusIcon = new Button( new Image( PLUS_ICON_PATH));
        plusIcon.setStyle( ICON_BUTTON_STYLE);
    }
}