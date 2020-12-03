import javafx.scene.control.Button;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AddPlayersView extends BeforeGameView {
    private final String ICON_BUTTON_STYLE = "-fx-background-color: transparent;";
    private final String ARROW_BUTTON_PATH = "/icons/" + "arrow_icon.png";

    private final int BETWEEN_TWO_PLAYER_BUTTONS = 10;
    private int playerCount = 0;

    private final int firstLocX = 0;
    private final int firstLocY = 0;
    private final int[] BUTTON_SIZE = {432, 202};

    private int width;
    private int height;

    private ArrayList<Player> playerList;
    private LinkedHashMap<ButtonPaneStrategy, int[]> buttonPaneAndLocation;

    AddPlayersView( Stage stage, int width, int height) {
        super();
        playerList = new ArrayList<>();
        buttonPaneAndLocation = new HashMap<>();

        addPlayerButtons();
        addGameButton( stage);
        addBackButton( stage);

        this.width = width;
        this.height = height;
    }

    private void addPlayerButtons() {
        GridPane playerPane = new GridPane();
        int buttonWidth = 432;
        int buttonHeight = 202;

        playerPane.setVgap(10);
        playerPane.setHgap(10);

        int totalPlayerButtons = 6;
        int[] rowCol = new int[2];
        System.out.println(playerList.size());

        StackPane newPane;
        for(int i = 0; i < totalPlayerButtons; i++) {
            if( i == 0) {
                newPane = new AdderButtonPane(buttonWidth, buttonHeight);
            }
            else {
                newPane = new EmptyButtonPane(buttonWidth, buttonHeight);
            }
            switch (i) {
                case 0 -> {
                    rowCol[0] = 0;
                    rowCol[1] = 0;
                    break;
                }
                case 1 -> {
                    rowCol[0] = 1;
                    rowCol[1] = 0;
                    break;
                }
                case 2 -> {
                    rowCol[0] = 0;
                    rowCol[1] = 1;
                    break;
                }
                case 3 -> {
                    rowCol[0] = 1;
                    rowCol[1] = 1;
                    break;
                }
                case 4 -> {
                    rowCol[0] = 0;
                    rowCol[1] = 2;
                    break;
                }
                case 5 -> {
                    rowCol[0] = 1;
                    rowCol[1] = 2;
                    break;
                }
            }
            playerPane.add(newPane, rowCol[0], rowCol[1]);
        }
/**
        for( int i = 0; i < totalPlayerButtons; i++) {
            StackPane newPane;
            if( i >= playerList.size()) {
                if( !adderButton) {
                    adderButton = true;
                    newPane = new AdderButtonPane(432, 202);
                    ((AdderButtonPane)newPane).clickAction(this, playerList);
                }
                else {
                    newPane = new EmptyButtonPane(0, 0, 432, 202);
                }
            }
            else {
                newPane = new PlayerButtonPane(0, 432, 202, playerList);
                ((PlayerButtonPane) newPane).clickAction(this, playerList);
            }
            switch (i) {
                case 0 -> {
                    playerPane.add(newPane, 0, 0);
                    break;
                }
                case 1 -> {
                    playerPane.add(newPane, 1, 0);
                    break;
                }
                case 2 -> {
                    playerPane.add(newPane, 0, 1);
                    break;
                }
                case 3 -> {
                    playerPane.add(newPane, 1, 1);
                    break;
                }
                case 4 -> {
                    playerPane.add(newPane, 0, 2);
                    break;
                }
                case 5 -> {
                    playerPane.add(newPane, 1, 2);
                    break;
                }
            }
        }
**/
        Group g = new Group();
        g.getChildren().addAll( playerPane);
        super.addGroup(g);
    }

    public void recreateButtonsRemoved( int buttonId) {
        int id = buttonId;
        for( int i = buttonId; i < 6; i++) {

        }
    }

    public void recreateButtonsAdded() {

    }

    private void addBackButton( Stage stage) {
        Button backButton = new Button();
        ImageView backButtonView = new ImageView( new Image(ARROW_BUTTON_PATH));
        backButtonView.setRotate( backButtonView.getRotate() - 90);
        backButton.setGraphic( backButtonView);
        backButton.setStyle(ICON_BUTTON_STYLE);

        backButton.setOnMousePressed( e -> {
            MainMenuView mainMenuView = new MainMenuView( stage, width, height);
            Scene newScene = new Scene(mainMenuView, width, height);
            stage.setScene( newScene);
        });

        backButton.setOnMouseEntered( e -> {
            backButton.setEffect( new DropShadow());
        });
        backButton.setOnMouseExited( e -> {
            backButton.setEffect( null);
        });

        this.getChildren().add(backButton);
        this.setAlignment(backButton, Pos.TOP_LEFT);
    }

    private void addGameButton( Stage stage) {
        Button gameButton  = new Button();
        ImageView gameButtonView = new ImageView( new Image(ARROW_BUTTON_PATH));
        gameButtonView.setRotate( gameButtonView.getRotate() + 90);
        gameButton.setGraphic( gameButtonView);
        gameButton.setStyle(ICON_BUTTON_STYLE);

        gameButton.setOnMousePressed( e -> {
            RiskView gameView = new RiskView( stage);
            Scene newScene = new Scene(gameView, width, height);
            stage.setScene( newScene);
        });

        gameButton.setOnMouseEntered( e -> {
            gameButton.setEffect( new DropShadow());
        });
        gameButton.setOnMouseExited( e -> {
            gameButton.setEffect( null);
        });

        this.getChildren().add(gameButton);
        this.setAlignment(gameButton, Pos.BOTTOM_RIGHT);
    }

}