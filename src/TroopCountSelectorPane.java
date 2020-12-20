import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

public class TroopCountSelectorPane extends VBox {
    private Button lessButton;
    private Button moreButton;
    private Button backButton;
    private Button getNumButton;
    private Button buildAirportButton;

    private Label troopCountLabel;

    private HBox soldierImg;
    private HBox switches;
    private VBox buttons;

    private Label soldier;

    public TroopCountSelectorPane(){
        String styleBackground = "-fx-background-color: #ff6666;" +
                "-fx-border-color: #00ccff";
        String styleBackgroundButton = "-fx-background-color: #ff6666;";

        troopCountLabel = new Label("   1  ");
        soldier = new Label();

        //panes initialize
        soldierImg = new HBox();
        switches = new HBox();
        buttons = new VBox();

        //buttons initialize
        lessButton = new Button();
        moreButton = new Button();
        backButton = new Button("Back");
        getNumButton = new Button("Place");
        buildAirportButton = new Button("Build Airport");

        //buttons set size
        backButton.setPrefSize(400, 75);
        getNumButton.setPrefSize(400, 75);
        buildAirportButton.setPrefSize(400, 75);

        //fonts for buttons
        backButton.setFont(Font.font("Snap ITC", 30));
        getNumButton.setFont(Font.font("Snap ITC", 30));
        buildAirportButton.setFont(Font.font("Snap ITC", 30));

        //set background for buttons
        backButton.setStyle(styleBackground);
        getNumButton.setStyle(styleBackground);
        buildAirportButton.setStyle(styleBackground);

        //graphics for buttons
        ImageView lessIcon = new ImageView(new Image("icons/less_icon.png"));
        lessIcon.setFitHeight(50);
        lessIcon.setFitWidth(50);
        lessButton.setGraphic(lessIcon);
        lessButton.setStyle(styleBackgroundButton);

        ImageView moreIcon = new ImageView(new Image("icons/more_icon.png"));
        moreIcon.setFitWidth(50);
        moreIcon.setFitHeight(50);
        moreButton.setGraphic(moreIcon);
        moreButton.setStyle(styleBackgroundButton);

        ImageView backImg = new ImageView(new Image("icons/back_arrow_icon.png"));
        backImg.setFitWidth(50);
        backImg.setFitHeight(50);
        backButton.setGraphic(backImg);

        ImageView airport = new ImageView(new Image("icons/airport_icon.png"));
        airport.setFitHeight(50);
        airport.setFitWidth(50);
        buildAirportButton.setGraphic(airport);

        ImageView place = new ImageView(new Image("icons/troop_icon.png"));
        place.setFitWidth(50);
        place.setFitHeight(50);
        getNumButton.setGraphic(place);

        //font for troop count
        troopCountLabel.setFont(javafx.scene.text.Font.font("Snap ITC", 30));

        //set soldier img
        soldier.setGraphic(new ImageView(new Image("icons/troop_icon.png")));

        //add to soldierImg pane
        soldierImg.setStyle(styleBackground);
        soldierImg.setMaxSize(100,100);
        soldierImg.getChildren().add(soldier);
        soldierImg.setAlignment(Pos.CENTER);

        //add to switches pane
        switches.setStyle(styleBackground);
        switches.setMaxSize(250,75);
        switches.getChildren().addAll(lessButton, troopCountLabel, moreButton);
        switches.setAlignment(Pos.CENTER);

        //add panes to main pane
        buttons.setAlignment(Pos.CENTER);
        this.getChildren().addAll(soldierImg, switches, getNumButton);
        this.setAlignment(Pos.CENTER);
    }

    public void addTroopCountSelectorPane(RiskGame.GameMode mode) {
        switch (mode) {
            case SoldierAllocationInit:
            case SoldierAllocationMode: {
                getNumButton.setText("Place");
                buttons.getChildren().addAll(buildAirportButton, backButton);
                this.getChildren().add(buttons);
                break;
            }
            case AttackMode: {
                getNumButton.setText("Attack");
                buttons.getChildren().addAll(backButton);
                this.getChildren().add(buttons);
                break;
            }
            case FortifyMode: {
                getNumButton.setText("Place");
                buttons.getChildren().addAll(backButton);
                this.getChildren().add(buttons);
                break;
            }
        }
    }

    public Button getBackButton()
    {
        return backButton;
    }

    public Button getLessButton()
    {
        return lessButton;
    }

    public Button getMoreButton()
    {
        return moreButton;
    }

    public Button getNumButton()
    {
        return getNumButton;
    }

    public Button getBuildAirportButton()
    {
        return buildAirportButton;
    }

    public Label getTroopCountLabel()
    {
        return troopCountLabel;
    }

    public void removeButtons() {
        buttons.getChildren().clear();
        this.getChildren().remove(buttons);
    }
}
