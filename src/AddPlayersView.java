import javafx.scene.control.Button;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;

import java.util.ArrayList;

public class AddPlayersView extends BeforeGameView {
    private final String ICON_BUTTON_STYLE = "-fx-background-color: transparent;";
    private final String ARROW_BUTTON_PATH = "/icons/" + "arrow_icon.png";

    private final int BETWEEN_TWO_PLAYER_BUTTONS = 10;
    private int playerCount = 0;

    private final int firstLocX = 182;
    private final int firstLocY = 190;

    private int width;
    private int height;
    private ArrayList<Player> playerList;
    private PlayerButton firstButton;
    private Stage stage;

    private Button gameButton;

    AddPlayersView( Stage stage, int width, int height) {
        super();
        addPlayerButtons();
        this.stage = stage;
        setGameButton();
        addBackButton();

        this.width = width;
        this.height = height;
    }

    private void addPlayerButtons() {
        PlayerButton playerButtonBeforeClicking6 = new PlayerButton( false,
                firstLocX + (new PlayerButton()).getButtonWidth() + BETWEEN_TWO_PLAYER_BUTTONS,
                firstLocY + (new PlayerButton()).getButtonHeight() * 2 + BETWEEN_TWO_PLAYER_BUTTONS * 2,
                null, 5, this);
        PlayerButton playerButtonBeforeClicking5 = new PlayerButton( false,
                firstLocX,
                firstLocY + playerButtonBeforeClicking6.getButtonHeight() * 2 + BETWEEN_TWO_PLAYER_BUTTONS * 2,
                playerButtonBeforeClicking6, 4, this);
        PlayerButton playerButtonBeforeClicking4 = new PlayerButton( false,
                firstLocX + playerButtonBeforeClicking6.getButtonWidth() + BETWEEN_TWO_PLAYER_BUTTONS,
                firstLocY + playerButtonBeforeClicking6.getButtonHeight() + BETWEEN_TWO_PLAYER_BUTTONS,
                playerButtonBeforeClicking5, 3, this);
        PlayerButton playerButtonBeforeClicking3 = new PlayerButton( false,
                firstLocX,
                firstLocY + playerButtonBeforeClicking6.getButtonHeight() + BETWEEN_TWO_PLAYER_BUTTONS,
                playerButtonBeforeClicking4, 2, this);
        PlayerButton playerButtonBeforeClicking2 = new PlayerButton( false,
                firstLocX + playerButtonBeforeClicking6.getButtonWidth() + BETWEEN_TWO_PLAYER_BUTTONS,
                firstLocY,
                playerButtonBeforeClicking3, 1, this);
        firstButton = new PlayerButton( true,
                firstLocX,
                firstLocY,
                playerButtonBeforeClicking2, 0, this);

        Group g = new Group();
        g.getChildren().addAll( firstButton, playerButtonBeforeClicking2,
                                playerButtonBeforeClicking3, playerButtonBeforeClicking4,
                                playerButtonBeforeClicking5, playerButtonBeforeClicking6);
        super.addGroup(g);
    }

    private void addBackButton() {
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

    private void setGameButton() {
        gameButton = new Button();
        ImageView gameButtonView = new ImageView( new Image(ARROW_BUTTON_PATH));
        gameButtonView.setRotate( gameButtonView.getRotate() + 90);
        gameButton.setGraphic( gameButtonView);
        gameButton.setStyle(ICON_BUTTON_STYLE);

        gameButton.setOnMousePressed( e -> {
            playerList = firstButton.getPlayerList();
            for (Player p : playerList)
                p.setPlayerCount(playerCount);
            RiskView gameView = new RiskView( stage, playerList, width, height);
            Scene newScene = new Scene(gameView, width, height);
            stage.setScene( newScene);
        });

        gameButton.setOnMouseEntered( e -> {
            gameButton.setEffect( new DropShadow());
        });
        gameButton.setOnMouseExited( e -> {
            gameButton.setEffect( null);
        });
        this.setAlignment(gameButton, Pos.BOTTOM_RIGHT);
    }

    public void addGameButton() {
        this.getChildren().add(gameButton);
    }

    public void removeGameButton() {
        this.getChildren().remove(gameButton);
    }
}