import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.File;

public class RPSGameController{
    @FXML
    Text optRock;
    @FXML
    Text optPaper;
    @FXML
    Text optScissor;
    @FXML
    Text playerName;
    @FXML
    ImageView rock_icon;
    @FXML
    ImageView paper_icon;
    @FXML
    ImageView scissors_icon;

    public void initialize() {
        File file = new File("src/icons/rock_rps.png");
        rock_icon.setImage(new Image(file.toURI().toString()));

        file = new File("src/icons/paper_rps.png");
        paper_icon.setImage(new Image(file.toURI().toString()));

        file = new File("src/icons/scissors_rps.png");
        scissors_icon.setImage(new Image(file.toURI().toString()));
    }

    private void showPlayer1Option () {
        playerName.setText("Player 1");
        optRock.setText("a");
        optPaper.setText("s");
        optScissor.setText("d");
    }
}