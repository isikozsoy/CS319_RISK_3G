import javafx.scene.layout.StackPane;
import javafx.scene.image.*;
import javafx.scene.*;

public class BeforeGameView extends StackPane {
    private String BACKGROUND_FILE_PATH   = "img/" + "background_image.png";

    public ImageView backgroundImageView;

    BeforeGameView() {
        backgroundImageView = new ImageView( new Image( BACKGROUND_FILE_PATH, true));
        addBackgroundImage();
    }

    private void addBackgroundImage() {
        this.getChildren().add( backgroundImageView);
        backgroundImageView.fitWidthProperty().bind( this.widthProperty());
        backgroundImageView.fitHeightProperty().bind( this.heightProperty());
/**
 BackgroundImage bgImage = new BackgroundImage( new Image( BACKGROUND_FILE_PATH, true),
 BackgroundRepeat.NO_REPEAT,
 BackgroundRepeat.NO_REPEAT,
 BackgroundPosition.DEFAULT,
 new BackgroundSize(1.0, 1.0, true, true, false, false));

 setBackground( new Background( bgImage));
 **/
    }
    public void addGroup( Group g) {
        this.getChildren().add(g);
    }
}