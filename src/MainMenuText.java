import javafx.scene.text.*;
import javafx.scene.paint.Color;
import java.io.*;

public class MainMenuText extends Text {
    final private String TEXT_STYLE           = "-fx-stroke: #F50000;" +
            "-fx-stroke-width: 3;";
    final private static int TEXT_SIZE = 60;
    final private static String FONT_PATH     = "font/EricaOne-Regular.ttf";

    MainMenuText( int textLocX, int textLocY, String text) {
        super( textLocX, textLocY + TEXT_SIZE, text);
        this.setFill( Color.WHITE);
        this.setStyle(TEXT_STYLE);
        try {
            this.setFont(Font.loadFont(new FileInputStream(FONT_PATH), TEXT_SIZE));
        }
        catch (Exception e) {
            this.setFont( Font.font("Verdana", FontWeight.BOLD, TEXT_SIZE));
        }
    }
}