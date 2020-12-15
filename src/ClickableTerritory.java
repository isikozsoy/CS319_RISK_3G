import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.*;

import java.util.HashMap;

public class ClickableTerritory extends ImageView {
    private final String territoryName;
    private ColorAdjust colorAdjust;
    private HashMap<String, Double> colorsAndHues;
    private String color;
    private Territory associatedTerritory;
    private RiskGame.GameMode mode = RiskGame.GameMode.TroopAllocationMode;

    private boolean clicked = false;

    ClickableTerritory( String territoryName, String origPath, Territory associatedTerritory) {
        this.territoryName = territoryName;
        this.associatedTerritory = associatedTerritory;
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

    public boolean getClicked() {
        return clicked;
    }

    public void setMode(RiskGame.GameMode mode) {
        this.mode = mode;
        addEventListeners();
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public Territory getAssociatedTerritory() {
        return associatedTerritory;
    }

    public void addEventListeners() {
        switch( mode) {
            case TroopAllocationMode: {
                setOnMousePressed(e -> {
                    if (clicked) {
                        return;
                    }
                    System.out.println("Clicked on " + territoryName);
                    changeColor();
                });
                setOnMouseReleased(e -> {
                    if (clicked) {
                        return;
                    }
                    clicked = true;
                    changeColor();
                });
                setOnMouseEntered(e -> {
                    if (clicked) {
                        return;
                    }
                    changeColor();
                });
                setOnMouseExited(e -> {
                    if (clicked) {
                        return;
                    }
                    setEffect(null);
                });
                break;
            }
            default: {
                setOnMousePressed( e -> {
                    if(clicked) {
                        changeColor();
                        clicked = false;
                    }
                    else {
                        setEffect(new DropShadow());
                        clicked = true;
                    }
                });
                setOnMouseReleased(e -> {
                    setEffect(new DropShadow());
                });
                setOnMouseEntered(e -> {
                    setEffect(new DropShadow());
                });
                setOnMouseExited(e -> {
                    changeColor();
                });
            }
        }
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