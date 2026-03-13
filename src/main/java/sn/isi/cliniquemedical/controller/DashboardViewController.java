package sn.isi.cliniquemedical.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sn.isi.cliniquemedical.model.RendezVous;
import sn.isi.cliniquemedical.model.StatutPaiement;
import sn.isi.cliniquemedical.service.*;
import sn.isi.cliniquemedical.util.SessionUtilisateur;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardViewController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private Label dateLabel;
    @FXML private Label nbPatients;
    @FXML private Label nbMedecins;
    @FXML private Label nbRdvJour;
    @FXML private Label nbFacturesNonPayees;
    @FXML private TableView<RendezVous> tableRdvJour;
    @FXML private TableColumn<RendezVous, String> colPatient;
    @FXML private TableColumn<RendezVous, String> colMedecin;
    @FXML private TableColumn<RendezVous, String> colHeure;
    @FXML private TableColumn<RendezVous, String> colMotif;
    @FXML private TableColumn<RendezVous, String> colStatut;

    private final PatientService patientService = new PatientService();
    private final MedecinService medecinService = new MedecinService();
    private final RendezVousService rendezVousService = new RendezVousService();
    private final FactureService factureService = new FactureService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Welcome
            String nom = SessionUtilisateur.getUtilisateurConnecte().getNom();
            welcomeLabel.setText("Bienvenue, " + nom + " !");
            dateLabel.setText("Aujourd'hui : " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));

            // Stats
            nbPatients.setText(String.valueOf(patientService.tousLesPatients().size()));
            nbMedecins.setText(String.valueOf(medecinService.tousMedecins().size()));
            nbRdvJour.setText(String.valueOf(rendezVousService.rendezVousDuJour().size()));
            nbFacturesNonPayees.setText(String.valueOf(factureService.parStatut(StatutPaiement.NON_PAYE).size()));

            // Table RDV du jour
            colPatient.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getPatient().getNom() + " " + d.getValue().getPatient().getPrenom()));
            colMedecin.setCellValueFactory(d -> new SimpleStringProperty("Dr. " + d.getValue().getMedecin().getNom()));
            colHeure.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDateHeure().format(formatter)));
            colMotif.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMotif()));
            colStatut.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getStatut().name()));
            tableRdvJour.setItems(FXCollections.observableArrayList(rendezVousService.rendezVousDuJour()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}