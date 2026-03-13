package sn.isi.cliniquemedical;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sn.isi.cliniquemedical.util.DataInitializer;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Initialiser les données par défaut
        DataInitializer.init();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sn/isi/cliniquemedical/views/login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Clinique Médicale - Connexion");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}