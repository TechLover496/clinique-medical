package sn.isi.cliniquemedical.controller;

import javafx.scene.layout.GridPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import sn.isi.cliniquemedical.model.Patient;
import sn.isi.cliniquemedical.service.PatientService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PatientController implements Initializable {

    @FXML private TextField searchField;
    @FXML private TableView<Patient> tablePatients;
    @FXML private TableColumn<Patient, String> colNom;
    @FXML private TableColumn<Patient, String> colPrenom;
    @FXML private TableColumn<Patient, String> colDateNaissance;
    @FXML private TableColumn<Patient, String> colSexe;
    @FXML private TableColumn<Patient, String> colTelephone;
    @FXML private TableColumn<Patient, String> colGroupeSanguin;
    @FXML private TableColumn<Patient, String> colActions;

    private final PatientService patientService = new PatientService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNom.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNom()));
        colPrenom.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPrenom()));
        colDateNaissance.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDateNaissance().toString()));
        colSexe.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getSexe().name()));
        colTelephone.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTelephone()));
        colGroupeSanguin.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getGroupeSanguin()));
        colActions.setCellFactory(col -> new TableCell<>() {
            final Button btnModifier = new Button("✏️");
            final Button btnSupprimer = new Button("🗑️");
            final HBox hbox = new HBox(5, btnModifier, btnSupprimer);

            {
                btnModifier.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-cursor: hand;");
                btnSupprimer.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");

                btnModifier.setOnAction(e -> handleModifier(getTableView().getItems().get(getIndex())));
                btnSupprimer.setOnAction(e -> handleSupprimer(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });

        chargerPatients();
    }

    private void chargerPatients() {
        List<Patient> patients = patientService.tousLesPatients();
        tablePatients.setItems(FXCollections.observableArrayList(patients));
    }

    @FXML
    private void handleRechercher() {
        String kw = searchField.getText().trim();
        List<Patient> results = kw.isEmpty() ? patientService.tousLesPatients() : patientService.rechercher(kw);
        tablePatients.setItems(FXCollections.observableArrayList(results));
    }

    @FXML
    private void handleAjouter() {
        showPatientDialog(null);
    }

    private void handleModifier(Patient patient) {
        showPatientDialog(patient);
    }

    private void handleSupprimer(Patient patient) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer " + patient.getNom() + " ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                patientService.supprimer(patient.getId());
                chargerPatients();
            }
        });
    }

    private void showPatientDialog(Patient patient) {
        Dialog<Patient> dialog = new Dialog<>();
        dialog.setTitle(patient == null ? "Nouveau Patient" : "Modifier Patient");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nom = new TextField(patient != null ? patient.getNom() : "");
        TextField prenom = new TextField(patient != null ? patient.getPrenom() : "");
        TextField telephone = new TextField(patient != null ? patient.getTelephone() : "");
        TextField adresse = new TextField(patient != null ? patient.getAdresse() : "");
        TextField groupeSanguin = new TextField(patient != null ? patient.getGroupeSanguin() : "");
        DatePicker dateNaissance = new DatePicker(patient != null ? patient.getDateNaissance() : null);
        ComboBox<String> sexe = new ComboBox<>(FXCollections.observableArrayList("MASCULIN", "FEMININ"));
        if (patient != null) sexe.setValue(patient.getSexe().name());
        TextArea antecedents = new TextArea(patient != null ? patient.getAntecedentsMedicaux() : "");
        antecedents.setPrefRowCount(3);

        grid.add(new Label("Nom:"), 0, 0); grid.add(nom, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1); grid.add(prenom, 1, 1);
        grid.add(new Label("Date Naissance:"), 0, 2); grid.add(dateNaissance, 1, 2);
        grid.add(new Label("Sexe:"), 0, 3); grid.add(sexe, 1, 3);
        grid.add(new Label("Téléphone:"), 0, 4); grid.add(telephone, 1, 4);
        grid.add(new Label("Adresse:"), 0, 5); grid.add(adresse, 1, 5);
        grid.add(new Label("Groupe Sanguin:"), 0, 6); grid.add(groupeSanguin, 1, 6);
        grid.add(new Label("Antécédents:"), 0, 7); grid.add(antecedents, 1, 7);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                Patient p = patient != null ? patient : new Patient();
                p.setNom(nom.getText());
                p.setPrenom(prenom.getText());
                p.setDateNaissance(dateNaissance.getValue());
                p.setSexe(sn.isi.cliniquemedical.model.Sexe.valueOf(sexe.getValue()));
                p.setTelephone(telephone.getText());
                p.setAdresse(adresse.getText());
                p.setGroupeSanguin(groupeSanguin.getText());
                p.setAntecedentsMedicaux(antecedents.getText());
                return p;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(p -> {
            if (patient == null) patientService.ajouter(p);
            else patientService.modifier(p);
            chargerPatients();
        });
    }
}