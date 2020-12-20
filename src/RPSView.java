import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class RPSView extends VBox {
    private Label player1;
    private Label player2;
    private Label player1TroopCount;
    private Label player2TroopCount;
    private Label winner;

    private VBox player1Count;
    private VBox player2Count;
    private HBox resultContainer;

    final String styleBackground = "-fx-background-color: #ff6666;" +
            "-fx-border-color: #00ccff";
    public RPSView() {


        VBox mainContainer = new VBox();

        HBox rpsContainer = new HBox();
        VBox rockContainer = new VBox();
        VBox paperContainer = new VBox();
        VBox scissorsContainer = new VBox();

        resultContainer = new HBox();
        player1Count = new VBox();
        player2Count = new VBox();

        //mainContainer.setStyle(styleBackground);
        //rpsContainer.setStyle(styleBackground);
        rockContainer.setStyle(styleBackground);
        paperContainer.setStyle(styleBackground);
        scissorsContainer.setStyle(styleBackground);
        resultContainer.setStyle(styleBackground);
        player1Count.setStyle(styleBackground);
        player2Count.setStyle(styleBackground);

        /////////// HBOX RPS CONTAINER ////////////////////////////////////////////
        //rock
        Label rock = new Label();
        rock.setFont(Font.font("Snap ITC", 30));
        rock.setAlignment(Pos.CENTER);

        Label rockLetter = new Label("  A");
        rockLetter.setFont(Font.font("Snap ITC", 30));
        rockLetter.setAlignment(Pos.CENTER);

        ImageView rockLetterAttackImg = new ImageView(new Image("icons/sword_icon.png"));
        rockLetterAttackImg.setFitHeight(30);
        rockLetterAttackImg.setFitWidth(30);
        rockLetter.setGraphic(rockLetterAttackImg);

        ImageView rockImg = new ImageView(new Image("icons/rock_rps.png"));
        rockImg.setFitHeight(100);
        rockImg.setFitWidth(100);
        rock.setGraphic(rockImg);

        ImageView rockNumberDefendImg = new ImageView(new Image("icons/shield_icon.png"));
        rockNumberDefendImg.setFitHeight(30);
        rockNumberDefendImg.setFitWidth(30);

        Label rockNumber = new Label("  1");
        rockNumber.setFont(Font.font("Snap ITC", 30));
        rockNumber.setAlignment(Pos.CENTER);
        rockNumber.setGraphic(rockNumberDefendImg);


        rockContainer.getChildren().addAll(rock, rockLetter, rockNumber);

        //paper
        Label paper = new Label();
        paper.setFont(Font.font("Snap ITC", 30));
        paper.setAlignment(Pos.CENTER);

        Label paperLetter = new Label("  S");
        paperLetter.setFont(Font.font("Snap ITC", 30));
        paperLetter.setAlignment(Pos.CENTER);

        ImageView paperLetterAttackImg = new ImageView(new Image("icons/sword_icon.png"));
        paperLetterAttackImg.setFitHeight(30);
        paperLetterAttackImg.setFitWidth(30);
        paperLetter.setGraphic(paperLetterAttackImg);

        ImageView paperNumberDefenceImg = new ImageView(new Image("icons/shield_icon.png"));
        paperNumberDefenceImg.setFitHeight(30);
        paperNumberDefenceImg.setFitWidth(30);

        Label paperNumber = new Label("  2");
        paperNumber.setFont(Font.font("Snap ITC", 30));
        paperNumber.setAlignment(Pos.CENTER);
        paperNumber.setGraphic(paperNumberDefenceImg);

        ImageView paperImg = new ImageView(new Image("icons/paper_rps.png"));
        paperImg.setFitHeight(100);
        paperImg.setFitWidth(100);
        paper.setGraphic(paperImg);

        paperContainer.getChildren().addAll(paper, paperLetter, paperNumber);

        //scissors
        Label scissors = new Label();
        scissors.setFont(Font.font("Snap ITC", 30));
        scissors.setAlignment(Pos.CENTER);

        Label scissorsLetter = new Label("  D");
        scissorsLetter.setFont(Font.font("Snap ITC", 30));
        scissorsLetter.setAlignment(Pos.CENTER);

        ImageView scissorsLetterAttackImg = new ImageView(new Image("icons/sword_icon.png"));
        scissorsLetterAttackImg.setFitHeight(30);
        scissorsLetterAttackImg.setFitWidth(30);
        scissorsLetter.setGraphic(scissorsLetterAttackImg);

        ImageView scissorsNumberDefenceImg = new ImageView(new Image("icons/shield_icon.png"));
        scissorsNumberDefenceImg.setFitHeight(30);
        scissorsNumberDefenceImg.setFitWidth(30);

        Label scissorsNumber = new Label("  3");
        scissorsNumber.setFont(Font.font("Snap ITC", 30));
        scissorsNumber.setAlignment(Pos.CENTER);
        scissorsNumber.setGraphic(scissorsNumberDefenceImg);

        ImageView scissorsImg = new ImageView(new Image("icons/scissors_rps.png"));
        scissorsImg.setFitHeight(100);
        scissorsImg.setFitWidth(100);
        scissors.setGraphic(scissorsImg);

        scissorsContainer.getChildren().addAll(scissors, scissorsLetter, scissorsNumber);

        rockContainer.setAlignment(Pos.CENTER);
        paperContainer.setAlignment(Pos.CENTER);
        scissorsContainer.setAlignment(Pos.CENTER);

        rpsContainer.getChildren().addAll(rockContainer, paperContainer, scissorsContainer);
        ////////////////////////////////////////////////////////////////////////////////

        /////////////////////////// HBOX RESULT CONTAINER //////////////////////////////////
        //player 1
        player1 = new Label("PLAYER 1");
        player1.setFont(Font.font("Snap ITC", 30));
        player1.setAlignment(Pos.CENTER);

        player1TroopCount = new Label("");
        player1TroopCount.setFont(Font.font("Snap ITC", 30));
        player1TroopCount.setAlignment(Pos.CENTER);

        player2 = new Label("PLAYER 2");
        player2.setFont(Font.font("Snap ITC", 30));
        player2.setAlignment(Pos.CENTER);

        player2TroopCount = new Label("");
        player2TroopCount.setFont(Font.font("Snap ITC", 30));
        player2TroopCount.setAlignment(Pos.CENTER);

        player1.setText("PLAYER 1");
        player2.setText("PLAYER 2");

        player1TroopCount.setAlignment(Pos.CENTER);
        player2TroopCount.setAlignment(Pos.CENTER);

        player1Count.getChildren().addAll(player1, player1TroopCount);
        player2Count.getChildren().addAll(player2, player2TroopCount);

        player1Count.setAlignment(Pos.CENTER);
        player2Count.setAlignment(Pos.CENTER);

        resultContainer.getChildren().addAll(player1, player2);
        ////////////////////////////////////////////////////////////////////////////////////
        winner = new Label();
        mainContainer.getChildren().addAll(rpsContainer, resultContainer);
        this.getChildren().add(mainContainer);

        rpsContainer.setMaxSize(300,200);
        rpsContainer.setAlignment(Pos.CENTER);

        resultContainer.setMaxSize(200, 60);
        resultContainer.setAlignment(Pos.CENTER);
        this.setMaxSize(300,600);
        this.setAlignment(Pos.CENTER);
    }

    public void updateTroopCounts(int attackerTroopCount, int defenderTroopCount) {

        player1TroopCount.setAlignment(Pos.CENTER);
        player2TroopCount.setAlignment(Pos.CENTER);

        ImageView sword = new ImageView(new Image("icons/sword_icon.png"));
        sword.setFitWidth(50);
        sword.setFitHeight(50);
        player1.setGraphic(sword);

        ImageView shield = new ImageView(new Image("icons/shield_icon.png"));
        shield.setFitHeight(50);
        shield.setFitWidth(50);
        player2.setGraphic(shield);

        player1.setText("" + attackerTroopCount + "    ");
        player2.setText("" + defenderTroopCount);

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
