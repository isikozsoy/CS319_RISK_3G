import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {
    final private int WIDTH = (int) Screen.getPrimary().getBounds().getWidth();
    final private int HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();

    Scene mainScene;
    //Stage mainStage;

    @Override
    public void start(Stage stage) {
        try {
            // stage.setFullScreen(true);
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