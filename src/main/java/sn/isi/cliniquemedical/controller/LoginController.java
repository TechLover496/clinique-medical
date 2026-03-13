package sn.isi.cliniquemedical.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sn.isi.cliniquemedical.service.AuthService;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs !");
            return;
        }

        if (authService.connecter(username, password)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sn/isi/cliniquemedical/views/dashboard.fxml"));
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(loader.load()));
                stage.setTitle("Clinique Médicale - Dashboard");
                stage.setMaximized(true);
            } catch (Exception e) {
                errorLabel.setText("Erreur de chargement !");
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Identifiants incorrects !");
        }
    }
}