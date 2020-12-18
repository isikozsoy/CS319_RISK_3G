import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.image.*;

import java.util.ArrayList;
import java.util.HashMap;
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
    private CardExchangePane cardExchangePane;
    private AllianceRequestPane allianceRequestPane;
    private AlliancePane alliancePane;
    private Button lessButton;
    private Button moreButton;
    private Button backButton;
    private Button placeButton;
    private Button buildAirportButton;
    private Button cardsButton;
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
        setCardExchangePane();
        setAllianceRequestPane();
        addBackground();

        setTroopsLeft();
        makeClickableMap();
        addPlayerNameBars();

        addNextPhaseButton();

        addPlayButton();

        initiateRiskGame();
    }

    private void setCardExchangePane() {
        cardExchangePane = new CardExchangePane();
    }

    private void setAllianceRequestPane() { allianceRequestPane = new AllianceRequestPane(); }

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

    public void setCardExchangeInfo(Player player) {
        cardExchangePane.setPlayerCards(player);
    }

    public void addTroopsLeft(Player currPlayer) {
        currPlayerBar.setStyle("-fx-background-color:" + currPlayer.getColor() + ";" +
                                "-fx-text-fill:white;");
        currPlayerBar.setText(currPlayer.getName());
        currPlayerBar.setFont(Font.font("SNAP ITC", 30));

        troopsLeftText.setText(Integer.toString(currPlayer.getTroopCount()));
    }

    public void updateTroopsCount(Player curPlayer) {
        troopsLeftText.setText(Integer.toString(curPlayer.getTroopCount()));
    }

    public  void updatePlayerBar (Player curPlayer) {
        currPlayerBar.setStyle("-fx-background-color:" + curPlayer.getColor() + ";" +
                "-fx-text-fill:white;");
        currPlayerBar.setText(curPlayer.getName());
        currPlayerBar.setFont(Font.font("SNAP ITC", 30));
    }

    public void updateTerTroopCount(ClickableTerritory clickableTerritory, int count) {
        Text terText = textForEachTer.get(clickableTerritory);
        terText.setText(String.valueOf(Integer.valueOf(terText.getText()) + count));
    }

    //below is only for the first mode, which is the Territory Allocation Mode
    //After that, it will not be used anymore
    public ClickableTerritory getClickableTerritory() {
        ClickableTerritory clickableTerritory = null;
        switch (mode) {
            case TerAllocationMode: {
                for( ClickableTerritory ct: territoryList) {
                    if( ct.getClicked() && !territoriesAlreadyClicked.contains(ct)) {
                        clickableTerritory = ct;
                        territoriesAlreadyClicked.add(ct);

                        Text territoryText = new Text("1");
                        territoryText.setFont(Font.font("Snap ITC", 20));
                        territoryText.setFill(Color.rgb(255, 255, 255));
                        territoryText.setStroke(Color.ORANGERED);
                        int[] imgLocations = ct.getTerritoryXY();

                        textForEachTer.put( ct, territoryText);
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
                        if( ct.getAssociatedTerritory().getName().equals( "Japan")) {
                            territoryText.setTranslateX(1165 * width / 1280 - width / 2);
                            territoryText.setTranslateY(380 * height / 1024 - height / 2);
                        }
                        else if( ct.getAssociatedTerritory().getName().equals( "Kamchatka")) {
                            territoryText.setTranslateX(1170 * width / 1280 - width / 2);
                            territoryText.setTranslateY(200 * height / 1024 - height / 2);
                        }
                        else if( ct.getAssociatedTerritory().getName().equals( "Eastern Australia")) {
                            System.out.println("aa");
                            territoryText.setTranslateX(imgLocations[0] * width / 1280 - width / 2 + 20 * width / 1280);
                        }

                        territoryText.translateXProperty();
                        territoryText.translateYProperty();

                        break;
                    }
                }
                return clickableTerritory;
            }
            case SoldierAllocationMode: {
                for( ClickableTerritory ct: territoryList) {
                    if( ct.getClicked()) {
                        clickableTerritory = ct;
                        ct.setClicked(false);
                        break;
                    }
                }
                return clickableTerritory;
            }
        }
        return clickableTerritory;
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

    public void addCardExchangePane() {
        this.getChildren().add(cardExchangePane);
        cardExchangePane.setAlignment(Pos.CENTER);
        cardExchangePane.getBackButton().setOnMouseClicked(e -> {
            if(mode == RiskGame.GameMode.SoldierAllocationMode) {
                updateTroopsCount(cardExchangePane.getCurPlayer());
            }
            this.getChildren().remove(cardExchangePane);
        });
    }

    public void setAllianceRequestInfo(Player player) {
        allianceRequestPane.setAllianceRequests(player);
    }

    public void addAllianceRequestPane(Player curPlayer) {
        this.getChildren().add(allianceRequestPane);
        allianceRequestPane.setAlignment(Pos.CENTER);
        List<AllianceRequestPane.AllianceRequest> list = allianceRequestPane.getRequestsElements();
        for (AllianceRequestPane.AllianceRequest element : list) {
            element.getAcceptButton().setOnMouseClicked( e -> {
                riskGame.getPlayers().get(element.getElementId()).addAlly(curPlayer.getId());
                curPlayer.addAlly(element.getElementId());
                element.removeButtons();
                if(allianceRequestPane.decreaseRequestCount() == 0) {
                    this.getChildren().remove(allianceRequestPane);
                }
            });

            element.getIgnoreButton().setOnMouseClicked( e -> {
                element.removeButtons();
                allianceRequestPane.decreaseRequestCount();
                if(allianceRequestPane.getRequestCount() == 0) {
                    this.getChildren().remove(allianceRequestPane);
                }
            });
        }
    }

    public void addTroopCountSelector( int troopCount) {
        this.getChildren().add(troopCountSelectionPane);
        countSelectionText.setText("   1  ");
        lessButton.setOnMouseClicked(e -> {
            int selectedTroopCount = Integer.valueOf(countSelectionText.getText().trim());
            String nextCount = "";
            //circles around 1-troopcount
            if(selectedTroopCount > 1)
                nextCount = String.valueOf(selectedTroopCount - 1);
            else if( selectedTroopCount == 1) {
                nextCount = String.valueOf(troopCount);
            }

            if(nextCount.length() == 1)
                nextCount = "   " + nextCount + "  ";
            else if(nextCount.length() == 2)
                nextCount = "  " + nextCount + "  ";
            countSelectionText.setText(nextCount);
        });

        moreButton.setOnMouseClicked(e -> {
            int selectedTroopCount = Integer.valueOf(countSelectionText.getText().trim());
            String nextCount = "";
            if(selectedTroopCount < troopCount)
                nextCount = String.valueOf(selectedTroopCount + 1);
            else if( selectedTroopCount == troopCount)
                nextCount = "1";

            if(nextCount.length() == 1)
                nextCount = "   " + nextCount + "  ";
            else if(nextCount.length() == 2)
                nextCount = "  " + nextCount + "  ";
            countSelectionText.setText(nextCount);
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

    public Button getBuildAirportButton() {
        return buildAirportButton;
    }

    public Button getCardsButton() {
        return cardsButton;
    }

    public int getSelectedTroop() {
        selectedTroop = Integer.valueOf(countSelectionText.getText().trim());
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

        Text textBack = new Text("Back");
        textBack.setFont(Font.font("Snap ITC", 30));
        backButton = new Button("Back");

        Text textPlace = new Text("Place");
        textPlace.setFont(Font.font("Snap ITC", 30));
        placeButton = new Button("Place");

        //Text textBuildAirport = new Text("Build Airport");
        //textBuildAirport.setFont(Font.font("Snap ITC", 30));
        buildAirportButton = new Button("Build Airport");
        countSelectionText = new Text("1");
        countSelectionText.setFont(Font.font("Snap ITC", 50));

        lessButton.setGraphic(new ImageView(new Image("icons/less_icon.png")));
        moreButton.setGraphic(new ImageView((new Image("icons/more_icon.png"))));
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();

        //hbox.getChildren().addAll(new ImageView(new Image("icons/troop_icon.png")));
        hbox1.getChildren().addAll( lessButton, countSelectionText, moreButton);
        hbox1.setAlignment(Pos.CENTER);

        hbox2.getChildren().addAll(backButton, placeButton, buildAirportButton);
        hbox2.setAlignment(Pos.CENTER);

        //troopCountSelectionPane.getChildren().addAll(new Image("icons/troop_icon.png"), hbox);
        troopCountSelectionPane.getChildren().addAll(new ImageView(new Image("icons/troop_icon.png")), hbox1, hbox2);
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

    public void addAlliancePane(boolean isAlly,  Player target) { //method to add the alliance pane
        // whenever a player is clicked
        alliancePane = new AlliancePane();
        this.getChildren().add(alliancePane);
        alliancePane.setAlignment(Pos.CENTER);

        if(!isAlly) {
            if (!target.getAllianceReq().containsKey(riskGame.getPlayers().get(riskGame.getCurPlayerId()).getId()))
                alliancePane.addSendAllianceButton(); //send alliance button is added to the pane
            // if the target player is not an ally of the source player
            // and the player has not been sent a request by the same player
        }
        else
            alliancePane.addCancelAllianceButton(); //if the players are allies, cancel alliance
        // button is added to the pane

        alliancePane.getSendAllianceRequestButton().setOnMouseClicked(e->{
            //alliance request is sent to the target player
            riskGame.sendAllianceRequest(target);
            this.getChildren().remove(alliancePane); //returned back to the game
        });

        alliancePane.getCancelAllianceButton().setOnMouseClicked(e->{
            //alliance is cancelled
            //riskGame.cancelAlliance(riskGame.getCurPlayerId(), target);
            //this.getChildren().remove(alliancePane); //returned back to the game
        });

        alliancePane.getBackButton().setOnMouseClicked(e -> {
            this.getChildren().remove(alliancePane); //returned back to the game
        });
    }

    public void addPlayerNameBars() {
        FlowPane nameBarPane = new FlowPane();
        nameBarPane.setHgap(10);
        nameBarPane.setVgap(10);
        cardsButton = new Button();
        ImageView cardsImg = new ImageView(new Image("icons/cards_icon.png"));
        cardsImg.setFitWidth(30);
        cardsImg.setFitHeight(40);
        cardsButton.setGraphic(cardsImg);
        nameBarPane.getChildren().add(cardsButton);
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

            //IMPLEMENTED FOR ALLIANCE PANE
            nameButton.setOnMouseClicked(e -> {
                //this long if statement checks if the player clicked is NOT the same player, who clicked the button
                // and the phases of the game
                if(riskGame.getCurPlayerId() != player.getId() &&
                        (mode == RiskGame.GameMode.TerAllocationMode
                                || mode == RiskGame.GameMode.SoldierAllocationMode
                                || mode == RiskGame.GameMode.SoldierAllocationModeContinued
                                || mode == RiskGame.GameMode.AttackMode || mode == RiskGame.GameMode.FortifyMode))
                    //alliance pane is added
                    this.addAlliancePane(riskGame.getPlayers().get(riskGame.getCurPlayerId()).isAlly(player), player);
            });
            /////////////////////////////////////////////////////////////////////////////
            nameBarPane.getChildren().add(nameButton);
        }
        nameBarPane.setMaxHeight(0);
        nameBarPane.setMaxWidth(1000);
        this.getChildren().add(nameBarPane);
        setAlignment(nameBarPane, Pos.BOTTOM_CENTER);
    }
}