import javafx.application.Application;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.effect.*;

import java.io.*; //exceptions

public class RiskView extends StackPane {
    final int WIDTH = 1280;
    final int HEIGHT = 1024;

    final String DIRECTORY_NAME = "/img/";
    final String FILE_NAME_HELPER = "_bw.png";
    final String FILE_NAME_HOVERED_HELPER = "_bw_hovered.png";
    final String[] territories = {"Alaska", "Northwest Territory", "Greenland", "Alberta", "Ontario", "Quebec",
            "West America", "East America", "Central America", "Venezuela", "Peru", "Brazil", "Argentina",
            "North Africa", "Egypt", "East Africa", "Congo", "South Africa", "Madagascar",
            "Iceland", "Scandinavia", "Ukraine", "Britain", "NE", "SE", "WE",
            "Indonesia", "New Guinea", "Western Australia", "Eastern Australia",
            "Siam", "India", "China", "Mongolia", "Japan", "Irkutsk", "Yakutsk", "Kamchatka", "Siberia",
            "Afghanistan", "Ural", "Middle East"
    };
    final String BACKGROUND_IMG_PATH = "background_image_bw.png";

    private Stage mainStage;

    public RiskView(Stage stage) {
        makeClickableMap();
    }

    /** come back to this **/
    public RiskView( int minWidth, int minHeight) {
        setMinSize( minWidth, minHeight);
        makeClickableMap();
    }

    //below works for the first round (territory allocation), probably
    private ImageView addClickableTerritory( String countryName, String path, String hoverPath) {
        ImageView territoryClickable = null;
        boolean clicked = false;
        try {
            Image image = new Image( getClass().getResource(path).toExternalForm());
            Image hoveredImage = new Image( hoverPath);

            territoryClickable = new ImageView(image);
            ImageView territoryHovered = new ImageView(hoveredImage);

            //when clicked on territory
            territoryClickable.setOnMouseClicked( e -> {
                System.out.println("Clicked on " + countryName);
            });

            //when mouse hovers over the territory
            territoryClickable.imageProperty().bind(
                    Bindings.when(territoryClickable.hoverProperty())
                            .then(hoveredImage)
                            .otherwise(image)
            );
        }
        catch( Exception e) {
            e.printStackTrace();
        }
        return territoryClickable;
    }

    private void makeClickableMap() {
        ImageView bgImage = new ImageView( new Image( DIRECTORY_NAME + BACKGROUND_IMG_PATH, true));
        bindMapToPaneSize(bgImage);
        this.getChildren().add( bgImage);

        for( int i = 0; i < territories.length; i++) {
            ImageView clickableTerritory = addClickableTerritory( territories[i],
                                                             DIRECTORY_NAME + territories[i] + FILE_NAME_HELPER,
                                                          DIRECTORY_NAME + territories[i] + FILE_NAME_HOVERED_HELPER);
            bindMapToPaneSize(clickableTerritory);

            this.getChildren().add( clickableTerritory);
        }
    }

    private void bindMapToPaneSize( ImageView imageView) {
        imageView.fitWidthProperty().bind( this.widthProperty());
        imageView.fitHeightProperty().bind( this.heightProperty());
        imageView.setPreserveRatio( true);
    }

}