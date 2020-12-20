import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.Button;
import java.awt.*;

public class MainMenuView extends BeforeGameView {
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
    final private String info             = "Annexation is a game that can be played with 2-6 players.\n" +
            "The objective of the game is to be the player that captures all\n" +
            "the territories on the board and achieve global domination.\n" +
            "Plays revolves with players taking turns one after another. The game\n" +
            "starts with the territory allocation step. In this step, each player\n" +
            "places a single troop one by one on unclaimed territories\n" +
            "until all territories have been claimed. Afterwards players place\n" +
            "their remaining troops on their territories and play begins\n" +
            "with the turn of the first player. Turns consist of three phases;\n" +
            "the soldier allocation, attack, and fortify phase. During the soldier allocation\n" +
            "phase, a player places the soldiers they have received that turn onto\n" +
            "their territories in any way that they wish. During the attack phase\n" +
            "players can attack other players’ territories to capture them, and during\n" +
            "the fortify phase a player may move several soldiers from one of their\n" +
            "territories to any other connected territory. During play, a player is eliminated\n" +
            "control of all their territories and a player wins the game once they lose\n" +
            "when all other players are eliminated.\n\n" +
            "For detailed-visualized manual regarding Annexation, please check out\n" +
            "our GitHub repository:\n" +
            "https://github.com/isikozsoy/CS319_RISK_3G\n\n\n";

    private final String styleBackground = "-fx-background-color: #ff6666;" +
            "-fx-border-color: #00ccff";


    Dimension screenSize;
    private int width;
    private int height;
    private VBox credits;

    MainMenuView( Stage stage, int width, int height) {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        buttonsPane( stage);
        this.width = width;
        this.height = height;
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
            AddPlayersView addPlayersView = new AddPlayersView( stage, width, height);
            Scene addPlayerScene = new Scene( addPlayersView, width, height);

            stage.setScene( addPlayerScene);
            //stage.show();
        });

        return newGameButton;
    }

    private MainMenuButton howToButton() {
        MainMenuButton howToButton = new MainMenuButton(locHowTo[0], locHowTo[1], 2);
        VBox infoBox = new VBox();
        Label infoLabel = new Label();
        Button back = new Button("Back");

        infoLabel.setText(info);
        infoLabel.setFont(Font.font("Snap ITC", 15));
        infoLabel.setStyle(styleBackground);

        back.setStyle(styleBackground);
        back.setFont(Font.font("Snap ITC", 30));
        back.setMaxSize(300,75);

        javafx.scene.image.ImageView backImg = new ImageView(new Image("icons/back_arrow_icon.png"));
        backImg.setFitHeight(50);
        backImg.setFitWidth(50);
        back.setGraphic(backImg);

        infoBox.getChildren().addAll(infoLabel, back);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setMaxSize(800,800);

        back.setOnMouseClicked(e -> {
            this.getChildren().removeAll(infoBox);
        });

        howToButton.setOnMouseClicked(e -> {
            this.getChildren().add(infoBox);
        });

        return howToButton;
    }

    private MainMenuButton settingsButton() {
        MainMenuButton settingsButton = new MainMenuButton(locSettings[0], locSettings[1], 3);
/**
        VBox sizeButtons = new VBox();
        Label currentSize = new Label("Default Size: 1000 x 750");
        currentSize.setStyle("-fx-background-color: #ffc588;" +
                "-fx-border-color: #0d5873");
        currentSize.setFont(Font.font("Snap ITC", 20));
        sizeButtons.getChildren().add(currentSize);
        String[] sizes = {"1400 x 1050", "1280 x 800", "1680 x 1050", "1280 x 1024", "1600 x 1200", "1920 x 1200"};
        for (int i = 0; i < sizes.length; i++) {
            Button option = new Button(sizes[i]);
            String info = sizes[i];
            option.setStyle(styleBackground);
            option.setFont(Font.font("Snap ITC", 30));
            option.setMaxSize(300,75);
            sizeButtons.getChildren().add(option);
            option.setOnMouseClicked(e -> {
                width = Integer.valueOf(info.substring(0, 4));
                height = Integer.valueOf(info.substring(7));
                currentSize.setText("Current Size: " + info);
            });
        }

        Button fullScreen = new Button("FULL SCREEN");
        fullScreen.setStyle(styleBackground);
        fullScreen.setFont(Font.font("Snap ITC", 30));
        fullScreen.setMaxSize(300,75);
        fullScreen.setOnMouseClicked(e -> {

        });

        Button back = new Button("Back");
        back.setStyle(styleBackground);
        back.setFont(Font.font("Snap ITC", 30));
        back.setMaxSize(300,75);

        javafx.scene.image.ImageView backImg = new ImageView(new Image("icons/back_arrow_icon.png"));
        backImg.setFitHeight(50);
        backImg.setFitWidth(50);
        back.setGraphic(backImg);
        sizeButtons.getChildren().add(back);

        settingsButton.setOnMouseClicked(e -> {
            sizeButtons.setAlignment(Pos.CENTER);
            this.getChildren().add(sizeButtons);
        });

        back.setOnMouseClicked(e->{
            this.getChildren().remove(sizeButtons);
        });
*/
        return settingsButton;
    }

    private MainMenuButton creditsButton() {
        MainMenuButton creditsButton = new MainMenuButton(locCredits[0], locCredits[1], 4);

        Label alp = new Label("Alp Üneri");
        Label burak = new Label("Burak Yetiştiren");
        Label isik = new Label("Işık Özsoy");
        Label defne = new Label("Defne Betül Çiftçi");
        Label hakan = new Label("Hakan Kara");
        Label creditsText = new Label("Credits:");

        Button back = new Button("Back");

        alp.setFont(Font.font("Snap ITC", 30));
        burak.setFont(Font.font("Snap ITC", 30));
        isik.setFont(Font.font("Snap ITC", 30));
        defne.setFont(Font.font("Snap ITC", 30));
        hakan.setFont(Font.font("Snap ITC", 30));
        creditsText.setFont(Font.font("Snap ITC", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 30));
        creditsText.setUnderline(true);

        back.setStyle(styleBackground);
        back.setFont(Font.font("Snap ITC", 30));
        back.setMaxSize(300,75);

        javafx.scene.image.ImageView backImg = new ImageView(new Image("icons/back_arrow_icon.png"));
        backImg.setFitHeight(50);
        backImg.setFitWidth(50);
        back.setGraphic(backImg);

        creditsButton.setOnMouseClicked(e->{
            credits = new VBox();
            credits.getChildren().addAll( creditsText, alp, burak, isik, defne, hakan, back);

            credits.setAlignment(Pos.CENTER);
            credits.setStyle(styleBackground);

            credits.setMaxSize(300,300);
            this.getChildren().add(credits);

        });

        back.setOnMouseClicked(e->{
            this.getChildren().removeAll(credits);
        });

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