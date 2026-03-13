package sn.isi.cliniquemedical.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sn.isi.cliniquemedical.service.AuthService;

public class DashboardController {

    @FXML
    private StackPane mainContent;

    private final AuthService authService = new AuthService();

    @FXML
    private void showDashboard() {
        loadView("/sn/isi/cliniquemedical/views/dashboard_view.fxml");
    }

    @FXML
    private void showPatients() {
        loadView("/sn/isi/cliniquemedical/views/patients.fxml");
    }

    @FXML
    private void showRendezVous() {
        System.out.println("Bouton Rendez-vous cliqué !");
        loadView("/sn/isi/cliniquemedical/views/rendezVous.fxml");
    }

    @FXML
    private void showConsultations() {
        loadView("/sn/isi/cliniquemedical/views/consultations.fxml");
    }

    @FXML
    private void showFactures() {
        loadView("/sn/isi/cliniquemedical/views/factures.fxml");
    }

    @FXML
    private void handleLogout() {
        try {
            authService.deconnecter();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sn/isi/cliniquemedical/views/login.fxml"));
            Stage stage = (Stage) mainContent.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setMaximized(false);
            stage.setTitle("Clinique Médicale - Connexion");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlPath) {
        System.out.println("Chargement : " + fxmlPath);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (loader.getLocation() == null) {
                System.err.println("FXML introuvable : " + fxmlPath);
                return;
            }
            System.out.println("FXML trouvé !");
            Pane pane = loader.load();
            mainContent.getChildren().setAll(pane);
            pane.prefWidthProperty().bind(mainContent.widthProperty());
            pane.prefHeightProperty().bind(mainContent.heightProperty());
        } catch (Exception e) {
            System.err.println("ERREUR : " + e.getMessage());
            e.printStackTrace();
        }
    }
}