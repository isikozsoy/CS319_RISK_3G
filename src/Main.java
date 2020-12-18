import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.nio.file.Paths;

public class Main extends Application {
    final private int WIDTH = 1000;
    final private int HEIGHT = 750;

    Scene mainScene;
    //Stage mainStage;

    @Override
    public void start(Stage stage) {
        try {
            MainMenuView menuView = new MainMenuView( stage, WIDTH, HEIGHT);
            mainScene = new Scene(menuView, WIDTH, HEIGHT);
            stage.setScene( mainScene);

            stage.show();
        }
        catch( Exception e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args) {
        launch(args);
    }
}