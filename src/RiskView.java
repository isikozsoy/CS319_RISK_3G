import javafx.beans.binding.ListBinding;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import org.jetbrains.annotations.NotNull;

//import java.awt.event.MouseEvent;
import java.io.*; //exceptions
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class RiskView extends StackPane {
    final String DIRECTORY_NAME = "/img/";
    final String FILE_NAME_HELPER = "_bw.png";
    final String[] territories = {"Alaska", "Northwest Territory", "Greenland", "Alberta", "Ontario", "Quebec",
            "West America", "East America", "Central America", "Venezuela", "Peru", "Brazil", "Argentina",
            "North Africa", "Egypt", "East Africa", "Congo", "South Africa", "Madagascar",
            "Iceland", "Scandinavia", "Ukraine", "Britain", "NE", "SE", "WE",
            "Indonesia", "New Guinea", "Western Australia", "Eastern Australia",
            "Siam", "India", "China", "Mongolia", "Japan", "Irkutsk", "Yakutsk", "Kamchatka", "Siberia",
            "Afghanistan", "Ural", "Middle East"
    };
    final String BACKGROUND_IMG_PATH = "background_image_bw.png";

    private ArrayList<ClickableTerritory> territoryList;
    private Button playButton;
    private int clickedOnPlay = 0;
    private ArrayList<Player> players;
    private int width;
    private int height;
    private GridPane rockPaperScissorPane;
    private Stage stage;
    private VBox troopCountSelectionPane;
    private Button lessButton;
    private Button moreButton;
    private Text countSelectionText;
    private HashMap<ClickableTerritory, Text> textForEachTer;

    private Button nextPhaseButton;

    private Text troopsLeftText;

    private RiskGame riskGame;
    private Pane territoryTextPane;

    private Button currPlayerBar;

    private boolean attackFlag;
    private ClickableTerritory source;
    private RiskGame.GameMode mode;

    public RiskView(Stage stage, ArrayList<Player> playerList, int width, int height) {
        this.riskGame = new RiskGame(playerList, this);
        territoryList = new ArrayList<>();
        textForEachTer = new HashMap<>();
        this.players = playerList;
        nextPhaseButton = new Button();
        this.width = width;
        this.height = height;
        this.stage = stage;
        mode = RiskGame.GameMode.TroopAllocationMode;

        setRockPaperScissorPane();
        setTroopCountSelector();
        addBackground();
        addPlayerNameBars();
        setTroopsLeft();
        makeClickableMap();
        addNextPhaseButton();
        addPlayButton();

        territoryAllocation();


    }


    public void attackPhase() {
        for (int i = 0; i < territoryList.size(); i++) {

            ClickableTerritory clickableTerritory = territoryList.get(i);
            clickableTerritory.setDisable(false);
            final int territoryId = i;

            clickableTerritory.setOnMousePressed((new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    if (riskGame.getTerritoryOwnerId(territoryId) == riskGame.getCurPlayerId()) {
                        //Source territory is clicked.

                        //To not search for attackable territories again,
                        //when the user click on the same source.
                        clickableTerritory.setDisable(true);

                        if (source != null) {
                            source.setColor(players.get(riskGame.getCurPlayerId()).getColor());
                            source.setDisable(false);
                        }
                        //clickableTerritory.setEffect(shadowAndColorBlend);
                        clickableTerritory.setColor("aqua");
                        source = clickableTerritory;
                        riskGame.loadAttackableTer(source.getTerritoryId());

                    } else if (source != null && riskGame.isAttackable(clickableTerritory.getTerritoryId())) {
                        startAttack(source, clickableTerritory);
                    }
                }

            }));
        }

    }

    private void addNextPhaseButton() {
        this.getChildren().add(nextPhaseButton);
        //set its graphic as the one in the icons directory
        Image nextPhaseImage = new Image("icons/next_phase_icon.png");
        nextPhaseButton.setGraphic(new ImageView(nextPhaseImage));
        //set its background as transparent
        nextPhaseButton.setStyle("-fx-background-color:transparent;");
        //set its location x y coordinates
        //originally its locations are (x, y) : (0, 800) in a 1280-1024 (width-height) space
        //so I adjust its size to fit in with whatever our size is
        //since (0, 0) is (width / 2 , height / 2) in StackPane by default, I also subtract them when setting the location
        nextPhaseButton.setTranslateX(0 * width / 1280 - width / 2 + nextPhaseImage.getWidth() / 2);
        nextPhaseButton.setTranslateY(800 * height / 1024 - height / 2);

        nextPhaseButton.translateXProperty();
        nextPhaseButton.translateYProperty();
    }

    private void setTroopsLeft() {
        FlowPane flowPane = new FlowPane();
        troopsLeftText = new Text();
        currPlayerBar = new Button(); //change it based on the current player
        currPlayerBar.setPrefSize(200, 100);

        VBox vbox = new VBox();
        vbox.getChildren().add(new ImageView(new Image("icons/troop_icon.png")));
        vbox.getChildren().add(troopsLeftText);
        vbox.setAlignment(Pos.TOP_RIGHT);

        flowPane.getChildren().add(currPlayerBar);
        flowPane.getChildren().add(vbox);

        troopsLeftText.setFont(Font.font("SNAP ITC", 30));

        setAlignment(flowPane, Pos.TOP_LEFT);
        setAlignment(troopsLeftText, Pos.CENTER);
        this.getChildren().add(flowPane);
    }

    public void addTroopsLeft(@NotNull Player currPlayer) {
        currPlayerBar.setStyle("-fx-background-color:" + currPlayer.getColor() + ";" +
                "-fx-text-fill:white;");
        currPlayerBar.setText(currPlayer.getName());
        currPlayerBar.setFont(Font.font("SNAP ITC", 30));

        troopsLeftText.setText(Integer.toString(currPlayer.getTroopCount()));
    }


    public void setTerritoryMode(RiskGame.GameMode mode) {
        this.mode = mode;
/*        for( ClickableTerritory clickableTerritory: territoryList) {
            clickableTerritory.setMode(mode);
        }*/
    }

    public void setTerritoryClicked(boolean clicked) {
        for (ClickableTerritory clickableTerritory : territoryList) {
            clickableTerritory.setClicked(clicked);
        }
    }

    public void setTerritoryColor(String color, int territoryId) {
        territoryList.get(territoryId).setColor(color);
    }

    public void addTroopCountSelector(int troopCount) {
        this.getChildren().add(troopCountSelectionPane);

        lessButton.setOnMouseClicked(e -> {

        });

        moreButton.setOnMouseClicked(e -> {

        });
    }

    public void removeTroopCountSelector() {
        this.getChildren().remove(troopCountSelectionPane);
    }

    private void setTroopCountSelector() {
        troopCountSelectionPane = new VBox();
        lessButton = new Button();
        moreButton = new Button();
        countSelectionText = new Text("1");

        lessButton.setGraphic(new ImageView(new Image("icons/less_icon.png")));
        moreButton.setGraphic(new ImageView((new Image("icons/more_icon.png"))));
        HBox hbox = new HBox();
        hbox.getChildren().add(lessButton);
        hbox.getChildren().add(countSelectionText);
        hbox.getChildren().add(moreButton);

        troopCountSelectionPane.getChildren().addAll(new ImageView(new Image("icons/troop_icon.png")), hbox);
        troopCountSelectionPane.setAlignment(Pos.CENTER);
    }

    private void setRockPaperScissorPane() {
        rockPaperScissorPane = new GridPane();

        rockPaperScissorPane.add(new ImageView(new Image("icons/rock_rps.png")), 0, 0);
        rockPaperScissorPane.add(new ImageView(new Image("icons/paper_rps.png")), 1, 0);
        rockPaperScissorPane.add(new ImageView(new Image("icons/scissors_rps.png")), 2, 0);

        Text textA = new Text("A");
        textA.setFont(Font.font("Snap ITC", 30));
        Text textS = new Text("S");
        textS.setFont(Font.font("Snap ITC", 30));
        Text textD = new Text("D");
        textD.setFont(Font.font("Snap ITC", 30));
        rockPaperScissorPane.add(textA, 0, 1);
        rockPaperScissorPane.add(textS, 1, 1);
        rockPaperScissorPane.add(textD, 2, 1);

        rockPaperScissorPane.setAlignment(Pos.CENTER);
    }

    private void addBackground() {
        ImageView bgImage = new ImageView(new Image(DIRECTORY_NAME + BACKGROUND_IMG_PATH, true));
        bindMapToPaneSize(bgImage);
        this.getChildren().add(bgImage);
    }

    private void makeClickableMap() {
        for (int i = 0; i < territories.length; i++) {

            ClickableTerritory clickableTerritory = new ClickableTerritory(territories[i],
                    DIRECTORY_NAME + territories[i] + FILE_NAME_HELPER, i);
            bindMapToPaneSize(clickableTerritory);

            territoryList.add(clickableTerritory);


            this.getChildren().add(clickableTerritory);
        }
    }

    private void bindMapToPaneSize(ImageView imageView) {
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());
    }

    private void addPlayButton() {
        playButton = new Button("play");
        playButton.setLayoutX(500);
        playButton.setLayoutY(20);
        addRPSView();
        this.getChildren().add(playButton);
        setAlignment(playButton, Pos.TOP_RIGHT);
    }

    private void addRPSView() {
        playButton.setOnMousePressed(e -> {
            this.getChildren().remove(playButton);
            playButton = new Button("play");
            playButton.setLayoutX(500);
            playButton.setLayoutY(20);
            setAlignment(playButton, Pos.TOP_RIGHT);

            disableAllClickableTer();
            this.getChildren().add(rockPaperScissorPane);
            this.getChildren().add(playButton);

            removeRPSView();
            //rpsGameRoot.execute(this);
        });
    }

    private void removeRPSView() {
        playButton.setOnMousePressed(e -> {
            this.getChildren().remove(rockPaperScissorPane);
            enableAllClickableTer();
            addRPSView();
        });
    }

    public void disableAllClickableTer() {
        for (int i = 0; i < territories.length; i++)
            (territoryList.get(i)).removeEventListeners();
    }

    public void enableAllClickableTer() {
        for (int i = 0; i < territories.length; i++)
            (territoryList.get(i)).addEventListeners();
    }

    public void territoryAllocation() {
        for (int i = 0; i < territoryList.size(); i++) {
            ClickableTerritory clickableTerritory = territoryList.get(i);
            final int territoryId = i;
/*            clickableTerritory.setOnMousePressed((new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {*/
            clickableTerritory.setDisable(true);
            riskGame.startTerAlloc(territoryId);

            Text territoryText = new Text("5");
            territoryText.setFont(Font.font("Snap ITC", 20));
            territoryText.setFill(Color.rgb(255, 255, 255));
            territoryText.setStroke(Color.ORANGERED);

            int[] imgLocations = clickableTerritory.getTerritoryXY();

            textForEachTer.put(clickableTerritory, territoryText);

            RiskView.this.getChildren().add(territoryText);

            //code below changes the location of the texts
            //their original locations in the (1280, 1024) (width, height) space are held inside the imgLocations array
            //so I adjust these x and y locations to fit within whatever width and height settings we have for the game
            //since (0, 0) is (width / 2 , height / 2) in StackPane by default, I also subtract them when setting the location
            territoryText.setTranslateX(imgLocations[0] * width / 1280 - width / 2);
            territoryText.setTranslateY(imgLocations[1] * height / 1024 - height / 2);

            //below are some special cases where the territory center is not the same as the center of the png image
            //associated with it
            //the explanations for the functions inside them will be given after the if statements
            if (clickableTerritory.getTerritoryName().equals("Japan")) {
                territoryText.setTranslateX(1165 * width / 1280 - width / 2);
                territoryText.setTranslateY(380 * height / 1024 - height / 2);
            } else if (clickableTerritory.getTerritoryName().equals("Kamchatka")) {
                territoryText.setTranslateX(1170 * width / 1280 - width / 2);
                territoryText.setTranslateY(200 * height / 1024 - height / 2);
            } else if (clickableTerritory.getTerritoryName().equals("Eastern Australia")) {
                territoryText.setTranslateX(imgLocations[0] * width / 1280 - width / 2 + 20 * width / 1280);
            }

            territoryText.translateXProperty();
            territoryText.translateYProperty();

            if (RiskView.this.mode == RiskGame.GameMode.AttackMode) {
                attackPhase();
            }

/*                    clickableTerritory.removeEventHandler(MouseEvent.ANY, this);
                }
            }));*/

        }

    }


    public void addPlayerNameBars() {
        FlowPane nameBarPane = new FlowPane();
        nameBarPane.setHgap(10);
        nameBarPane.setVgap(10);
        for (Player player : players) {
            Button nameButton = new Button();
            nameButton.setStyle("-fx-background-color: " + player.getColor() + ";" +
                    "-fx-text-fill: white;-fx-background-radius:20;");
            nameButton.setPrefSize(width / players.size() - 40, 50);
            nameButton.setText(player.getName());
            if (players.size() <= 4)
                nameButton.setFont(Font.font("Snap ITC", 30));
            else
                nameButton.setFont(Font.font("Snap ITC", 30 * 4 / (players.size())));
            nameButton.setOnMouseClicked(e -> {
                System.out.println("Clicked");
            });
            nameBarPane.getChildren().add(nameButton);
        }
        nameBarPane.setAlignment(Pos.BOTTOM_RIGHT);
        this.getChildren().add(nameBarPane);
    }

    public void startAttack(ClickableTerritory source, ClickableTerritory target) {


        TextField textField = new TextField();
        textField.setMaxSize(width / 8, 40);
        textField.setFont(Font.font("Snap ITC", 30));
        Button attackButton = new Button("Attack!");
        Button cancelAttackButton = new Button("Cancel");
        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(attackButton, cancelAttackButton);
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(textField, buttonBox);
        this.getChildren().add(vbox);

        attackButton.setOnMouseClicked(e -> {
            if (!textField.getText().isEmpty()) {
                int troopCount = Integer.parseInt(textField.getText());

                int[] newTroopCounts = new int[2];

                int result = riskGame.startAttack(source.getTerritoryId(), target.getTerritoryId(), troopCount,
                        newTroopCounts);

                //Game not played.
                if (result == -1) {
                    return;
                }

                Text targetTerritoryText = textForEachTer.get(target);
                Text sourceTerritoryText = textForEachTer.get(source);

                this.getChildren().remove(sourceTerritoryText);
                sourceTerritoryText.setText("" + newTroopCounts[0]);
                this.getChildren().add(sourceTerritoryText);

                this.getChildren().remove(targetTerritoryText);
                targetTerritoryText.setText("" + newTroopCounts[1]);
                this.getChildren().add(targetTerritoryText);

                //The owner of the source won.
                if (result == 0) {
                    target.setColor(players.get(riskGame.getCurPlayerId()).getColor());
                }
            }
            this.getChildren().remove(vbox);
        });


        cancelAttackButton.setOnMouseClicked(e -> {
            this.getChildren().remove(vbox);

        });

    }
}