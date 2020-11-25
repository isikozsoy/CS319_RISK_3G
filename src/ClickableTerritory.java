import javafx.scene.image.*;

public class ClickableTerritory extends ImageView {
    private String origPath;
    private String hoveredPath;
    private String territoryName;

    private Image origImage;
    private Image hoveredImage;

    private boolean clicked = false;

    ClickableTerritory( String territoryName, String origPath, String hoveredPath) {
        this.territoryName = territoryName;
        this.hoveredPath = hoveredPath;
        this.origPath = origPath;

        try {
            origImage = new Image(origPath);
            hoveredImage = new Image( hoveredPath);
            setImage(origImage);
            addEventListeners();
        }
        catch( Exception e) {
            e.printStackTrace();
        }
    }

    private void addEventListeners() {
        setOnMousePressed( e -> {
            if( clicked) {
                return;
            }
            setImage(hoveredImage);
        });
        setOnMouseReleased( e -> {
            if( clicked) {
                return;
            }
            clicked = true;
            origImage = hoveredImage;
        });
        setOnMouseEntered( e -> {
            if( clicked) {
                return;
            }
            setImage(hoveredImage);
        });
        setOnMouseExited( e -> {
            if( clicked) {
                return;
            }
            setImage(origImage);
        });
    }
    public void removeEventListeners() {
        setOnMousePressed( e -> {
        });
        setOnMouseReleased( e -> {
        });
        setOnMouseEntered( e -> {
        });
        setOnMouseExited( e -> {
        });
    }
}