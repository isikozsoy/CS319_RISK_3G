import javafx.beans.binding.ListBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.image.*;

import java.io.*; //exceptions
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

    private List<ClickableTerritory> territoryList;
    private ArrayList<ClickableTerritory> territoriesAlreadyClicked = new ArrayList<>();
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
    private Button backButton;
    private Button placeButton;
    private Text countSelectionText;
    private boolean backButtonIsClicked = false;
    private int selectedTroop = 0;
    private HashMap<ClickableTerritory, StackPane> paneForEachTer;
    private HashMap<ClickableTerritory, Text> textForEachTer;

    private Button nextPhaseButton;

    private Text troopsLeftText;

    private RiskGame riskGame;
    private Territory[] territoriesAsClass;
    private Pane territoryTextPane;

    private Button currPlayerBar;
    private RiskGame.GameMode mode = RiskGame.GameMode.TerAllocationMode;

    public RiskView(Stage stage, ArrayList<Player> playerList, int width, int height) {
        players = playerList;
        territoryList = new ArrayList<>();
        textForEachTer = new HashMap<>();
        territoriesAsClass = new Territory[42];
        nextPhaseButton = new Button();
        this.width = width;
        this.height = height;
        this.stage = stage;

        setRockPaperScissorPane();
        setTroopCountSelector();
        addBackground();
        addPlayerNameBars();
        setTroopsLeft();
        makeClickableMap();
        addNextPhaseButton();

        addPlayButton();

        initiateRiskGame();
    }

    private void addNextPhaseButton() {
        this.getChildren().add(nextPhaseButton);
        //set its graphic as the one in the icons directory
        Image nextPhaseImage = new Image("icons/arrow_icon.png");
        nextPhaseButton.setGraphic( new ImageView(nextPhaseImage));
        //set its background as transparent
        nextPhaseButton.setStyle("-fx-background-color:transparent;");
        //set its location x y coordinates
        //originally its locations are (x, y) : (0, 800) in a 1280-1024 (width-height) space
        //so I adjust its size to fit in with whatever our size is
        //since (0, 0) is (width / 2 , height / 2) in StackPane by default, I also subtract them when setting the location
        nextPhaseButton.setTranslateX( 0 * width / 1280 - width / 2 + nextPhaseImage.getWidth() / 2);
        nextPhaseButton.setTranslateY( 800 * height / 1024 - height / 2);

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

    public void addTroopsLeft(Player currPlayer) {
        currPlayerBar.setStyle("-fx-background-color:" + currPlayer.getColor() + ";" +
                                "-fx-text-fill:white;");
        currPlayerBar.setText(currPlayer.getName());
        currPlayerBar.setFont(Font.font("SNAP ITC", 30));

        troopsLeftText.setText(Integer.toString(currPlayer.getTroopCount()));
    }

    //below is only for the first mode, which is the Territory Allocation Mode
    //After that, it will not be used anymore
    public Territory getClickedTerritory() {
        Territory territoryClicked = null;
        switch (mode) {
            case TerAllocationMode: {
                for( ClickableTerritory clickableTerritory: territoryList) {
                    if( clickableTerritory.getClicked() && !territoriesAlreadyClicked.contains(clickableTerritory)) {
                        territoryClicked = clickableTerritory.getAssociatedTerritory();
                        territoriesAlreadyClicked.add(clickableTerritory);

                        Text territoryText = new Text("1");
                        territoryText.setFont(Font.font("Snap ITC", 20));
                        territoryText.setFill(Color.rgb(255, 255, 255));
                        territoryText.setStroke(Color.ORANGERED);
                        int[] imgLocations = clickableTerritory.getTerritoryXY();

                        textForEachTer.put( clickableTerritory, territoryText);
                        this.getChildren().add(territoryText);

                        //code below changes the location of the texts
                        //their original locations in the (1280, 1024) (width, height) space are held inside the imgLocations array
                        //so I adjust these x and y locations to fit within whatever width and height settings we have for the game
                        //since (0, 0) is (width / 2 , height / 2) in StackPane by default, I also subtract them when setting the location
                        territoryText.setTranslateX(imgLocations[0] * width / 1280 - width / 2);
                        territoryText.setTranslateY(imgLocations[1] * height / 1024 - height / 2);

                        //below are some special cases where the territory center is not the same as the center of the png image
                        //associated with it
                        //the explanations for the functions inside them will be given after the if statements
                        if( clickableTerritory.getAssociatedTerritory().getName().equals( "Japan")) {
                            territoryText.setTranslateX(1165 * width / 1280 - width / 2);
                            territoryText.setTranslateY(380 * height / 1024 - height / 2);
                        }
                        else if( clickableTerritory.getAssociatedTerritory().getName().equals( "Kamchatka")) {
                            territoryText.setTranslateX(1170 * width / 1280 - width / 2);
                            territoryText.setTranslateY(200 * height / 1024 - height / 2);
                        }
                        else if( clickableTerritory.getAssociatedTerritory().getName().equals( "Eastern Australia")) {
                            System.out.println("aa");
                            territoryText.setTranslateX(imgLocations[0] * width / 1280 - width / 2 + 20 * width / 1280);
                        }

                        territoryText.translateXProperty();
                        territoryText.translateYProperty();

                        break;
                    }
                }
                return territoryClicked;
            }
            case SoldierAllocationMode: {
                for( ClickableTerritory clickableTerritory: territoryList) {
                    if (clickableTerritory.getClicked())
                        return clickableTerritory.getAssociatedTerritory();
                }
            }
        }
        return null;
    }

    public Territory getClickedTerritoryAfterAllocation() {
        Territory territoryClicked = null;
        for( ClickableTerritory clickableTerritory: territoryList) {
            if( clickableTerritory.getClicked()) {
                territoryClicked = clickableTerritory.getAssociatedTerritory();
                clickableTerritory.setClicked(false);
                break;
            }
        }

        return territoryClicked;
    }

    public boolean backButtonIsClicked() {
        return backButtonIsClicked;
    }

    public void initiateRiskGame() {
        riskGame = new RiskGame(players, territoriesAsClass, this);
        riskGame.play();
    }

    public void setTerritoryMode(RiskGame.GameMode mode) {
        if(mode == RiskGame.GameMode.SoldierAllocationMode) {
            for( ClickableTerritory clickableTerritory: territoryList)
                clickableTerritory.setClicked(false);
        }
        this.mode = mode;
        for( ClickableTerritory clickableTerritory: territoryList) {
            clickableTerritory.setMode(mode);
        }
    }

    public void setTerritoryClicked( boolean clicked) {
        for( ClickableTerritory clickableTerritory: territoryList) {
            clickableTerritory.setClicked(clicked);
        }
    }

    public void setTerritoryColor( String color) {
        for( ClickableTerritory clickableTerritory: territoryList) {
            clickableTerritory.setColor(color);
        }
    }

    public void addTroopCountSelector( int troopCount) {
        this.getChildren().add(troopCountSelectionPane);
        countSelectionText.setText("1");
        lessButton.setOnMouseClicked(e -> {
            int selectedTroopCount = Integer.valueOf(countSelectionText.getText());
            //circles around 1-troopcount
            if(selectedTroopCount > 1)
                countSelectionText.setText(String.valueOf(selectedTroopCount - 1));
            else if( selectedTroopCount == 1) {
                countSelectionText.setText(String.valueOf(troopCount));
            }
        });

        moreButton.setOnMouseClicked(e -> {
            int selectedTroopCount = Integer.valueOf(countSelectionText.getText());
            if(selectedTroopCount < troopCount)
                countSelectionText.setText(String.valueOf(selectedTroopCount + 1));
            else if( selectedTroopCount == troopCount)
                countSelectionText.setText(String.valueOf(1));
        });

        /**
        backButton.setOnMouseClicked(e -> {
            backButtonIsClicked = true;
            removeTroopCountSelector();
        });

        placeButton.setOnMouseClicked(e -> {
            selectedTroop = Integer.valueOf(countSelectionText.getText());
        });
         **/
    }

    public void setMaxCountSelection( int troopCount) {
        countSelectionText.setText(Integer.toString(troopCount));
        this.getChildren().remove(troopCountSelectionPane);
        addTroopCountSelector( troopCount);
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getPlaceButton() {
        return placeButton;
    }

    public int getSelectedTroop() {
        selectedTroop = Integer.valueOf(countSelectionText.getText());
        return selectedTroop;
    }

    public void setSelectedTroop( int selectedTroop) {
        this.selectedTroop = selectedTroop;
    }

    public void removeTroopCountSelector() {
        this.getChildren().remove(troopCountSelectionPane);
    }

    private void setTroopCountSelector() {
        troopCountSelectionPane = new VBox();
        lessButton = new Button();
        moreButton = new Button();
        placeButton = new Button("place");
        backButton = new Button("back");
        countSelectionText = new Text("1");

        lessButton.setGraphic(new ImageView(new Image("icons/less_icon.png")));
        moreButton.setGraphic(new ImageView((new Image("icons/more_icon.png"))));
        HBox hbox = new HBox();
        hbox.getChildren().add(lessButton);
        hbox.getChildren().add(countSelectionText);
        hbox.getChildren().add(moreButton);
        hbox.getChildren().add(backButton);
        hbox.getChildren().add(placeButton);

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
        ImageView bgImage = new ImageView( new Image( DIRECTORY_NAME + BACKGROUND_IMG_PATH, true));
        bindMapToPaneSize(bgImage);
        this.getChildren().add( bgImage);
    }

    private void makeClickableMap() {
        for (int i = 0; i < territories.length; i++) {
            Territory territory = new Territory(territories[i]);

            ClickableTerritory clickableTerritory = new ClickableTerritory(territories[i],
                    DIRECTORY_NAME + territories[i] + FILE_NAME_HELPER,
                    territory);
            bindMapToPaneSize(clickableTerritory);

            territoryList.add(clickableTerritory);

            territoriesAsClass[i] = territory;

            this.getChildren().add(clickableTerritory);
        }
    }

    private void bindMapToPaneSize( ImageView imageView) {
        imageView.fitWidthProperty().bind( this.widthProperty());
        imageView.fitHeightProperty().bind( this.heightProperty());
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
        playButton.setOnMousePressed( e -> {
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

    public void addPlayerNameBars() {
        FlowPane nameBarPane = new FlowPane();
        nameBarPane.setHgap(10);
        nameBarPane.setVgap(10);
        for( Player player: players) {
            Button nameButton = new Button();
            nameButton.setStyle("-fx-background-color: " + player.getColor() + ";" +
                    "-fx-text-fill: white;-fx-background-radius:20;");
            nameButton.setPrefSize(width / players.size() - 40, 50);
            nameButton.setText(player.getName());
            if(players.size() <= 4)
                nameButton.setFont(Font.font("Snap ITC", 30));
            else
                nameButton.setFont(Font.font("Snap ITC", 30*4/(players.size())));
            nameButton.setOnMouseClicked(e -> {
                System.out.println("Clicked");
            });
            nameBarPane.getChildren().add(nameButton);
        }
        nameBarPane.setAlignment(Pos.BOTTOM_RIGHT);
        this.getChildren().add(nameBarPane);
    }
}