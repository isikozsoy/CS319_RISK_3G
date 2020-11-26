import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RPSGameView extends RiskView {
    final private String TEXT_STYLE = "-fx-stroke: #f50000;" + "-fx-stroke-width: 3;";
    final String DIRECTORY_NAME = "/icons/";
    final String FILE_NAME_HELPER = "_rps.png";
    final String[] RPSOptions = {"rock", "paper", "scissors"};

    Text optRock;
    Text optPaper;
    Text optScissor;
    Text playerName;

    public RPSGameView(Stage stage) {
        super(stage);
        disableAllComponents();
        optRock = new Text("");
        optPaper = new Text(600, 900, "");
        optScissor = new Text(650, 900, "");
        playerName = new Text(640, 460, "");
        addRPSComps();
        showPlayer1Option();
    }

    private void addRPSComps() {
        FlowPane RPSIcons = new FlowPane();
        RPSIcons.setOrientation(Orientation.HORIZONTAL);
        RPSIcons.setAlignment(Pos.CENTER);
        for (int i = 0; i < RPSOptions.length; i++) {
            Image image = new Image(DIRECTORY_NAME + RPSOptions[i] + FILE_NAME_HELPER);
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            RPSIcons.getChildren().add(imageView);
        }
        this.getChildren().addAll(playerName, RPSIcons, optRock, optPaper, optScissor);
        optRock.setLayoutX(550);
        optRock.setLayoutY(900);
    }

    private void showPlayer1Option() {
        playerName.setText("Player 1");
        optRock.setText("a");
        optPaper.setText("s");
        optScissor.setText("d");
    }
}