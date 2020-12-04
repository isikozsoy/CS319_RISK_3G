import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.image.*;

import java.io.*; //exceptions
import java.util.ArrayList;
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
    //private
    StackPane rpsGameRoot;
    private Button playButton;
    private int clickedOnPlay = 0;

    public RiskView(Stage stage) {
        territoryList = new ArrayList<>();
        try {
            rpsGameRoot = FXMLLoader.load(Main.class.getResource("RPSView.fxml"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        makeClickableMap();
        addPlayButton(stage);
    }

    private void makeClickableMap() {
        ImageView bgImage = new ImageView( new Image( DIRECTORY_NAME + BACKGROUND_IMG_PATH, true));
        bindMapToPaneSize(bgImage);
        this.getChildren().add( bgImage);

        for (String territory : territories) {
            ClickableTerritory clickableTerritory = new ClickableTerritory(territory,
                    DIRECTORY_NAME + territory + FILE_NAME_HELPER);
            bindMapToPaneSize(clickableTerritory);
            territoryList.add(clickableTerritory);

            this.getChildren().add(clickableTerritory);
        }
    }

    private void bindMapToPaneSize( ImageView imageView) {
        imageView.fitWidthProperty().bind( this.widthProperty());
        imageView.fitHeightProperty().bind( this.heightProperty());
    }

    private void addPlayButton(Stage stage) {
        playButton = new Button("play");
        playButton.setLayoutX(500);
        playButton.setLayoutY(20);
        addRPSView();
        this.getChildren().add(playButton);
        setAlignment(playButton, Pos.TOP_RIGHT);
    }

    private void addRPSView() {
        playButton.setOnMousePressed( e -> {
            disableAllClickableTer();
            this.getChildren().add(rpsGameRoot);
            //removeRPSView();
            //rpsGameRoot.execute(this);
        });
    }

    private void removeRPSView() {
        playButton.setOnMousePressed(e -> {
            System.out.println("Clicked on play");
            enableAllClickableTer();
            this.getChildren().remove(rpsGameRoot);
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
}