import javafx.scene.control.Button;
import javafx.scene.image.*;

public class PlayerButton extends Button {
    private final String BUTTON_STYLE = "-fx-background-color: grey;" +
                                        "-fx-opacity: 0.5;";
    private final String BUTTON_STYLE_HOVERED = "-fx-background-color: white;" +
                                                "-fx-opacity: 0.5;"
    private final String PLAYER_COLORS = {"red", "green", "blue", "yellow", "orange", "purple", "pink"}
    private String BUTTON_PRESSED_STYLE = "-fx-background-color: red;" +
                                                "-fx-opacity: 0.5;";
    private final String ICON_DIRECTORY = "icons/";
    private final String PLUS_ICON_PATH = ICON_DIRECTORY + "plus_icon.png";
    private final String MINUS_ICON_PATH = ICON_DIRECTORY + "minus_icon.png";

    PlayerButtonBeforeClicking() {
        setStyle( BUTTON_STYLE);
        addIcons();
        setEventListeners();
    }

    private void addIcons() {
        super( new ImageView( new Image( PLUS_ICON_PATH)));
    }

    private void setEventListeners() {
        setOnMousePressed( e -> {
            //yeni button'a gönder
            this = new PlayerButtonAfterClicking();
        });
        setOnMouseEntered( e -> {
            setStyle( BUTTON_STYLE_HOVERED);
        });
        setOnMouseExited( e -> {
            setStyle( BUTTON_STYLE);
        });
    }
}