import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.control.Button;
import java.awt.Dimension;
import java.awt.Toolkit;

public class MainMenuView extends BeforeGameView {
    final private int MENU_WIDTH          = 800;
    final private int MENU_HEIGHT         = 600;

    final private int   locXForMenu       = 290;
    final private int   locYIncrease      = 100;
    final private int[] locLogo           = {locXForMenu, 75};
    final private int   locYNewGame       = 290;
    final private int[] locNewGameButton  = {locXForMenu, locYNewGame};
    final private int[] locHowTo          = {locXForMenu, locYNewGame + locYIncrease};
    final private int[] locSettings       = {locXForMenu, locYNewGame + 2 * locYIncrease};
    final private int[] locCredits        = {locXForMenu, locYNewGame + 3 * locYIncrease};
    final private int[] locExit           = {locXForMenu, locYNewGame + 4 * locYIncrease};
    final private int   locYText          = 304;

    final private String LOGO_STYLE       = "-fx-background-color: linear-gradient(to right, yellow, tomato);" +
                                            "-fx-opacity:0.6;";

    Dimension screenSize;

    MainMenuView( Stage stage) {
        System.out.println("Here");
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        buttonsPane( stage);
    }

    private void buttonsPane(Stage stage){
        MainMenuButton newGameButton  = newGameButton(stage);
        MainMenuText   newGameText    = new MainMenuText(482, locYText, "NEW GAME");

        MainMenuButton howToButton    = howToButton();
        MainMenuText   howToText      = new MainMenuText  (395, locYText + locYIncrease, "HOW TO PLAY");

        MainMenuButton settingsButton = settingsButton();
        MainMenuText   settingsText   = new MainMenuText  (474, locYText + locYIncrease * 2, "SETTINGS");

        MainMenuButton creditsButton  = creditsButton();
        MainMenuText   creditsText    = new MainMenuText  ( 474, locYText + locYIncrease * 3, "CREDITS");

        MainMenuButton exitButton     = exitButton( stage);
        MainMenuText   exitText       = new MainMenuText  ( 548, locYText + locYIncrease * 4, "EXIT");

        Button annexationLogo         = new Button();
        annexationLogo.setLayoutX(locLogo[0]);
        annexationLogo.setLayoutY(locLogo[1]);
        annexationLogo.setStyle(LOGO_STYLE);
        annexationLogo.setMinWidth(exitButton.getButtonWidth());
        annexationLogo.setMinHeight(exitButton.getButtonHeight());
        MainMenuText annexationText = new MainMenuText( 432, locLogo[1] + 10, "ANNEXATION");

        /**
        TODO:: BIND BUTTON SIZES TO PANE SIZE
        **/
        Group g = new Group();
        g.getChildren().addAll(newGameText,   howToText,   settingsText,   creditsText,   exitText,
                newGameButton, howToButton, settingsButton, creditsButton, exitButton);
        g.getChildren().addAll( annexationLogo, annexationText);
        super.addGroup(g);
    }

    private MainMenuButton newGameButton(Stage stage) {
        MainMenuButton newGameButton = new MainMenuButton(locNewGameButton[0], locNewGameButton[1], 1);

        newGameButton.setOnMouseClicked( e -> {
            System.out.println("Clicked on New Game");
            //RiskView gameView = new RiskView();
            AddPlayersView addPlayersView = new AddPlayersView( stage);
            Scene addPlayerScene = new Scene( addPlayersView, 1280, 1024);

            stage.setScene( addPlayerScene);
            //stage.show();
        });

        return newGameButton;
    }

    private MainMenuButton howToButton() {
        MainMenuButton howToButton = new MainMenuButton(locHowTo[0], locHowTo[1], 2);

        return howToButton;
    }

    private MainMenuButton settingsButton() {
        MainMenuButton settingsButton = new MainMenuButton(locSettings[0], locSettings[1], 3);

        return settingsButton;
    }

    private MainMenuButton creditsButton() {
        MainMenuButton creditsButton = new MainMenuButton(locCredits[0], locCredits[1], 4);

        return creditsButton;
    }

    private MainMenuButton exitButton( Stage stage) {
        MainMenuButton exitButton = new MainMenuButton( locExit[0], locExit[1], 5);

        exitButton.setOnMousePressed( e -> {
            stage.close();
        });

        return exitButton;
    }
/**
    private void bindButtonsToPane( MainMenuButton button) {
        button.fitWidthProperty().bind( this.widthProperty());
        button.fitHeightProperty().bind( this.heightProperty());
        button.setPreserveRatio( true);
    }
 **/
}