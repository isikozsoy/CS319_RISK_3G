import javafx.scene.control.Button;
import javafx.scene.effect.*;
import javafx.scene.*;

public class MainMenuButton extends Button {
    //font is Erica One
    final private String BUTTON_FREE_STYLE_ODD     = "-fx-background-color: linear-gradient(to right, orange, tomato);" +
                                                     "transform: perspective(500px) rotateY(15deg);";
    final private String BUTTON_HOVERED_STYLE_ODD  = "transform: perspective(200px) rotateY(15deg);" ;
    final private String BUTTON_FREE_STYLE_EVEN    = "background: linear-gradient(to left, orange, tomato);" +
                                                     "text-align: right;" +
                                                     "padding-right: 10%;" +
                                                     "transform: perspective(500px) rotateY(-15deg);";
    final private String BUTTON_HOVERED_STYLE_EVEN = "transform: perspective(200px) rotateY(-15deg);" +
                                                     "padding-right: 5%;";
    /**
    final private String BUTTON_FREE_STYLE    = "-fx-background-color: #F50000;" +
                                                "-fx-opacity: 0.5;" +
                                                "-fx-text-fill: #000000;";
    **/
    final private int MENU_WIDTH = 720;
    final private int MENU_HEIGHT = 80;
    public int menuButtonCount = 0;
    private int buttonLocX;
    private int buttonLocY;
/**
    body {
        margin: 0;
        height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        background: cornsilk;
    }

    ul {
        padding: 0;
        list-style-type: none;
    }

    ul li {
        box-sizing: border-box;
        width: 15em;
        height: 3em;
        font-size: 20px;
        border-radius: 0.5em;
        margin: 0.5em;
        box-shadow: 0 0 1em rgba(0,0,0,0.2);
        color: white;
        font-family: sans-serif;
        text-transform: capitalize;
        line-height: 3em;
        transition: 0.3s;
        cursor: pointer;
    }
**/

    public MainMenuButton(int locX, int locY, int count) {
        buttonLocX = locX;
        buttonLocY = locY;

        setPrefWidth(MENU_WIDTH);
        setPrefHeight(MENU_HEIGHT);

        menuButtonCount = count;

        if( menuButtonCount % 2 == 0) {
            setStyle( BUTTON_FREE_STYLE_EVEN);
        }
        else {
            setStyle( BUTTON_FREE_STYLE_ODD);
        }

        setEventListeners();
        setLayoutX( locX);
        setLayoutY( locY);
    }

    private void setEventListeners(){
        setOnMousePressed( e -> {
            if( menuButtonCount % 2 == 0) {
                setStyle( BUTTON_HOVERED_STYLE_EVEN);
            }
            else {
                setStyle( BUTTON_HOVERED_STYLE_ODD);
            }
        });
        //setOnMouseReleased( e -> setStyle(BUTTON_FREE_STYLE));
        setOnMouseEntered( e -> {
            if( menuButtonCount % 2 == 0) {
                setStyle( BUTTON_HOVERED_STYLE_EVEN);
            }
            else {
                setStyle( BUTTON_HOVERED_STYLE_ODD);
            }
        });
        setOnMouseExited ( e -> {
            if( menuButtonCount % 2 == 0) {
                setStyle( BUTTON_FREE_STYLE_EVEN);
            }
            else {
                setStyle( BUTTON_FREE_STYLE_ODD);
            }
        });
    }

    public Group perspectiveSetting( MainMenuText menuText) {
        Group g = new Group();

        int arbitraryHeight = 30;

        PerspectiveTransform pt = new PerspectiveTransform();
        pt.setUlx(buttonLocX);
        pt.setUly(buttonLocY);
        pt.setUrx(buttonLocX + MENU_WIDTH);
        pt.setUry(buttonLocY + arbitraryHeight);
        pt.setLrx(buttonLocX + MENU_WIDTH);
        pt.setLry(buttonLocY + MENU_WIDTH + arbitraryHeight * 2);
        pt.setLlx(buttonLocX);
        pt.setLly(buttonLocX + MENU_HEIGHT);

        g.setEffect(pt);
        g.getChildren().add( this);
        g.getChildren().add( menuText);
        return g;
    }

}