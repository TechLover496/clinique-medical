package sn.isi.cliniquemedical.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import sn.isi.cliniquemedical.model.*;
import sn.isi.cliniquemedical.service.ConsultationService;
import sn.isi.cliniquemedical.service.FactureService;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class FactureController implements Initializable {

    @FXML private TableView<Facture> tableFactures;
    @FXML private TableColumn<Facture, String> colPatient;
    @FXML private TableColumn<Facture, String> colDate;
    @FXML private TableColumn<Facture, String> colMontant;
    @FXML private TableColumn<Facture, String> colMode;
    @FXML private TableColumn<Facture, String> colStatut;
    @FXML private TableColumn<Facture, String> colActions;

    private final FactureService factureService = new FactureService();
    private final ConsultationService consultationService = new ConsultationService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colPatient.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getConsultation().getPatient().getNom() + " " +
                        d.getValue().getConsultation().getPatient().getPrenom()));
        colDate.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDateFacture().format(formatter)));
        colMontant.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMontantTotal() + " FCFA"));
        colMode.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getModePaiement()));
        colStatut.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatutPaiement().name()));

        colActions.setCellFactory(col -> new TableCell<>() {
            final Button btnPayer = new Button(" Marquer Payé");
            final Button btnPdf = new Button(" Facture PDF");
            final HBox hbox = new HBox(5, btnPayer, btnPdf);
            {
                btnPayer.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-cursor: hand;");
                btnPdf.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-cursor: hand;");
                btnPayer.setOnAction(e -> {
                    Facture f = getTableView().getItems().get(getIndex());
                    factureService.marquerPaye(f.getId());
                    charger(factureService.toutesLesFactures());
                });
                btnPdf.setOnAction(e -> {
                    Facture f = getTableView().getItems().get(getIndex());
                    String chemin = System.getProperty("user.home") + "/facture_" + f.getId() + ".pdf";
                    String result = sn.isi.cliniquemedical.util.PdfUtil.genererFacture(f, chemin);
                    if (result != null) {
                        new Alert(Alert.AlertType.INFORMATION, "Facture générée : " + chemin).showAndWait();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Erreur lors de la génération !").showAndWait();
                    }
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });

        charger(factureService.toutesLesFactures());
    }

    private void charger(List<Facture> list) {
        tableFactures.setItems(FXCollections.observableArrayList(list));
    }

    @FXML private void handleToutes() { charger(factureService.toutesLesFactures()); }
    @FXML private void handlePayees() { charger(factureService.parStatut(StatutPaiement.PAYE)); }
    @FXML private void handleNonPayees() { charger(factureService.parStatut(StatutPaiement.NON_PAYE)); }

    @FXML
    private void handleAjouter() {
        Dialog<Facture> dialog = new Dialog<>();
        dialog.setTitle("Nouvelle Facture");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<Consultation> cbConsultation = new ComboBox<>(
                FXCollections.observableArrayList(consultationService.toutesLesConsultations()));
        cbConsultation.setConverter(new javafx.util.StringConverter<>() {
            public String toString(Consultation c) {
                return c == null ? "" : c.getPatient().getNom() + " - " + c.getDateConsultation().format(formatter);
            }
            public Consultation fromString(String s) { return null; }
        });

        TextField montantField = new TextField();
        montantField.setPromptText("Montant en FCFA");

        ComboBox<String> cbMode = new ComboBox<>(
                FXCollections.observableArrayList("ESPECES", "CARTE", "VIREMENT"));

        grid.add(new Label("Consultation:"), 0, 0); grid.add(cbConsultation, 1, 0);
        grid.add(new Label("Montant (FCFA):"), 0, 1); grid.add(montantField, 1, 1);
        grid.add(new Label("Mode Paiement:"), 0, 2); grid.add(cbMode, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    Facture f = new Facture();
                    f.setConsultation(cbConsultation.getValue());
                    f.setMontantTotal(Double.parseDouble(montantField.getText()));
                    f.setModePaiement(cbMode.getValue());
                    return f;
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Erreur de saisie !").showAndWait();
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(f -> {
            factureService.generer(f);
            charger(factureService.toutesLesFactures());
        });
    }
}