import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;

public class RPSGameView extends RiskView {

    public RPSGameView(Stage stage) {
        super(stage);
        super.disableAllComponents();
        Text t = new Text("sd");
        this.getChildren().add(t);
    }
}
