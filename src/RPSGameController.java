import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class RPSGameController implements Initializable {
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

    int noOfP1SoldiersPriv;
    int noOfP2SoldiersPriv;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("src/icons/rock_rps.png");
        rock_icon.setImage(new Image(file.toURI().toString()));

        file = new File("src/icons/paper_rps.png");
        paper_icon.setImage(new Image(file.toURI().toString()));

        file = new File("src/icons/scissors_rps.png");
        scissors_icon.setImage(new Image(file.toURI().toString()));

        play(5, 5);

    }

    public void play(int noOfP1Soldiers, int noOfP2Soldiers) {
        //if (noOfP1Soldiers <= 0 || noOfP2Soldiers <= 0)

        //int winner = -1; // Initially we assume round is draw

        final EventHandler<KeyEvent> keyEventHandler =
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(final KeyEvent keyEvent) {
                        int noOfP1Soldiers = 3;
                        int noOfP2Soldiers = 3;
                        while (noOfP1Soldiers > 0 && noOfP2Soldiers > 0) {
                            String p1Choice = "";
                            String p2Choice = "";

                            showPlayer1Option();
                            while (keyEvent.getCode() != KeyCode.A || keyEvent.getCode() != KeyCode.S
                                    || keyEvent.getCode() != KeyCode.D) {
                            }
                            if (keyEvent.getCode() != KeyCode.A) {
                                p1Choice = "A";
                            } else if (keyEvent.getCode() != KeyCode.S) {
                                p1Choice = "S";
                            } else if (keyEvent.getCode() != KeyCode.S) {
                                p1Choice = "S";
                            }

                            showPlayer2Option();
                            while (keyEvent.getCode() != KeyCode.DIGIT1 || keyEvent.getCode() != KeyCode.DIGIT2
                                    || keyEvent.getCode() != KeyCode.DIGIT3) {
                            }
                            if (keyEvent.getCode() != KeyCode.DIGIT1) {
                                p2Choice = "1";
                            } else if (keyEvent.getCode() != KeyCode.DIGIT2) {
                                p2Choice = "2";
                            } else if (keyEvent.getCode() != KeyCode.DIGIT3) {
                                p2Choice = "3";
                            }

                            int winner = compare(p1Choice, p2Choice); // 1 if Player 1 wins, 2 if Player 2 wins, -1 if draw

                            if (winner == -1) // Draw
                                winner = compare(p1Choice, p2Choice);
                            if (winner == 1)  // Player 1 wins
                                noOfP2Soldiers--;
                            if (winner == 2) // Player 2 wins
                                noOfP1Soldiers--;
                        }
                        System.out.println(noOfP1Soldiers);
                        System.out.println(noOfP2Soldiers);
                    }
                };
    }

    public int compare(String p1Choice, String p2Choice)
    {
        // 'A' or 'a' or '1' = Rock
        // 'S' or 's' or '2' = Paper
        // 'D' or 'd' or '3' = Scissors
        p1Choice.toUpperCase();

        if( (p1Choice == "A" && p2Choice == "3")
                || (p1Choice == "S" && p2Choice == "1")
                ||(p1Choice == "D" && p2Choice == "2"))
            return 1; // Player 1 wins
        if( (p1Choice == "D" && p2Choice == "1")
                || (p1Choice == "A" && p2Choice == "2")
                ||(p1Choice == "S" && p2Choice == "3"))
            return 2; // Player 2 wins
        return -1; // Draw
    }

    private void showPlayer1Option () {
        playerName.setText("Player 1");
        optRock.setText("a");
        optPaper.setText("s");
        optScissor.setText("d");
    }

    private void showPlayer2Option () {
        playerName.setText("Player 2");
        optRock.setText("1");
        optPaper.setText("2");
        optScissor.setText("3");
    }
}