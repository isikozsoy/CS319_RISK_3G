import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;

public class ClickableTerritory extends ImageView {
    private final String territoryName;
    private ColorAdjust colorAdjust;

    private boolean clicked = false;

    ClickableTerritory( String territoryName, String origPath) {
        this.territoryName = territoryName;
        colorAdjust = new ColorAdjust();

        try {
            Image origImage = new Image(origPath);
            setImage(origImage);
            addEventListeners();
        }
        catch( Exception e) {
            e.printStackTrace();
        }
    }

    public void addEventListeners() {
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
        colorAdjust.setHue(0.18);
        colorAdjust.setSaturation(95);
        colorAdjust.setBrightness(0.35);
        this.setEffect(colorAdjust);
    }
}