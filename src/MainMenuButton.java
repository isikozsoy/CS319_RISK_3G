import javafx.scene.control.Button;

public class MainMenuButton extends Button {
    //font is Erica One
    final private String BUTTON_FREE_STYLE     = "-fx-background-color: linear-gradient(to right, yellow, tomato);" +
                                                "-fx-opacity:0.6;" +
            //"-fx-background-color: linear-gradient(to right, yellow, tomato);" +
                                                     "-fx-background-radius: 3px;";
    final private String BUTTON_HOVERED_STYLE  = "-fx-background-color: tomato; -fx-opacity:0.6;" ;
    /**
    final private String BUTTON_FREE_STYLE    = "-fx-background-color: #F50000;" +
                                                "-fx-opacity: 0.5;" +
                                                "-fx-text-fill: #000000;";
    **/
    final private int MENU_WIDTH = 720;
    final private int MENU_HEIGHT = 80;
    public int menuButtonCount = 0;
    private int buttonLocX;
    private int buttonLocY;

    public MainMenuButton(int locX, int locY, int count) {
        buttonLocX = locX;
        buttonLocY = locY;

        setPrefWidth(MENU_WIDTH);
        setPrefHeight(MENU_HEIGHT);

        menuButtonCount = count;

        setStyle( BUTTON_FREE_STYLE);

        setEventListeners();
        setLayoutX( locX);
        setLayoutY( locY);
    }

    public int getButtonWidth() {
        return MENU_WIDTH;
    }

    public int getButtonHeight() {
        return MENU_HEIGHT;
    }

    public int getButtonLocX() {
        return buttonLocX;
    }

    public int getButtonLocY () {
        return buttonLocY;
    }

    private void setEventListeners(){
        setOnMousePressed( e -> {
            setStyle(BUTTON_HOVERED_STYLE);
        });
        //setOnMouseReleased( e -> setStyle(BUTTON_FREE_STYLE));
        setOnMouseEntered( e -> {
            setStyle(BUTTON_HOVERED_STYLE);
        });
        setOnMouseExited ( e -> {
            setStyle( BUTTON_FREE_STYLE);
        });
    }
}