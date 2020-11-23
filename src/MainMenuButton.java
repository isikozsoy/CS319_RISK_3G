import javafx.scene.control.Button;

public class MainMenuButton extends Button {
    //font is Erica One
    final private String BUTTON_PRESSED_STYLE = "-fx-background-color: #E9C46A;" +
                                                "-fx-opacity: 0.5;" +
                                                "-fx-text-fill: #000000;";
    final private String BUTTON_FREE_STYLE    = "-fx-background-color: #F50000;" +
                                                "-fx-opacity: 0.5;" +
                                                "-fx-text-fill: #000000;";
    final private int MENU_WIDTH = 720;
    final private int MENU_HEIGHT = 80;

    public MainMenuButton(int locX, int locY) {
        setPrefWidth(MENU_WIDTH);
        setPrefHeight(MENU_HEIGHT);
        setStyle(BUTTON_FREE_STYLE);
        setEventListeners();
        setLayoutX( locX);
        setLayoutY( locY);
    }

    private void setEventListeners(){
        setOnMousePressed( e -> setStyle(BUTTON_PRESSED_STYLE));
        setOnMouseReleased( e -> setStyle(BUTTON_FREE_STYLE));
        setOnMouseEntered( e -> setStyle(BUTTON_PRESSED_STYLE));
        setOnMouseExited ( e -> setStyle(BUTTON_FREE_STYLE));
    }

}