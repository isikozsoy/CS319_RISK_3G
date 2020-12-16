import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.text.Text;

import java.util.HashMap;

public class ClickableTerritory extends ImageView {
    private final String territoryName;
    private ColorAdjust colorAdjust;
    private HashMap<String, Double> colorsAndHues;
    private String color;
    private Territory associatedTerritory;
    private RiskGame.GameMode mode = RiskGame.GameMode.TerAllocationMode;

    private boolean clicked = false;

    private Blend shadowAndColorBlend;

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
            case TerAllocationMode: {
                setOnMousePressed(e -> {
                    if (clicked) {
                        return;
                    }
                });
                setOnMouseReleased(e -> {
                    if (clicked) {
                        return;
                    }
                    clicked = true;
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

            case SoldierAllocationMode: {
                setOnMousePressed(e -> {
                    changeColor();
                });
                setOnMouseReleased(e -> {
                    clicked = true;
                    changeColor();
                });
                setOnMouseEntered(e -> {
                    changeColor();
                });
                setOnMouseExited(e -> {
                    colorAdjust.setBrightness(0.35);
                    setEffect(colorAdjust);
                });
                break;
            }

            default: {
                setOnMousePressed( e -> {
                    System.out.println(clicked);
                    if(clicked) {
                        setEffect(colorAdjust);
                        clicked = false;
                    }
                    else {
                        shadowAndColorBlend = new Blend();
                        shadowAndColorBlend.setMode(BlendMode.ADD);
                        //Setting both the shadow effects to the blend
                        shadowAndColorBlend.setBottomInput(new DropShadow());
                        shadowAndColorBlend.setBottomInput(colorAdjust);
                        setEffect(shadowAndColorBlend);
                        clicked = true;
                    }
                });
                setOnMouseReleased(e -> {
                    shadowAndColorBlend = new Blend();
                    shadowAndColorBlend.setMode(BlendMode.ADD);
                    //Setting both the shadow effects to the blend
                    shadowAndColorBlend.setBottomInput(new DropShadow());
                    shadowAndColorBlend.setBottomInput(colorAdjust);
                    setEffect(shadowAndColorBlend);
                });
                setOnMouseEntered(e -> {
                    shadowAndColorBlend = new Blend();
                    shadowAndColorBlend.setMode(BlendMode.ADD);
                    //Setting both the shadow effects to the blend
                    shadowAndColorBlend.setBottomInput(colorAdjust);
                    shadowAndColorBlend.setBottomInput(new DropShadow());
                    setEffect(shadowAndColorBlend);
                });
                setOnMouseExited(e -> {
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
        switch (mode) {
            case TerAllocationMode: {
                colorAdjust.setHue(colorsAndHues.get(color));
                colorAdjust.setSaturation(95);
                colorAdjust.setBrightness(0.35);
                this.setEffect(colorAdjust);
                break;
            }
            case SoldierAllocationMode: {
                colorAdjust.setBrightness(1);
            }
        }
    }

}