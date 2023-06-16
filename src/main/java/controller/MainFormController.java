package controller;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MainFormController {

    public Label lblMenu;
    public Label lblDescription;
    public AnchorPane root;

    public void navigate(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();

            Parent root = null;

            switch (icon.getId()) {
                case "Appointment":
                    root = FXMLLoader.load(this.getClass().getResource("/view/manage-appointment-from.fxml"));
                    break;
                case "doctor":
                    root = FXMLLoader.load(this.getClass().getResource("/view/manage-doctor-from.fxml"));
                    break;
                case "Patients":
                    root = FXMLLoader.load(this.getClass().getResource("/view/patient-manage-from.fxml"));
                    break;
                case "report":
                    root = FXMLLoader.load(this.getClass().getResource("/view/report-manage-from.fxml"));
                    break;
            }

            if (root != null) {
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.root.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();

            }
        }

    }

    public void playMouseEnterAnimation(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();

            switch (icon.getId()) {
                case "Appointment":
                    lblMenu.setText("Appointment");
                    lblDescription.setText("Click to add,search or view Appointment");
                    break;
                case "doctor":
                    lblMenu.setText("Manage Doctors");
                    lblDescription.setText("Register - edit -or view Doctors Details");
                    break;
                case "Patients":
                    lblMenu.setText("Manage Patients ");
                    lblDescription.setText("edit - search or view Patients Details");
                    break;
                case "report":
                    lblMenu.setText(" Day  Summery ");
                    lblDescription.setText("Print   -   Mail    -   view");
                    break;
            }
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.CORNFLOWERBLUE);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);
        }
    }

    public void playMouseExitAnimation(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();
            icon.setEffect(null);
            lblMenu.setText("Welcome");
            lblDescription.setText("Please select one of above main operations to proceed");
        }
    }
}
