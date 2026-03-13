package sn.isi.cliniquemedical.controller;


import javafx.scene.layout.GridPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import sn.isi.cliniquemedical.model.*;
import sn.isi.cliniquemedical.service.MedecinService;
import sn.isi.cliniquemedical.service.PatientService;
import sn.isi.cliniquemedical.service.RendezVousService;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class RendezVousController implements Initializable {

    @FXML private TableView<RendezVous> tableRendezVous;
    @FXML private TableColumn<RendezVous, String> colPatient;
    @FXML private TableColumn<RendezVous, String> colMedecin;
    @FXML private TableColumn<RendezVous, String> colDateHeure;
    @FXML private TableColumn<RendezVous, String> colMotif;
    @FXML private TableColumn<RendezVous, String> colStatut;
    @FXML private TableColumn<RendezVous, String> colActions;

    private final RendezVousService rendezVousService = new RendezVousService();
    private final PatientService patientService = new PatientService();
    private final MedecinService medecinService = new MedecinService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colPatient.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPatient().getNom() + " " + d.getValue().getPatient().getPrenom()));
        colMedecin.setCellValueFactory(d -> new SimpleStringProperty("Dr. " + d.getValue().getMedecin().getNom()));
        colDateHeure.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDateHeure().format(formatter)));
        colMotif.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMotif()));
        colStatut.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatut().name()));
        colActions.setCellFactory(col -> new TableCell<>() {
            final Button btnAnnuler = new Button("❌ Annuler");
            final HBox hbox = new HBox(5, btnAnnuler);
            {
                btnAnnuler.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
                btnAnnuler.setOnAction(e -> {
                    RendezVous r = getTableView().getItems().get(getIndex());
                    rendezVousService.annuler(r.getId());
                    charger(rendezVousService.tousLesRendezVous());
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });
        try{
        charger(rendezVousService.tousLesRendezVous());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void charger(List<RendezVous> list) {
        tableRendezVous.setItems(FXCollections.observableArrayList(list));
    }

    @FXML private void handleTous() { charger(rendezVousService.tousLesRendezVous()); }

    @FXML private void handleAujourdhui() { charger(rendezVousService.rendezVousDuJour()); }

    @FXML
    private void handleAjouter() {
        Dialog<RendezVous> dialog = new Dialog<>();
        dialog.setTitle("Nouveau Rendez-vous");

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

        DatePicker datePicker = new DatePicker();
        TextField heureField = new TextField();
        heureField.setPromptText("HH:mm");
        TextField motifField = new TextField();

        grid.add(new Label("Patient:"), 0, 0); grid.add(cbPatient, 1, 0);
        grid.add(new Label("Médecin:"), 0, 1); grid.add(cbMedecin, 1, 1);
        grid.add(new Label("Date:"), 0, 2); grid.add(datePicker, 1, 2);
        grid.add(new Label("Heure (HH:mm):"), 0, 3); grid.add(heureField, 1, 3);
        grid.add(new Label("Motif:"), 0, 4); grid.add(motifField, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    RendezVous r = new RendezVous();
                    r.setPatient(cbPatient.getValue());
                    r.setMedecin(cbMedecin.getValue());
                    String[] heure = heureField.getText().split(":");
                    r.setDateHeure(LocalDateTime.of(datePicker.getValue(), java.time.LocalTime.of(Integer.parseInt(heure[0]), Integer.parseInt(heure[1]))));
                    r.setMotif(motifField.getText());
                    return r;
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Erreur de saisie !").showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(r -> {
            try {
                rendezVousService.planifier(r);
                charger(rendezVousService.tousLesRendezVous());
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        });
    }
}