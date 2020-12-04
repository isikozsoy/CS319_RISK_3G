import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PlayerButton extends StackPane {
    final private int WIDTH = 432;
    final private int HEIGHT = 202;
    final private String BUTTON_STYLE = "-fx-background-color: grey;" +
                                          "-fx-opacity: 0.5;";
    final private String BUTTON_STYLE_HOVERED = "-fx-opacity: 0.8;";
    final private String BUTTON_STYLE_PLAYER = "-fx-opacity:0.8;-fx-background-color:";
    final private String MINUS_BUTTON_STYLE = "-fx-background-image: url(icons/minus_icon.png);" +
            "-fx-background-color:white;" +
            "-fx-background-size: 30px 40px;" +
            "-fx-background-repeat: no-repeat;" +
            "-fx-background-position: center;";
    final private String NAMEFIELD_STYLE = "-fx-text-fill:black;";

    private boolean adderButton; //there is a normal button and an adder button with a plus sign
    private PlayerButton nextPlayer;
    private PlayerButton prevPlayer;
    private Button mainButton;
    private Button minusButton;
    private TextField nameField;
    private String currStyle;
    private Player player;
    private int buttonId;

    private static ArrayList<Player> playerList;
    private static Queue<String> colors;
    private static PlayerButton lastPlayer;

    ObservableList<String> options =
            FXCollections.observableArrayList(
                    "Asia",
                    "North America",
                    "South America",
                    "Africa",
                    "Europe",
                    "Australia"
            );
    final ComboBox CONTINENTS_OPTION_BOX = new ComboBox(options);

    PlayerButton() {
    }

    PlayerButton( boolean adderButton, double locX, double locY, PlayerButton nextPlayer, int buttonId) {
        setLayoutX( locX);
        setLayoutY( locY);

        mainButton = new Button();
        playerList = new ArrayList<>();
        colors = new LinkedList<>();

        //initialize colors
        colors.add("red");
        colors.add("green");
        colors.add("blue");
        colors.add("purple");
        colors.add("pink");
        colors.add("orange");
        colors.add("yellow");

        mainButton.setPrefSize( WIDTH, HEIGHT);
        mainButton.setMaxSize ( WIDTH, HEIGHT);
        mainButton.setMinSize ( 100, 100);

        this.buttonId = buttonId;
        this.adderButton = adderButton;
        this.nextPlayer = nextPlayer;
        lastPlayer = null;
        this.prevPlayer = null; //this is for the first one

        if( nextPlayer != null) nextPlayer.prevPlayer = this;

        currStyle = BUTTON_STYLE;
        mainButton.setStyle( currStyle);

        this.getChildren().add(mainButton);

        if( !adderButton) {
            return;
        }
        else {
            addAdderIcon();
            setAsAdderButton();
        }
    }

    private void addAdderIcon() {
        mainButton.setGraphic( new ImageView( new Image( "icons/plus_icon.png")));
    }

    private void setAsAdderButton() {
        System.out.println(playerList);
        if( minusButton != null) {
            this.getChildren().removeAll(minusButton, nameField, CONTINENTS_OPTION_BOX);
            minusButton = null;
        }

        this.adderButton = true;
        lastPlayer = this;
        addAdderIcon();
        this.currStyle = BUTTON_STYLE;
        mainButton.setStyle( currStyle);

        mainButton.setOnMousePressed( e -> {
            player = new Player("Player" + (buttonId + 1), buttonId, "Asia", 3, colors.remove());   // temporary
            playerList.add( buttonId, player);

            setAsPlayerButton();
            if(nextPlayer == null) {
                lastPlayer = this;
            }
            else {
                nextPlayer.setAsAdderButton();
            }
        });
        mainButton.setOnMouseEntered( e -> {
            mainButton.setStyle( BUTTON_STYLE_HOVERED);
        });
        mainButton.setOnMouseExited( e -> {
            mainButton.setStyle( currStyle);
        });
    }

    private void setAsPlayerButton() {
        //set the remove player button
        minusButton = new Button();
        minusButton.setStyle(MINUS_BUTTON_STYLE);
        setAlignment(minusButton, Pos.TOP_RIGHT);
        //setting a player name input option
        nameField = new TextField("Player" + (buttonId+ 1));
        nameField.setFont(Font.font("Snap ITC", 30));
        nameField.setStyle(NAMEFIELD_STYLE);
        nameField.setPrefSize(WIDTH - 20, 40);
        setAlignment(nameField, Pos.CENTER);
        //dropdown menu box for target continent choosing
        setAlignment(CONTINENTS_OPTION_BOX, Pos.BOTTOM_CENTER);
        //add all of these to the pane
        this.getChildren().addAll(minusButton, nameField, CONTINENTS_OPTION_BOX);

        this.adderButton = false;
        mainButton.setGraphic(null);
        this.currStyle = BUTTON_STYLE_PLAYER + playerList.get(buttonId).getColor() + ";";
        mainButton.setStyle( currStyle);

        //change name when name field changes
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            playerList.get(buttonId).setName(newValue);
        });

        CONTINENTS_OPTION_BOX.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            playerList.get(buttonId).setTargetCont((String) newValue);
        });

        minusButton.setOnMousePressed( e -> {
            colors.add(playerList.get(buttonId).getColor());
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
                curr.mainButton.setStyle( curr.currStyle);
                curr = curr.nextPlayer;
            }
        });

        mainButton.setOnMousePressed(e -> {
            colors.add(playerList.get(buttonId).getColor());
            String newColor = colors.remove();
            playerList.get(buttonId).setColor(newColor);
            this.currStyle = BUTTON_STYLE_PLAYER + playerList.get(buttonId).getColor() + ";";
            mainButton.setStyle( currStyle);
        });

        mainButton.setOnMouseEntered( e -> {
            mainButton.setStyle(BUTTON_STYLE_HOVERED);
        });

        mainButton.setOnMouseExited( e -> {
            mainButton.setStyle(currStyle);
        });
    }

    private void setAsEmptyButton() {
        if( minusButton != null) {
            this.getChildren().removeAll(minusButton, nameField);
            minusButton = null;
        }
        this.adderButton = false;
        mainButton.setGraphic(null);
        this.currStyle = BUTTON_STYLE;
        mainButton.setStyle(currStyle);

        mainButton.setOnMousePressed( e -> {
        });
        mainButton.setOnMouseEntered( e -> {
        });
        mainButton.setOnMouseExited( e -> {
        });
    }

    public int getButtonWidth() {
        return WIDTH;
    }

    public int getButtonHeight() {
        return HEIGHT;
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }
}