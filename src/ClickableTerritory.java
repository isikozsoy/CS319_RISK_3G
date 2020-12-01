import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;

public class ClickableTerritory extends ImageView {
    private String origPath;
    private String territoryName;

    private Image origImage;

    private boolean clicked = false;

    ClickableTerritory( String territoryName, String origPath) {
        this.territoryName = territoryName;
        this.origPath = origPath;

        try {
            origImage = new Image(origPath);
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
            System.out.println("Clicked on " + territoryName);
            changeColor();
        });
        setOnMouseReleased( e -> {
            if( clicked) {
                return;
            }
            clicked = true;
            changeColor();
        });
        setOnMouseEntered( e -> {
            if( clicked) {
                return;
            }
            changeColor();
        });
        setOnMouseExited( e -> {
            if( clicked) {
                return;
            }
            setEffect(null);
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

    private void changeColor() {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setHue(18);
        colorAdjust.setSaturation(90);
        this.setEffect(colorAdjust);
    }
}