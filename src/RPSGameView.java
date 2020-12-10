import com.sun.javafx.scene.KeyboardShortcutsHandler;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Scanner;

import static javafx.application.Application.launch;


public class RPSGameView extends RiskView {
    final private String DIRECTORY_NAME = "/icons/";
    final private String FILE_NAME_HELPER = "_rps.png";
    final private String[] RPSOptions = {"rock", "paper", "scissors"};
    private int noOfP1SoldiersPriv;
    private int noOfP2SoldiersPriv;
    private char p1Choice;
    private char p2Choice;

    public RPSGameView(Stage stage) {
        super(stage);
        disableAllComponents();
        addRPSImages();

        System.out.println(play(2,1, stage));
        //play(2,1, stage);

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

    private void showPlayer1Option() {

    }

    public int play(  int noOfP1Soldiers, int noOfP2Soldiers, Stage stage)
    {
        if( noOfP1Soldiers <= 0 || noOfP2Soldiers <= 0)
            return -1; //invalid entry

        int winner = -1; // Initially we assume round is draw

        EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.A ||
                        keyEvent.getCode() == KeyCode.S ||
                        keyEvent.getCode() == KeyCode.D) {

                    p1Choice = keyEvent.getCode().getChar().charAt(0);
                }
                if (keyEvent.getCode() == KeyCode.DIGIT1 ||
                        keyEvent.getCode() == KeyCode.DIGIT2 ||
                        keyEvent.getCode() == KeyCode.DIGIT3) {

                    p2Choice = keyEvent.getCode().getChar().charAt(0);
                }
            }
        };
        stage.addEventHandler(KeyEvent.KEY_PRESSED, handler);

/*        while( noOfP1Soldiers > 0 && noOfP2Soldiers > 0) // game is played until one of the players have no soldiers
        {
            if((p1Choice == 'A' || p1Choice == 'S' || p1Choice == 'D') &&
                    (p2Choice == '1' || p2Choice == '2' || p2Choice == '3'))
            {
                winner = compare(p1Choice, p2Choice); // 1 if Player 1 wins, 2 if Player 2 wins, -1 if draw
                if(winner == 1)  // Player 1 wins
                    noOfP2Soldiers--;
                if(winner == 2) // Player 2 wins
                    noOfP1Soldiers--;
            }
        }*/
        noOfP1SoldiersPriv = noOfP1Soldiers;
        noOfP2SoldiersPriv = noOfP2Soldiers;

        if( noOfP1Soldiers == 0)
            return 1;

        System.out.println(noOfP1Soldiers);
        System.out.println(noOfP2Soldiers);
        return 2;
    }

    public int compare(char p1Choice, char p2Choice)
    {
        // 'A' or 'a' or '1' = Rock
        // 'S' or 's' or '2' = Paper
        // 'D' or 'd' or '3' = Scissors

        if( ((p1Choice == 'a' || p1Choice == 'A') && p2Choice == '3')
                || ((p1Choice == 's' || p1Choice == 'S') && p2Choice == '1')
                ||((p1Choice == 'd' || p1Choice == 'D') && p2Choice == '2'))
            return 1; // Player 1 wins
        if( ((p1Choice == 'd' || p1Choice == 'D') && p2Choice == '1')
                || ((p1Choice == 'a' || p1Choice == 'A') && p2Choice == '2')
                ||((p1Choice == 's' || p1Choice == 'S') && p2Choice == '3'))
            return 2; // Player 2 wins
        return -1; // Draw
    }
}
