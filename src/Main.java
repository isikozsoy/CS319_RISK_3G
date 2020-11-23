import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.Scene;

public class Main extends Application {
    final private int WIDTH = 1280;
    final private int HEIGHT = 1024;

    @Override
    public void start(Stage stage) {
        try {
            MainMenuView menuView = new MainMenuView( stage);
            Scene newScene = new Scene(menuView, WIDTH, HEIGHT);
            Stage mainStage = new Stage();
            stage.setScene( newScene);

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