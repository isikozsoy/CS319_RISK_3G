import javafx.scene.effect.*;
import javafx.scene.image.*;

import java.util.HashMap;

public class ClickableTerritory extends ImageView {
    private final String territoryName;
    private ColorAdjust colorAdjust;
    private HashMap<String, Double> colorsAndHues;
    private String color;
    private Territory associatedTerritory;
    private RiskGame.GameMode mode = RiskGame.GameMode.TroopAllocationMode;
    private PixelReader pixelReader;

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
            pixelReader = origImage.getPixelReader();
            addEventListeners();
        }
        catch( Exception e) {
            e.printStackTrace();
        }
    }

    public int[] getTerritoryXY() {
        int W = (int) this.getImage().getWidth();
        int H = (int) this.getImage().getHeight();

        int smallestY = Integer.MAX_VALUE;
        int smallestX = Integer.MAX_VALUE;
        int largestY = Integer.MIN_VALUE;
        int largestX = Integer.MIN_VALUE;

        int argb = 0;

        for( int y = 0; y < H; y++) {
            for(int x = 0; x < W; x++) {
                argb = pixelReader.getArgb(x, y);
                if( argb != 0) {
                    if( x < smallestX) smallestX = x;
                    if( x > largestX) largestX = x;
                    if( y < smallestY) smallestY = y;
                    if( y > largestY) largestY = y;
                }
            }
        }

        int[] locArray = { smallestX + (largestX - smallestX) / 2, smallestY + (largestY - smallestY) / 2};

        return locArray;
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
                    setEffect(colorAdjust);
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