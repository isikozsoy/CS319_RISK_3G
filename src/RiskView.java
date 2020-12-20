import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.image.*;

import java.lang.reflect.Array;
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
    private ArrayList<Player> players;
    private int width;
    private int height;
    private Stage stage;
    private CardExchangePane cardExchangePane;
    private AllianceRequestPane allianceRequestPane;
    private AlliancePane alliancePane;
    private Button cardsButton;
    private List<Button> nameButtons;
    private boolean backButtonIsClicked = false;
    private int selectedTroop = 0;
    private HashMap<String, Territory> nameAndTerritory;
    private HashMap<Territory, Label> textForEachTer;
    private TroopCountSelectorPane troopCountSelectorPane;
    private Label currentPhaseBar;
    private FlowPane nameBarPane;

    private StackPane endGamePane;

    private int[][] x_y_forEachTerritory = new int[42][2];

    private Button nextPhaseButton;

    private Text troopsLeftText;

    private RiskGame riskGame;
    private Territory[] territoriesAsClass;

    private Button currPlayerBar;
    private RiskGame.GameMode mode = RiskGame.GameMode.TerAllocationMode;

    private RPSView rpsView;

    public RiskView(Stage stage, ArrayList<Player> playerList, int width, int height) {
        players = playerList;
        territoryList = new ArrayList<>();
        textForEachTer = new HashMap<>();
        nameAndTerritory = new HashMap<>();
        territoriesAsClass = new Territory[42];
        nextPhaseButton = new Button();
        rpsView = new RPSView();
        this.width = width;
        this.height = height;
        this.stage = stage;

        Button mainMenuButton = backToMenu(stage);
        HBox mainMenuContainer = new HBox();
        mainMenuContainer.getChildren().add(mainMenuButton);
        mainMenuContainer.setMaxHeight(0);
        mainMenuContainer.setMaxWidth(1000);

        setTroopCountSelector();
        setCardExchangePane();
        setAllianceRequestPane();
        addBackground();

        setTroopsLeft();
        makeClickableMap();
        setTerritoryTexts();
        addPlayerNameBars( this.players);
        for( Territory territory: nameAndTerritory.values()) {
            setTerritoryNeighbors(territory);
        }

        setAlignment(mainMenuContainer, Pos.TOP_RIGHT);
        this.getChildren().add(mainMenuContainer);
        setCurPhaseBar();
        initiateRiskGame();
    }

    public HashMap<String, Territory> getNameAndTerritory()
    {
        return nameAndTerritory;
    }

    private Button backToMenu(Stage stage)
    {
        String styleBackground = "-fx-background-color: #ff8a14;" +
                "-fx-border-color: #00ccff;" + "-fx-text-fill: white";
        Button back = new Button("Return to Main Menu");
        back.setStyle(styleBackground);
        back.setFont(Font.font("SNAP ITC", 15));

        back.setOnMouseClicked( e -> {
            System.out.println("Clicked on New Game");
            //RiskView gameView = new RiskView();
            MainMenuView view = new MainMenuView( stage, width, height);
            Scene menuScene = new Scene( view, width, height);

            stage.setScene( menuScene);;
        });

        return back;
    }

    private void setCardExchangePane() {
        cardExchangePane = new CardExchangePane();
    }

    public void addEndGamePane( Player player) {
        endGamePane = new StackPane();
        endGamePane.setStyle("-fx-background-color:" + player.getColor() + ";-fx-opacity:0.5;");

        VBox vBox = new VBox();
        Text endGameText = new Text("Game Over!");
        endGameText.setFont(Font.font("Snap ITC", 30));
        Text playerWonText = new Text("Player " + player.getName() + " won!");
        playerWonText.setFont(Font.font("Snap ITC", 30));
        Button returnButton = new Button("Return to main menu");
        returnButton.setFont(Font.font("Snap ITC", 30));
        vBox.getChildren().addAll(endGameText, playerWonText, returnButton);

        endGamePane.setAlignment(vBox, Pos.CENTER);

        endGamePane.getChildren().add( vBox);

        returnButton.setOnMouseClicked(e -> {
            Scene newScene = new Scene( new MainMenuView(stage, width, height), width, height);
            stage.setScene(newScene);
        });

        this.getChildren().add(endGamePane);
    }

    // Method that creates the label which will shows the current phase of the game.
    private void setCurPhaseBar() {
        currentPhaseBar = new Label();
        currentPhaseBar.setAlignment(Pos.CENTER);
        currentPhaseBar.setStyle("-fx-background-color: #ff6666;" +
                "-fx-border-color: #00ccff");
        currentPhaseBar.setFont(Font.font("Snap ITC", 27));
        currentPhaseBar.setMaxSize(500,75);
        this.getChildren().add(currentPhaseBar);
        this.setAlignment(currentPhaseBar, Pos.TOP_RIGHT);
    }

    private void setAllianceRequestPane() { allianceRequestPane = new AllianceRequestPane(); }

    public void goToNextPhase() {
        switch (mode) {
            case AttackMode: {
                currentPhaseBar.setText("Fortify Phase");
                break;
            }
            case FortifyMode: {
                currentPhaseBar.setText("Soldier Allocation");
                break;
            }
        }
    }

    public void addNextPhaseButton() {
        this.getChildren().add(nextPhaseButton);
        //set its graphic as the one in the icons directory
        Image nextPhaseImage = new Image("icons/next_phase_icon.png");
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

    public void removeNextPhaseButton() {
        this.getChildren().remove(nextPhaseButton);
    }

    // Updates the currentPhaseBar according to the mode of the game
    public void updateCurPhase() {
        switch (mode) {
            case TerAllocationMode: {
                currentPhaseBar.setText("Territory Allocation");
                break;
            }
            case SoldierAllocationInit: {
                currentPhaseBar.setText("Soldier Allocation");
                break;
            }
            case SoldierAllocationMode: {
                currentPhaseBar.setText("Soldier Allocation");
                break;
            }
            case AttackMode: {
                currentPhaseBar.setText("Attack Phase");
                break;
            }
            case FortifyMode: {
                currentPhaseBar.setText("Fortify Phase");
                break;
            }
        }
    }

    public void updateText(Territory territory, int newNum) {
        Label territoryText = textForEachTer.get(territory);
        territoryText.setText(Integer.toString(newNum));
    }

    public void updateText( Territory territory, int newNum, boolean hasAirport) {
        Label territoryText = textForEachTer.get(territory);
        territoryText.setText(Integer.toString(newNum));
        if(hasAirport)
        {
            ImageView airport = new ImageView(new Image("icons/plane_icon.png"));
            airport.setFitWidth(40);
            airport.setFitHeight(40);
            territoryText.setGraphic(airport);
        }
        else
        {
            territoryText.setGraphic(null);
        }

    }

    public RPSView getRpsView() {
        return rpsView;
    }

    public void displayRPSView() {
        this.getChildren().add(rpsView);
    }

    public Button getNextPhaseButton() {
        return nextPhaseButton;
    }

    private void setTroopsLeft() {
        FlowPane flowPane = new FlowPane();
        troopsLeftText = new Text();
        currPlayerBar = new Button(); //change it based on the current player
        currPlayerBar.setPrefSize(200, 100);

        flowPane.getChildren().add(currPlayerBar);
        flowPane.getChildren().add(new ImageView(new Image("icons/troop_icon.png")));
        flowPane.getChildren().add(troopsLeftText);

        troopsLeftText.setFont(Font.font("SNAP ITC", 30));

        setAlignment(flowPane, Pos.TOP_LEFT);
        setAlignment(troopsLeftText, Pos.CENTER);
        this.getChildren().add(flowPane);
    }

    public void setCardExchangeInfo(Player player) {
        cardExchangePane.setPlayerCards(player);
    }

    public void addTroopsLeft(Player currPlayer) {
        VBox alliesBox = new VBox();
        Button back = new Button("Back");
        back.setOnMouseClicked(e -> {
            alliesBox.getChildren().clear();
            this.getChildren().remove(alliesBox);
        });
        back.setStyle("-fx-background-color: #ff6666;" +
                "-fx-border-color: #00ccff");
        back.setFont(Font.font("Snap ITC", 30));
        back.setMaxSize(500,75);
        javafx.scene.image.ImageView backImg = new ImageView(new Image("icons/back_arrow_icon.png"));
        backImg.setFitHeight(50);
        backImg.setFitWidth(50);
        back.setGraphic(backImg);

        Label title = new Label("Allies:");
        title.setMaxSize(500,75);
        title.setStyle("-fx-background-color: #ff6666;" +
                "-fx-border-color: #00ccff");
        title.setFont(Font.font("Snap ITC", 25));
        title.setAlignment(Pos.CENTER);
        javafx.scene.image.ImageView allyImg = new ImageView(new Image("icons/ally_icon.png"));
        allyImg.setFitHeight(50);
        allyImg.setFitWidth(50);
        title.setGraphic(allyImg);
        currPlayerBar.setOnMouseClicked(e -> {
            alliesBox.getChildren().add(title);
            boolean hasAlly = false;
            boolean[] alliesList = currPlayer.getAllies();
            for (int i = 0; i < players.size(); i++) {
                if(alliesList[i]) {
                    hasAlly = true;
                    Label ally = new Label(players.get(i).getName());
                    ally.setStyle("-fx-background-color: #ff6666;" +
                            "-fx-border-color: #00ccff");
                    ally.setFont(Font.font("Snap ITC", 30));
                    ally.setMaxSize(500,75);
                    ally.setAlignment(Pos.CENTER);
                    alliesBox.getChildren().add(ally);
                    alliesBox.setAlignment(Pos.CENTER);
                }
            }
            if (!hasAlly) {
                title.setText("You do not have any ally.");
            }
            alliesBox.getChildren().add(back);
            this.getChildren().add(alliesBox);
        });
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
        Label terText = textForEachTer.get(clickableTerritory);
        terText.setText(String.valueOf(Integer.valueOf(terText.getText()) + count));
    }

    public void setTerritoryTexts() {
        for( int i = 0; i < territoryList.size(); i++) {
            Label territoryText = new Label("1");
            territoryText.setFont(Font.font("Snap ITC", 20));
/*            String styleBackground = "-fx-background-color: #ff8a14;" +
                    "-fx-border-color: #00ccff;" + "-fx-text-fill: white";*/
            territoryText.setStyle("-fx-stroke: firebrick;"+ "-fx-text-fill: white;" + "-fx-stroke-width: 2px;");
            //territoryText.setFill(Color.rgb(255, 255, 255));
            //territoryText.setStroke(Color.ORANGERED);
            x_y_forEachTerritory[i] = territoryList.get(i).getTerritoryXY();

            //code below changes the location of the texts
            //their original locations in the (1280, 1024) (width, height) space are held inside the imgLocations array
            //so I adjust these x and y locations to fit within whatever width and height settings we have for the game
            //since (0, 0) is (width / 2 , height / 2) in StackPane by default, I also subtract them when setting the location
            territoryText.setTranslateX(x_y_forEachTerritory[i][0] * width / 1280 - width / 2);
            territoryText.setTranslateY(x_y_forEachTerritory[i][1] * height / 1024 - height / 2);

            //below are some special cases where the territory center is not the same as the center of the png image
            //associated with it
            //the explanations for the functions inside them will be given after the if statements
            if( territoryList.get(i).getAssociatedTerritory().getName().equals( "Japan")) {
                territoryText.setTranslateX(1165 * width / 1280 - width / 2);
                territoryText.setTranslateY(380 * height / 1024 - height / 2);
            }
            else if( territoryList.get(i).getAssociatedTerritory().getName().equals( "Kamchatka")) {
                territoryText.setTranslateX(1170 * width / 1280 - width / 2);
                territoryText.setTranslateY(200 * height / 1024 - height / 2);
            }
            else if( territoryList.get(i).getAssociatedTerritory().getName().equals( "Eastern Australia")) {
                territoryText.setTranslateX(x_y_forEachTerritory[i][0] * width / 1280 - width / 2 + 20 * width / 1280);
            }

            territoryText.translateXProperty();
            territoryText.translateYProperty();

            textForEachTer.put( territoryList.get(i).getAssociatedTerritory(), territoryText);
        }
    }

    public boolean backButtonIsClicked() {
        return backButtonIsClicked;
    }

    public void initiateRiskGame() {
        riskGame = new RiskGame(players, territoriesAsClass, territoryList, this);
        riskGame.play();
    }

    public void setTerritoryMode(RiskGame.GameMode mode) {
        if(mode == RiskGame.GameMode.SoldierAllocationMode) {
            for( ClickableTerritory clickableTerritory: territoryList)
                clickableTerritory.setClicked(false);
        }
        this.mode = mode;
        for( ClickableTerritory clickableTerritory1: territoryList) {
            clickableTerritory1.setMode(mode);
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

                element.removeRequest();
                if(allianceRequestPane.decreaseRequestCount() == 0) {
                    this.getChildren().remove(allianceRequestPane);
                    curPlayer.getAllianceReq().clear();
                }
            });

            element.getIgnoreButton().setOnMouseClicked( e -> {
                element.removeRequest();
                allianceRequestPane.decreaseRequestCount();
                if(allianceRequestPane.getRequestCount() == 0) {
                    this.getChildren().remove(allianceRequestPane);
                    curPlayer.getAllianceReq().clear();
                }
            });
        }
    }

    //below is only for the first mode, which is the Territory Allocation Mode
    //After that, it will not be used anymore
    public void addTextForTerritory(Territory territoryClicked) {
        this.getChildren().add(textForEachTer.get(territoryClicked));
    }

    public void addTroopCountSelector( int troopCount, RiskGame.GameMode mode) {
        troopCountSelectorPane.addTroopCountSelectorPane(mode);
        this.getChildren().add(troopCountSelectorPane);
        troopCountSelectorPane.getTroopCountLabel().setText("   1  ");
        troopCountSelectorPane.getLessButton().setOnMouseClicked(e -> {
            int selectedTroopCount = Integer.valueOf(troopCountSelectorPane.getTroopCountLabel().getText().trim());
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
            troopCountSelectorPane.getTroopCountLabel().setText(nextCount);
        });

        troopCountSelectorPane.getMoreButton().setOnMouseClicked(e -> {
            int selectedTroopCount = Integer.valueOf(troopCountSelectorPane.getTroopCountLabel().getText().trim());
            String nextCount = "";
            if(selectedTroopCount < troopCount)
                nextCount = String.valueOf(selectedTroopCount + 1);
            else if( selectedTroopCount == troopCount)
                nextCount = "1";

            if(nextCount.length() == 1)
                nextCount = "   " + nextCount + "  ";
            else if(nextCount.length() == 2)
                nextCount = "  " + nextCount + "  ";
            troopCountSelectorPane.getTroopCountLabel().setText(nextCount);
        });

    }

    public Button getAndSetAttackButton() {
        Button attackButton = new Button("Attack");
        this.getChildren().add(attackButton);
        return attackButton;
    }


    /*public Button getPlaceButton() {
        return placeButton;
    }
    public Button getBuildAirportButton() {
        return buildAirportButton;
    }*/

    public Button getCardsButton() {
        return cardsButton;
    }

    public int getSelectedTroop() {
        System.out.println();
        if(troopCountSelectorPane.getTroopCountLabel().getText() != "")
            selectedTroop = Integer.valueOf(troopCountSelectorPane.getTroopCountLabel().getText().trim());
        return selectedTroop;
    }

    public void setSelectedTroop( int selectedTroop) {
        this.selectedTroop = selectedTroop;
    }

    public void removeTroopCountSelector() {
        troopCountSelectorPane.removeButtons();
        this.getChildren().remove(troopCountSelectorPane);
    }

    private void setTroopCountSelector() {
        troopCountSelectorPane = new TroopCountSelectorPane();
        /*troopCountSelectionPane = new VBox();
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
        troopCountSelectionPane.setAlignment(Pos.CENTER);*/
    }

    private void addBackground() {
        ImageView bgImage = new ImageView( new Image( DIRECTORY_NAME + BACKGROUND_IMG_PATH, true));
        bindMapToPaneSize(bgImage);
        this.getChildren().add( bgImage);
    }

    private void makeClickableMap() {
        for (int i = 0; i < territories.length; i++) {
            Territory territory = new Territory(territories[i], i);

            ClickableTerritory clickableTerritory = new ClickableTerritory(territories[i],
                    DIRECTORY_NAME + territories[i] + FILE_NAME_HELPER,
                    territory);
            bindMapToPaneSize(clickableTerritory);

            territoryList.add(clickableTerritory);

            territoriesAsClass[i] = territory;
            nameAndTerritory.put(territories[i], territory);

            this.getChildren().add(clickableTerritory);
        }
    }

    private void bindMapToPaneSize( ImageView imageView) {
        imageView.fitWidthProperty().bind( this.widthProperty());
        imageView.fitHeightProperty().bind( this.heightProperty());
    }


    public void removeRPSView() {
        this.getChildren().remove(rpsView);
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
        else if(mode == RiskGame.GameMode.SoldierAllocationMode)
            alliancePane.addCancelAllianceButton(); //if the players are allies, cancel alliance
        // button is added to the pane

        alliancePane.getSendAllianceRequestButton().setOnMouseClicked(e->{
            //alliance request is sent to the target player
            riskGame.sendAllianceRequest(target);
            this.getChildren().remove(alliancePane); //returned back to the game
        });

        alliancePane.getCancelAllianceButton().setOnMouseClicked(e->{
            riskGame.cancelAlliance(target);
            this.getChildren().remove(alliancePane); //returned back to the game
        });

        alliancePane.getBackButton().setOnMouseClicked(e -> {
            this.getChildren().remove(alliancePane); //returned back to the game
        });
    }

    public void addPlayerNameBars( ArrayList<Player> players) {
        nameBarPane = new FlowPane();
        nameButtons = new ArrayList<>();

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
                if(player.getId() == -1) return; //do nothing if the player is removed

                //this long if statement checks if the player clicked is NOT the same player, who clicked the button
                if(riskGame.getPlayers().size() > 2 && riskGame.getCurPlayerId() != player.getId())
                    //alliance pane is added
                    this.addAlliancePane(riskGame.getPlayers().get(riskGame.getCurPlayerId()).isAlly(player), player);
            });
            /////////////////////////////////////////////////////////////////////////////
            nameBarPane.getChildren().add(nameButton);

            nameButtons.add( nameButton);
        }
        nameBarPane.setMaxHeight(0);
        nameBarPane.setMaxWidth(2000);
        this.getChildren().add(nameBarPane);
        setAlignment(nameBarPane, Pos.BOTTOM_CENTER);
    }

    public void changeNameButtonColor( Player player) {
        nameButtons.get(player.getId()).setStyle("-fx-background-color:gray;" +
                "-fx-text-fill: white;-fx-background-radius:20;");
    }

    final public void setTerritoryNeighbors( Territory territory) {
        //for all 42
        switch (territory.getName()) {
            case "Alaska": {
                territory.addNeighbor(nameAndTerritory.get("Northwest Territory"));
                territory.addNeighbor(nameAndTerritory.get("Alberta"));
                territory.addNeighbor(nameAndTerritory.get("Kamchatka"));
            }
            case "Northwest Territory": {
                territory.addNeighbor(nameAndTerritory.get("Alaska"));
                territory.addNeighbor(nameAndTerritory.get("Alberta"));
                territory.addNeighbor(nameAndTerritory.get("Ontario"));
                territory.addNeighbor(nameAndTerritory.get("Greenland"));
                break;
            }
            case "Alberta": {
                territory.addNeighbor(nameAndTerritory.get("Alaska"));
                territory.addNeighbor(nameAndTerritory.get("Northwest Territory"));
                territory.addNeighbor(nameAndTerritory.get("Ontario"));
                territory.addNeighbor(nameAndTerritory.get("West America"));
                break;
            }
            case "Ontario": {
                territory.addNeighbor(nameAndTerritory.get("Alberta"));
                territory.addNeighbor(nameAndTerritory.get("Northwest Territory"));
                territory.addNeighbor(nameAndTerritory.get("East America"));
                territory.addNeighbor(nameAndTerritory.get("West America"));
                territory.addNeighbor(nameAndTerritory.get("Quebec"));
                territory.addNeighbor(nameAndTerritory.get("Greenland"));
                break;
            }
            case "Quebec": {
                territory.addNeighbor(nameAndTerritory.get("Ontario"));
                territory.addNeighbor(nameAndTerritory.get("East America"));
                territory.addNeighbor(nameAndTerritory.get("Greenland"));
                break;
            }
            case "Greenland" : {
                territory.addNeighbor(nameAndTerritory.get("Quebec"));
                territory.addNeighbor(nameAndTerritory.get("Northwest Territory"));
                territory.addNeighbor(nameAndTerritory.get("Ontario"));
                territory.addNeighbor(nameAndTerritory.get("Iceland"));
                break;
            }
            case "East America": {
                territory.addNeighbor(nameAndTerritory.get("West America"));
                territory.addNeighbor(nameAndTerritory.get("Ontario"));
                territory.addNeighbor(nameAndTerritory.get("Quebec"));
                territory.addNeighbor(nameAndTerritory.get("Central America"));
                break;
            }
            case "West America": {
                territory.addNeighbor(nameAndTerritory.get("East America"));
                territory.addNeighbor(nameAndTerritory.get("Ontario"));
                territory.addNeighbor(nameAndTerritory.get("Central America"));
                territory.addNeighbor(nameAndTerritory.get("Alberta"));
                break;
            }
            case "Central America": {
                territory.addNeighbor(nameAndTerritory.get("West America"));
                territory.addNeighbor(nameAndTerritory.get("East America"));
                territory.addNeighbor(nameAndTerritory.get("Venezuela"));
                break;
            }
            case "Venezuela": {
                territory.addNeighbor(nameAndTerritory.get("Brazil"));
                territory.addNeighbor(nameAndTerritory.get("Ontario"));
                territory.addNeighbor(nameAndTerritory.get("Peru"));
                break;
            }
            case "Brazil": {
                territory.addNeighbor(nameAndTerritory.get("Venezuela"));
                territory.addNeighbor(nameAndTerritory.get("Argentina"));
                territory.addNeighbor(nameAndTerritory.get("Peru"));
                territory.addNeighbor(nameAndTerritory.get("North Africa"));
                break;
            }
            case "Peru": {
                territory.addNeighbor(nameAndTerritory.get("Venezuela"));
                territory.addNeighbor(nameAndTerritory.get("Argentina"));
                territory.addNeighbor(nameAndTerritory.get("Brazil"));
                break;
            }
            case "Argentina": {
                territory.addNeighbor(nameAndTerritory.get("Brazil"));
                territory.addNeighbor(nameAndTerritory.get("Peru"));
                break;
            }
            case "Egypt": {
                territory.addNeighbor(nameAndTerritory.get("SE"));
                territory.addNeighbor(nameAndTerritory.get("Middle East"));
                territory.addNeighbor(nameAndTerritory.get("North Africa"));
                territory.addNeighbor(nameAndTerritory.get("East Africa"));
                break;
            }
            case "East Africa": {
                territory.addNeighbor(nameAndTerritory.get("Congo"));
                territory.addNeighbor(nameAndTerritory.get("Middle East"));
                territory.addNeighbor(nameAndTerritory.get("North Africa"));
                territory.addNeighbor(nameAndTerritory.get("South Africa"));
                territory.addNeighbor(nameAndTerritory.get("Madagascar"));
                territory.addNeighbor(nameAndTerritory.get("Egypt"));
                break;
            }
            case "Congo": {
                territory.addNeighbor(nameAndTerritory.get("South Africa"));
                territory.addNeighbor(nameAndTerritory.get("North Africa"));
                territory.addNeighbor(nameAndTerritory.get("East Africa"));
                break;
            }
            case "North Africa": {
                territory.addNeighbor(nameAndTerritory.get("Congo"));
                territory.addNeighbor(nameAndTerritory.get("Brazil"));
                territory.addNeighbor(nameAndTerritory.get("SE"));
                territory.addNeighbor(nameAndTerritory.get("Egypt"));
                territory.addNeighbor(nameAndTerritory.get("East Africa"));
                break;
            }
            case "South Africa": {
                territory.addNeighbor(nameAndTerritory.get("Madagascar"));
                territory.addNeighbor(nameAndTerritory.get("Congo"));
                territory.addNeighbor(nameAndTerritory.get("East Africa"));
                break;
            }
            case "Madagascar": {
                territory.addNeighbor(nameAndTerritory.get("South Africa"));
                territory.addNeighbor(nameAndTerritory.get("East Africa"));
                break;
            }
            case "SE": {
                territory.addNeighbor(nameAndTerritory.get("NE"));
                territory.addNeighbor(nameAndTerritory.get("Middle East"));
                territory.addNeighbor(nameAndTerritory.get("Ukraine"));
                territory.addNeighbor(nameAndTerritory.get("WE"));
                territory.addNeighbor(nameAndTerritory.get("North Africa"));
                territory.addNeighbor(nameAndTerritory.get("Egypt"));
                break;
            }
            case "Afghanistan": {
                territory.addNeighbor(nameAndTerritory.get("Ural"));
                territory.addNeighbor(nameAndTerritory.get("Middle East"));
                territory.addNeighbor(nameAndTerritory.get("India"));
                territory.addNeighbor(nameAndTerritory.get("China"));
                territory.addNeighbor(nameAndTerritory.get("Ukraine"));
                break;
            }
            case "Ukraine": {
                territory.addNeighbor(nameAndTerritory.get("NE"));
                territory.addNeighbor(nameAndTerritory.get("SE"));
                territory.addNeighbor(nameAndTerritory.get("Scandinavia"));
                territory.addNeighbor(nameAndTerritory.get("Middle East"));
                territory.addNeighbor(nameAndTerritory.get("Afghanistan"));
                territory.addNeighbor(nameAndTerritory.get("Ural"));
                break;
            }
            case "Ural": {
                territory.addNeighbor(nameAndTerritory.get("Siberia"));
                territory.addNeighbor(nameAndTerritory.get("China"));
                territory.addNeighbor(nameAndTerritory.get("Afghanistan"));
                territory.addNeighbor(nameAndTerritory.get("Ukraine"));
                break;
            }
            case "India": {
                territory.addNeighbor(nameAndTerritory.get("Siam"));
                territory.addNeighbor(nameAndTerritory.get("China"));
                territory.addNeighbor(nameAndTerritory.get("Middle East"));
                territory.addNeighbor(nameAndTerritory.get("Afghanistan"));
                break;
            }
            case "Siam": {
                territory.addNeighbor(nameAndTerritory.get("India"));
                territory.addNeighbor(nameAndTerritory.get("China"));
                territory.addNeighbor(nameAndTerritory.get("Indonesia"));
                break;
            }
            case "China": {
                territory.addNeighbor(nameAndTerritory.get("India"));
                territory.addNeighbor(nameAndTerritory.get("Siam"));
                territory.addNeighbor(nameAndTerritory.get("Afghanistan"));
                territory.addNeighbor(nameAndTerritory.get("Ural"));
                territory.addNeighbor(nameAndTerritory.get("Siberia"));
                territory.addNeighbor(nameAndTerritory.get("Mongolia"));
                break;
            }
            case "Mongolia": {
                territory.addNeighbor(nameAndTerritory.get("China"));
                territory.addNeighbor(nameAndTerritory.get("Siam"));
                territory.addNeighbor(nameAndTerritory.get("Irkutsk"));
                territory.addNeighbor(nameAndTerritory.get("Kamchatka"));
                territory.addNeighbor(nameAndTerritory.get("Japan"));
                break;
            }
            case "Kamchatka": {
                territory.addNeighbor(nameAndTerritory.get("Alaska"));
                territory.addNeighbor(nameAndTerritory.get("Yakutsk"));
                territory.addNeighbor(nameAndTerritory.get("Mongolia"));
                territory.addNeighbor(nameAndTerritory.get("Japan"));
                break;
            }
            case "Yakutsk": {
                territory.addNeighbor(nameAndTerritory.get("Siberia"));
                territory.addNeighbor(nameAndTerritory.get("Irkutsk"));
                territory.addNeighbor(nameAndTerritory.get("Kamchatka"));
                break;
            }
            case "Irkutsk": {
                territory.addNeighbor(nameAndTerritory.get("Yakutsk"));
                territory.addNeighbor(nameAndTerritory.get("Siberia"));
                territory.addNeighbor(nameAndTerritory.get("Mongolia"));
                territory.addNeighbor(nameAndTerritory.get("Kamchatka"));
                break;
            }
            case "Siberia": {
                territory.addNeighbor(nameAndTerritory.get("Ural"));
                territory.addNeighbor(nameAndTerritory.get("China"));
                territory.addNeighbor(nameAndTerritory.get("Mongolia"));
                territory.addNeighbor(nameAndTerritory.get("Irkutsk"));
                territory.addNeighbor(nameAndTerritory.get("Yakutsk"));
                break;
            }
            case "Japan": {
                territory.addNeighbor(nameAndTerritory.get("Kamchatka"));
                territory.addNeighbor(nameAndTerritory.get("Mongolia"));
                break;
            }
            case "Indonesia": {
                territory.addNeighbor(nameAndTerritory.get("Siam"));
                territory.addNeighbor(nameAndTerritory.get("New Guinea"));
                territory.addNeighbor(nameAndTerritory.get("Western Australia"));
                territory.addNeighbor(nameAndTerritory.get("Eastern Australia"));
                break;
            }
            case "New Guinea": {
                territory.addNeighbor(nameAndTerritory.get("Indonesia"));
                territory.addNeighbor(nameAndTerritory.get("New Guinea"));
                territory.addNeighbor(nameAndTerritory.get("Western Australia"));
                territory.addNeighbor(nameAndTerritory.get("Eastern Australia"));
                break;
            }
            case "Western Australia": {
                territory.addNeighbor(nameAndTerritory.get("Indonesia"));
                territory.addNeighbor(nameAndTerritory.get("New Guinea"));
                territory.addNeighbor(nameAndTerritory.get("Eastern Australia"));
                break;
            }
            case "Eastern Australia": {
                territory.addNeighbor(nameAndTerritory.get("Indonesia"));
                territory.addNeighbor(nameAndTerritory.get("New Guinea"));
                territory.addNeighbor(nameAndTerritory.get("Western Australia"));
                break;
            }
            case "NE": {
                territory.addNeighbor(nameAndTerritory.get("Britain"));
                territory.addNeighbor(nameAndTerritory.get("WE"));
                territory.addNeighbor(nameAndTerritory.get("Scandinavia"));
                territory.addNeighbor(nameAndTerritory.get("SE"));
                territory.addNeighbor(nameAndTerritory.get("Ukraine"));
                break;
            }
            case "WE": {
                territory.addNeighbor(nameAndTerritory.get("Britain"));
                territory.addNeighbor(nameAndTerritory.get("NE"));
                territory.addNeighbor(nameAndTerritory.get("SE"));
                break;
            }
            case "Britain": {
                territory.addNeighbor(nameAndTerritory.get("NE"));
                territory.addNeighbor(nameAndTerritory.get("WE"));
                territory.addNeighbor(nameAndTerritory.get("Scandinavia"));
                territory.addNeighbor(nameAndTerritory.get("Iceland"));
                break;
            }
            case "Iceland": {
                territory.addNeighbor(nameAndTerritory.get("Greenland"));
                territory.addNeighbor(nameAndTerritory.get("Britain"));
                territory.addNeighbor(nameAndTerritory.get("Scandinavia"));
                break;
            }
            case "Middle East": {
                territory.addNeighbor(nameAndTerritory.get("SE"));
                territory.addNeighbor(nameAndTerritory.get("Ukraine"));
                territory.addNeighbor(nameAndTerritory.get("Egypt"));
                territory.addNeighbor(nameAndTerritory.get("India"));
                territory.addNeighbor(nameAndTerritory.get("Afghanistan"));
                territory.addNeighbor(nameAndTerritory.get("East Africa"));
                break;
            }
            case "Scandinavia": {
                territory.addNeighbor(nameAndTerritory.get("Iceland"));
                territory.addNeighbor(nameAndTerritory.get("NE"));
                territory.addNeighbor(nameAndTerritory.get("Ukraine"));
                break;
            }
            default: {
                System.out.println(territory.getName() + " is not present.");
            }
        }
    }

    public TroopCountSelectorPane getTroopCountSelectorPane()
    {
        return troopCountSelectorPane;
    }
}