import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class RPSView extends GridPane {

    private Text attackerTroops;
    private Text defenderTroops;
    private Text winner;


    public RPSView() {

        add(new ImageView(new Image("icons/rock_rps.png")), 0, 0);
        add(new ImageView(new Image("icons/paper_rps.png")), 1, 0);
        add(new ImageView(new Image("icons/scissors_rps.png")), 2, 0);

        Text textA = new Text("A");
        textA.setFont(Font.font("Snap ITC", 30));
        Text textS = new Text("S");
        textS.setFont(Font.font("Snap ITC", 30));
        Text textD = new Text("D");
        textD.setFont(Font.font("Snap ITC", 30));

        attackerTroops = new Text();
        attackerTroops.setFont(Font.font("Snap ITC", 30));

        defenderTroops = new Text();
        defenderTroops.setFont(Font.font("Snap ITC", 30));

/*        add(textA, 0, 1);
        add(textS, 1, 1);
        add(textD, 2, 1);*/

        add(attackerTroops, 1, 1);
        add(defenderTroops, 2, 1);

        winner = new Text();
        winner.setFont(Font.font("Snap ITC", 30));
        add(winner, 1, 5);

        setAlignment(Pos.CENTER);
    }

    public void updateTroopCounts(int attackerTroopCount, int defenderTroopCount) {

        attackerTroops.setText("" + attackerTroopCount);
        defenderTroops.setText("" + defenderTroopCount);
    }

    public void updateWinner(int result) {
        if (result == 1) {
            winner.setText("Attacker Won!");
        } else if (result == 2) {
            winner.setText("Defender Won!");
        } else {
            winner.setText("Draw!");
        }
    }
}
