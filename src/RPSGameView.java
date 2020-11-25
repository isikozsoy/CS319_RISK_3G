import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.plaf.PanelUI;
import java.awt.*;


public class RPSGameView extends RiskView {
    final String DIRECTORY_NAME = "/icons/";
    final String FILE_NAME_HELPER = "_rps.png";
    final String[] RPSOptions = {"rock", "paper", "scissors"};

    public RPSGameView(Stage stage) {
        super(stage);
        disableAllComponents();
        addRPSImages();
    }

    private void addRPSImages() {
        FlowPane RPSIcons = new FlowPane();
        RPSIcons.setOrientation(Orientation.HORIZONTAL);
        RPSIcons.setAlignment(Pos.CENTER);
        for (int i = 0; i < RPSOptions.length; i++) {
            Image image = new Image(DIRECTORY_NAME + RPSOptions[i] + FILE_NAME_HELPER);
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            RPSIcons.getChildren().add(imageView);
        }
        this.getChildren().add(RPSIcons);
    }
}
