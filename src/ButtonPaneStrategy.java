import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public interface ButtonPaneStrategy {
    public void clickAction(AddPlayersView pane, ArrayList<Player> playerList);
}
