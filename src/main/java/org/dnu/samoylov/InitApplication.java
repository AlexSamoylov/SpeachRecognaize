package org.dnu.samoylov;

import org.dnu.samoylov.controller.main.FxmlController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.dnu.samoylov.util.SpringFXMLLoader;
import sun.security.util.BitArray;


public class InitApplication extends Application {

    public static void main(String[] args) {
        launch(args);

    }


    @Override
    public void start(final Stage stage) throws Exception {
        FxmlController controller = SpringFXMLLoader.load("/fxml/main.fxml");
        Parent parent = (Parent) controller.getView();

        Scene scene = new Scene(parent);
        stage.setTitle("Samoylov PZ-12-1");
        stage.setScene(scene);
        stage.show();
    }
}
