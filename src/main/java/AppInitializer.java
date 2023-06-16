import controller.MainFormController;
import db.DbConnection;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main-form.fxml"));
        Parent parent = fxmlLoader.load();
        primaryStage.setScene(new Scene(parent));
        primaryStage.getIcons().add(new Image("/view/assests/images/WhatsApp_Image_2023-05-11_at_10.01.28-removebg-preview.png"));
        MainFormController controller = fxmlLoader.getController();
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
