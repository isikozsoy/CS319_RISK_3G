import javafx.scene.control.Button;
import javafx.scene.image.*;

public class PlayerButtonBeforeClicking extends PlayerButton {
    private final String BUTTON_STYLE = "-fx-background-color: grey;" +
                                        "-fx-opacity: 0.5;";
    private final String BUTTON_STYLE_HOVERED = "-fx-background-color: white;" +
                                                "-fx-opacity: 0.5;";
    private final String[] PLAYER_COLORS = {"red", "green", "blue", "yellow", "orange", "purple", "pink"};

    private boolean adderButton = false; //there is a normal button that is not clickable and an adder button
                                        // that is clickable
    private PlayerButtonBeforeClicking nextPlayer;

    PlayerButtonBeforeClicking( boolean adderButton, double locX, double locY, PlayerButtonBeforeClicking nextPlayer) {
        super(locX, locY);

        this.adderButton = adderButton;
        this.nextPlayer = nextPlayer;

        setStyle( BUTTON_STYLE + BUTTON_STYLE_WIDTH_HEIGHT);

        if( !adderButton) {
            return;
        }
        else {
            addIcon();
            setEventListeners();
        }
    }

    private void addIcon() {
        setGraphic( new ImageView( new Image( PLUS_ICON_PATH)));
    }

    private PlayerButtonBeforeClicking getNext() {
        return nextPlayer;
    }

    private void setEventListeners() {
        setOnMousePressed( e -> {
            //yeni button'a gönder
            //this = new PlayerButtonAfterClicking(); //bu olmuyor
            if(nextPlayer == null) {
                return;
            }
            System.out.println("Clicked on player");
            setStyle(BUTTON_STYLE_HOVERED); //PLACEHOLDER
            nextPlayer.setAsAdderButton(true);
        });
        setOnMouseEntered( e -> {
            setStyle( BUTTON_STYLE_HOVERED + BUTTON_STYLE_WIDTH_HEIGHT);
        });
        setOnMouseExited( e -> {
            setStyle( BUTTON_STYLE + BUTTON_STYLE_WIDTH_HEIGHT);
        });
    }

    private void setAsAdderButton( boolean adderButton) {
        if( adderButton) {
            this.adderButton = adderButton;
            addIcon();
            setEventListeners();
        }
    }
}