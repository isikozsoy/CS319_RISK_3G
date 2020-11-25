import javafx.scene.control.Button;
import javafx.scene.image.*;
import java.util.ArrayList;

public class PlayerButton extends Button {
    final private String ICON_DIRECTORY = "icons/";
    final private String PLUS_ICON_PATH = ICON_DIRECTORY + "plus_icon.png";
    final private String MINUS_ICON_PATH = ICON_DIRECTORY + "minus_icon.png";
    final private int WIDTH = 432;
    final private int HEIGHT = 202;
    final private String BUTTON_STYLE = "-fx-background-color: grey;" +
                                          "-fx-opacity: 0.5;";
    final private String BUTTON_STYLE_HOVERED = "-fx-background-color: white;" +
                                            "-fx-opacity: 0.5;";
    final private String BUTTON_STYLE_PLAYER = "-fx-opacity:0.8;-fx-background-color:";

    private boolean adderButton; //there is a normal button and an adder button with a plus sign
    private PlayerButton nextPlayer;
    private PlayerButton prevPlayer;
    private String currStyle;
    private Player player;
    private int buttonId;
    private static ArrayList<Player> playerList;

    private static PlayerButton lastPlayer;

    PlayerButton() {
    }

    PlayerButton( boolean adderButton, double locX, double locY, PlayerButton nextPlayer, int buttonId) {
        setLayoutX( locX);
        setLayoutY( locY);

        setPrefSize( WIDTH, HEIGHT);
        setMaxSize ( WIDTH, HEIGHT);
        setMinSize ( 100, 100);

        playerList = new ArrayList<Player>();
        this.buttonId = buttonId;
        this.adderButton = adderButton;
        this.nextPlayer = nextPlayer;
        this.player = player;
        this.lastPlayer = null;
        this.prevPlayer = null; //this is for the first one

        if( nextPlayer != null) nextPlayer.prevPlayer = this;

        currStyle = BUTTON_STYLE;
        setStyle( currStyle);

        if( !adderButton) {
            return;
        }
        else {
            addIcon();
            setAsAdderButton();
        }
    }

    private void addIcon() {
        setGraphic( new ImageView( new Image( PLUS_ICON_PATH)));
    }

    private PlayerButton getNext() {
        return nextPlayer;
    }
    private PlayerButton getPrev() {
        return prevPlayer;
    }

    private void setAsAdderButton() {
        this.adderButton = true;
        lastPlayer = this;
        addIcon();
        this.currStyle = BUTTON_STYLE;
        setStyle( currStyle);

        setOnMousePressed( e -> {
            Player player = new Player("isik", 0, "Asia", 3);   // temporary
            playerList.add( buttonId, player);

            setAsPlayerButton();
            if(nextPlayer == null) {
                lastPlayer = this;
            }
            else {
                nextPlayer.setAsAdderButton();
            }
        });
        setOnMouseEntered( e -> {
            setStyle( BUTTON_STYLE_HOVERED);
        });
        setOnMouseExited( e -> {
            setStyle( currStyle);
        });
    }

    private void setAsPlayerButton() {
        Player player = playerList.get(buttonId);

        this.adderButton = false;
        setGraphic(null);
        this.currStyle = BUTTON_STYLE_PLAYER + player.getColor() + ";";
        setStyle( currStyle);

        setOnMousePressed( e -> {
            playerList.remove( buttonId);

            boolean lastIsSixth = lastPlayer.nextPlayer == null && !lastPlayer.adderButton;
            if( lastIsSixth)
                lastPlayer.setAsAdderButton();//we need to NOT change last player at this point
            else
                lastPlayer.setAsEmptyButton();

            //if last player really IS the last player, as in the 6th player, then the lastPlayer variable needs to
            // stay as it is

            if( lastPlayer.prevPlayer != null && !lastIsSixth) {
                lastPlayer = lastPlayer.prevPlayer;
                lastPlayer.setAsAdderButton();
            }
            else if(!lastIsSixth) {
                lastPlayer = this;
            }

            PlayerButton curr = this;
            while( curr.buttonId != lastPlayer.buttonId) {
                Player newPlayer = playerList.get( curr.buttonId);
                curr.currStyle = BUTTON_STYLE_PLAYER + newPlayer.getColor() + ";";
                curr.setStyle( curr.currStyle);
                curr = curr.nextPlayer;
            }
        });

        setOnMouseEntered( e -> {
            setStyle(BUTTON_STYLE_HOVERED);
        });

        setOnMouseExited( e -> {
            setStyle(currStyle);
        });
    }

    private void setAsEmptyButton() {
        this.adderButton = false;
        setGraphic(null);
        this.currStyle = BUTTON_STYLE;
        setStyle(currStyle);

        setOnMousePressed( e -> {
        });
        setOnMouseEntered( e -> {
        });
        setOnMouseExited( e -> {
        });
    }

    public int getButtonWidth() {
        return WIDTH;
    }

    public int getButtonHeight() {
        return HEIGHT;
    }
}