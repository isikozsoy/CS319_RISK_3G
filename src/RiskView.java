import javafx.beans.binding.ListBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
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
    private Text countSelectionText;
    private HashMap<ClickableTerritory, StackPane> paneForEachTer;

    private Text territoryText;

    private RiskGame riskGame;
    private Territory[] territoriesAsClass;

    public RiskView(Stage stage, ArrayList<Player> playerList, int width, int height) {
        players = playerList;
        territoryList = new ArrayList<>();
        paneForEachTer = new HashMap<>();
        territoriesAsClass = new Territory[42];
        this.width = width;
        this.height = height;
        this.stage = stage;

        setRockPaperScissorPane();
        setTroopCountSelector();
        addBackground();
        addPlayerNameBars();
        makeClickableMap();

        addPlayButton();

        initiateRiskGame();
    }

    //below is for the first mode
    public Territory getClickedTerritory() {
        Territory territoryClicked = null;
        for( ClickableTerritory clickableTerritory: territoryList) {
            if( clickableTerritory.getClicked() && !territoriesAlreadyClicked.contains(clickableTerritory)) {
                territoryClicked = clickableTerritory.getAssociatedTerritory();
                territoriesAlreadyClicked.add(clickableTerritory);

                //add troop count
                Text troopText = new Text("1");
                troopText.setFont(Font.font("Snap ITC", 30));
                paneForEachTer.get(clickableTerritory).getChildren().add(troopText);

                break;
            }
        }
        return territoryClicked;
    }

    public void initiateRiskGame() {
        riskGame = new RiskGame(players, territoriesAsClass, this);
        riskGame.play();
    }

    public void setTerritoryMode(RiskGame.GameMode mode) {
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

        lessButton.setOnMouseClicked(e -> {

        });

        moreButton.setOnMouseClicked(e -> {

        });
    }

    private void removeTroopCountSelector() {
        this.getChildren().remove(troopCountSelectionPane);
    }

    public void setTroopCountSelector() {
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
        ImageView bgImage = new ImageView( new Image( DIRECTORY_NAME + BACKGROUND_IMG_PATH, true));
        bindMapToPaneSize(bgImage);
        this.getChildren().add( bgImage);
    }

    private void makeClickableMap() {
        for (int i = 0; i < territories.length; i++) {
            StackPane territoryPane = new StackPane();

            Territory territory = new Territory(territories[i]);

            ClickableTerritory clickableTerritory = new ClickableTerritory(territories[i],
                    DIRECTORY_NAME + territories[i] + FILE_NAME_HELPER,
                    territory);
            bindMapToPaneSize(clickableTerritory);

            territoryPane.getChildren().add(clickableTerritory);
            paneForEachTer.put(clickableTerritory, territoryPane);

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

    public void removeListeners(){
        setOnMousePressed(e->{

        });
        setOnMouseReleased(e->{

        });
    }
}