import javafx.scene.text.*;
import javafx.scene.paint.Color;
import java.io.*;

public class MainMenuText extends Text {
    final private String TEXT_STYLE = "-fx-stroke: #f50000;" +
            "-fx-stroke-width: 3;";
    final private static int TEXT_SIZE = 60;

    MainMenuText( int textLocX, int textLocY, String text) {
        super( textLocX, textLocY + TEXT_SIZE, text);
        this.setFill( Color.WHITE);
        this.setStyle(TEXT_STYLE);
        this.setFont(Font.font("Snap ITC", TEXT_SIZE));
    }
}