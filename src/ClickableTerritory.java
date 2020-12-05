import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;

import java.util.HashMap;

public class ClickableTerritory extends ImageView {
    private final String territoryName;
    private ColorAdjust colorAdjust;
    private HashMap<String, Double> colorsAndHues;
    private String color;

    private boolean clicked = false;

    ClickableTerritory( String territoryName, String origPath) {
        this.territoryName = territoryName;
        colorAdjust = new ColorAdjust();
        color = "aqua"; //as an initial value
        colorsAndHues = new HashMap<>();
        colorsAndHues.put("orange", 0.18);
        colorsAndHues.put("red", 0.0);
        colorsAndHues.put("aqua", 0.9);
        colorsAndHues.put("fuchsia", -0.1);
        colorsAndHues.put("purple", -0.4);
        colorsAndHues.put("blue", -0.7);
        colorsAndHues.put("green", 0.7);
        colorsAndHues.put("yellow", 0.3);

        try {
            Image origImage = new Image(origPath);
            setImage(origImage);
            addEventListeners();
        }
        catch( Exception e) {
            e.printStackTrace();
        }
    }

    public void setColor(String color) {
        this.color = color;
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
        colorAdjust.setHue(colorsAndHues.get(color));
        colorAdjust.setSaturation(95);
        colorAdjust.setBrightness(0.35);
        this.setEffect(colorAdjust);
    }
}