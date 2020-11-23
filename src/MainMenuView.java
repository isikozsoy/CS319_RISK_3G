import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;

public class MainMenuView extends AnchorPane {
    final private int   locXForMenu       = 290;
    final private int   locYIncrease      = 125;
    final private int[] locLogo           = {locXForMenu, 106};
    final private int   locYNewGame       = 391;
    final private int[] locNewGameButton  = {locXForMenu, locYNewGame};
    final private int[] locHowTo          = {locXForMenu, locYNewGame + locYIncrease};
    final private int[] locSettings       = {locXForMenu, locYNewGame + 2 * locYIncrease};
    final private int[] locCredits        = {locXForMenu, locYNewGame + 3 * locYIncrease};
    final private int[] locExit           = {locXForMenu, locYNewGame + 4 * locYIncrease};

    private String BACKGROUND_FILE_PATH   = "img/" + "background_image.png";

    final private String BACKGROUND_STYLE = "-fx-background-image: url(\"BACKGROUND_FILE_PATH\");" +
                                            "-fx-background-size: cover;";

    MainMenuView( Stage stage) {
        addBackgroundImage();
        buttonsPane( stage);
    }

    private void setLogo() {

    }

    private void addBackgroundImage() {
        BackgroundImage bgImage = new BackgroundImage( new Image( BACKGROUND_FILE_PATH, true),
                                                       BackgroundRepeat.NO_REPEAT,
                                                       BackgroundRepeat.NO_REPEAT,
                                                       BackgroundPosition.DEFAULT,
                                                   new BackgroundSize(1.0, 1.0, true, true, false, false));

        setBackground( new Background( bgImage));
        /**
        Image bgImage = new Image( BACKGROUND_FILE_PATH);
        ImageView bgView = new ImageView( bgImage);
        bgView.fitWidthProperty().bind( this.widthProperty());
        bgView.fitWidthProperty().bind( this.heightProperty());
        bgView.setPreserveRatio(true);

        this.getChildren().add( bgView);

        //setStyle( BACKGROUND_STYLE);
         **/
    }

    private void buttonsPane(Stage stage){
        MainMenuButton newGameButton  = newGameButton(stage);
        MainMenuText   newGameText    = new MainMenuText(382, 404, "NEW GAME");

        MainMenuButton howToButton    = howToButton();
        MainMenuText   howToText      = new MainMenuText  (295, 529, "HOW TO PLAY");

        MainMenuButton settingsButton = settingsButton();
        MainMenuText   settingsText   = new MainMenuText  (424, 654, "SETTINGS");

        MainMenuButton creditsButton  = creditsButton();
        MainMenuText   creditsText    = new MainMenuText  ( 424, 654+125, "CREDITS");

        MainMenuButton exitButton     = exitButton();
        MainMenuText   exitText       = new MainMenuText  ( 548, 654+250, "EXIT");
/**
        bindButtonsToPane( newGameButton, newGameText);
        bindButtonsToPane( howToButton, howToText);
        bindButtonsToPane( settingsButton, settingsText);
        bindButtonsToPane( creditsButton, creditsText);
        bindButtonsToPane( exitButton, exitText);
**/
        Blend overlayBlend = new Blend();
        overlayBlend.setMode( BlendMode.OVERLAY);
        newGameButton.setEffect(overlayBlend);

        Group g = newGameButton.perspectiveSetting( newGameText);

        getChildren().addAll(g, howToButton, settingsButton, creditsButton, exitButton);
        getChildren().addAll(howToText,   settingsText,   creditsText,   exitText);
    }

    private MainMenuButton newGameButton(Stage stage) {
        MainMenuButton newGameButton = new MainMenuButton(locNewGameButton[0], locNewGameButton[1], 1);
        newGameButton.setOnMouseClicked( e -> {
            RiskView gameView = new RiskView( stage);
            Scene newScene = new Scene( gameView);
            stage.setScene( newScene);
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

    private MainMenuButton exitButton() {
        MainMenuButton exitButton = new MainMenuButton( locExit[0], locExit[1], 5);

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