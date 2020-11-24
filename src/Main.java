import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.Scene;

public class Main extends Application {
    final private int WIDTH = 1280;
    final private int HEIGHT = 1024;

    Scene mainScene;
    //Stage mainStage;

    @Override
    public void start(Stage stage) {
        try {
            MainMenuView menuView = new MainMenuView( stage);
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