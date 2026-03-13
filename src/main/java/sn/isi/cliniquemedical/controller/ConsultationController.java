package sn.isi.cliniquemedical.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import sn.isi.cliniquemedical.model.*;
import sn.isi.cliniquemedical.service.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ConsultationController implements Initializable {

    @FXML private TableView<Consultation> tableConsultations;
    @FXML private TableColumn<Consultation, String> colPatient;
    @FXML private TableColumn<Consultation, String> colMedecin;
    @FXML private TableColumn<Consultation, String> colDate;
    @FXML private TableColumn<Consultation, String> colDiagnostic;
    @FXML private TableColumn<Consultation, String> colActions;

    private final ConsultationService consultationService = new ConsultationService();
    private final PatientService patientService = new PatientService();
    private final MedecinService medecinService = new MedecinService();
    private final RendezVousService rendezVousService = new RendezVousService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colPatient.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPatient().getNom() + " " + d.getValue().getPatient().getPrenom()));
        colMedecin.setCellValueFactory(d -> new SimpleStringProperty("Dr. " + d.getValue().getMedecin().getNom()));
        colDate.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDateConsultation().format(formatter)));
        colDiagnostic.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDiagnostic()));

        colActions.setCellFactory(col -> new TableCell<>() {
            final Button btnVoir = new Button("👁️ Voir");
            final Button btnPdf = new Button("📄 Ordonnance");
            final HBox hbox = new HBox(5, btnVoir, btnPdf);
            {
                btnVoir.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");
                btnPdf.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-cursor: hand;");
                btnVoir.setOnAction(e -> handleVoir(getTableView().getItems().get(getIndex())));
                btnPdf.setOnAction(e -> handlePdf(getTableView().getItems().get(getIndex())));
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });

        charger();
    }

    private void charger() {
        tableConsultations.setItems(FXCollections.observableArrayList(consultationService.toutesLesConsultations()));
    }

    private void handleVoir(Consultation c) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détail Consultation");
        alert.setHeaderText("Patient : " + c.getPatient().getNom() + " " + c.getPatient().getPrenom());
        alert.setContentText(
                "Médecin : Dr. " + c.getMedecin().getNom() + "\n" +
                        "Date : " + c.getDateConsultation().format(formatter) + "\n" +
                        "Diagnostic : " + c.getDiagnostic() + "\n" +
                        "Observations : " + c.getObservations() + "\n" +
                        "Prescription : " + c.getPrescription()
        );
        alert.showAndWait();
    }

    private void handlePdf(Consultation c) {
        String chemin = System.getProperty("user.home") + "/ordonnance_" + c.getId() + ".pdf";
        String result = sn.isi.cliniquemedical.util.PdfUtil.genererOrdonnance(c, chemin);
        if (result != null) {
            new Alert(Alert.AlertType.INFORMATION, "Ordonnance générée : " + chemin).showAndWait();
        } else {
            new Alert(Alert.AlertType.ERROR, "Erreur lors de la génération !").showAndWait();
        }
    }

    @FXML
    private void handleAjouter() {
        Dialog<Consultation> dialog = new Dialog<>();
        dialog.setTitle("Nouvelle Consultation");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<Patient> cbPatient = new ComboBox<>(FXCollections.observableArrayList(patientService.tousLesPatients()));
        cbPatient.setConverter(new javafx.util.StringConverter<>() {
            public String toString(Patient p) { return p == null ? "" : p.getNom() + " " + p.getPrenom(); }
            public Patient fromString(String s) { return null; }
        });

        ComboBox<Medecin> cbMedecin = new ComboBox<>(FXCollections.observableArrayList(medecinService.tousMedecins()));
        cbMedecin.setConverter(new javafx.util.StringConverter<>() {
            public String toString(Medecin m) { return m == null ? "" : "Dr. " + m.getNom(); }
            public Medecin fromString(String s) { return null; }
        });

        ComboBox<RendezVous> cbRendezVous = new ComboBox<>(FXCollections.observableArrayList(rendezVousService.tousLesRendezVous()));
        cbRendezVous.setConverter(new javafx.util.StringConverter<>() {
            public String toString(RendezVous r) { return r == null ? "" : r.getPatient().getNom() + " - " + r.getDateHeure().format(formatter); }
            public RendezVous fromString(String s) { return null; }
        });

        TextArea diagnostic = new TextArea(); diagnostic.setPrefRowCount(2);
        TextArea observations = new TextArea(); observations.setPrefRowCount(2);
        TextArea prescription = new TextArea(); prescription.setPrefRowCount(2);

        grid.add(new Label("Patient:"), 0, 0); grid.add(cbPatient, 1, 0);
        grid.add(new Label("Médecin:"), 0, 1); grid.add(cbMedecin, 1, 1);
        grid.add(new Label("Rendez-vous:"), 0, 2); grid.add(cbRendezVous, 1, 2);
        grid.add(new Label("Diagnostic:"), 0, 3); grid.add(diagnostic, 1, 3);
        grid.add(new Label("Observations:"), 0, 4); grid.add(observations, 1, 4);
        grid.add(new Label("Prescription:"), 0, 5); grid.add(prescription, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                Consultation c = new Consultation();
                c.setPatient(cbPatient.getValue());
                c.setMedecin(cbMedecin.getValue());
                c.setRendezVous(cbRendezVous.getValue());
                c.setDateConsultation(LocalDateTime.now());
                c.setDiagnostic(diagnostic.getText());
                c.setObservations(observations.getText());
                c.setPrescription(prescription.getText());
                return c;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(c -> {
            consultationService.enregistrer(c);
            charger();
        });
    }
}