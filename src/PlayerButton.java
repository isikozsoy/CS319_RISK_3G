import javafx.scene.control.Button;

public class PlayerButton extends Button {
    final String ICON_DIRECTORY = "icons/";
    final String PLUS_ICON_PATH = ICON_DIRECTORY + "plus_icon.png";
    final String MINUS_ICON_PATH = ICON_DIRECTORY + "minus_icon.png";
    final String BUTTON_STYLE_WIDTH_HEIGHT = "-fx-pref-width: 432px;" +
                                            "-fx-pref-height: 202px;";
    final int WIDTH = 432;
    final int HEIGHT = 202;

    PlayerButton( double locX, double locY) {
        setLayoutX( locX);
        setLayoutY( locY);

        setPrefWidth ( WIDTH);
        setPrefHeight( HEIGHT);
        setMaxSize( WIDTH, HEIGHT);
        setMinSize( 100, 100);
    }

    public int getButtonWidth() {
        return WIDTH;
    }

    public int getButtonHeight() {
        return HEIGHT;
    }

}