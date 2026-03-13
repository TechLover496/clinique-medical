package sn.isi.cliniquemedical.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import sn.isi.cliniquemedical.model.Consultation;
import sn.isi.cliniquemedical.model.Facture;

import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

public class PdfUtil {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

    private PdfUtil() {}

    // ===== ORDONNANCE =====
    public static String genererOrdonnance(Consultation consultation, String cheminFichier) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(cheminFichier));
            document.open();

            // En-tête clinique
            ajouterEntete(document, "ORDONNANCE MÉDICALE");

            // Infos patient
            document.add(new Paragraph("Patient : " + consultation.getPatient().getNom() + " " + consultation.getPatient().getPrenom(), BOLD_FONT));
            document.add(new Paragraph("Date de naissance : " + consultation.getPatient().getDateNaissance(), NORMAL_FONT));
            document.add(new Paragraph("Date : " + consultation.getDateConsultation().format(formatter), NORMAL_FONT));
            document.add(new Paragraph("Médecin : Dr. " + consultation.getMedecin().getNom() + " " + consultation.getMedecin().getPrenom(), NORMAL_FONT));
            document.add(new Paragraph("Spécialité : " + consultation.getMedecin().getSpecialite(), NORMAL_FONT));
            document.add(Chunk.NEWLINE);

            // Séparateur
            ajouterSeparateur(document);

            // Diagnostic
            document.add(new Paragraph("Diagnostic :", BOLD_FONT));
            document.add(new Paragraph(consultation.getDiagnostic(), NORMAL_FONT));
            document.add(Chunk.NEWLINE);

            // Prescription
            document.add(new Paragraph("Prescription :", BOLD_FONT));
            document.add(new Paragraph(consultation.getPrescription(), NORMAL_FONT));
            document.add(Chunk.NEWLINE);

            // Observations
            document.add(new Paragraph("Observations :", BOLD_FONT));
            document.add(new Paragraph(consultation.getObservations(), NORMAL_FONT));
            document.add(Chunk.NEWLINE);

            ajouterPiedDePage(document);
            document.close();
            return cheminFichier;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ===== FACTURE =====
    public static String genererFacture(Facture facture, String cheminFichier) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(cheminFichier));
            document.open();

            ajouterEntete(document, "FACTURE");

            // Infos patient
            document.add(new Paragraph("Patient : " + facture.getConsultation().getPatient().getNom() + " " + facture.getConsultation().getPatient().getPrenom(), BOLD_FONT));
            document.add(new Paragraph("Date : " + facture.getDateFacture().format(formatter), NORMAL_FONT));
            document.add(Chunk.NEWLINE);

            ajouterSeparateur(document);

            // Tableau montant
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            ajouterCelluleHeader(table, "Description");
            ajouterCelluleHeader(table, "Montant");

            ajouterCellule(table, "Consultation médicale");
            ajouterCellule(table, facture.getMontantTotal() + " FCFA");

            document.add(table);
            document.add(Chunk.NEWLINE);

            // Infos paiement
            document.add(new Paragraph("Mode de paiement : " + facture.getModePaiement(), BOLD_FONT));
            document.add(new Paragraph("Statut : " + facture.getStatutPaiement().name(), BOLD_FONT));
            document.add(Chunk.NEWLINE);

            ajouterPiedDePage(document);
            document.close();
            return cheminFichier;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ===== HELPERS =====
    private static void ajouterEntete(Document doc, String titre) throws DocumentException {
        Paragraph clinique = new Paragraph("🏥 CLINIQUE MÉDICALE", TITLE_FONT);
        clinique.setAlignment(Element.ALIGN_CENTER);
        doc.add(clinique);

        Paragraph sousTitre = new Paragraph(titre, new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.GRAY));
        sousTitre.setAlignment(Element.ALIGN_CENTER);
        doc.add(sousTitre);
        doc.add(Chunk.NEWLINE);
    }

    private static void ajouterSeparateur(Document doc) throws DocumentException {
        Paragraph separateur = new Paragraph("________________________________________________");
        separateur.setAlignment(Element.ALIGN_CENTER);
        doc.add(separateur);
        doc.add(Chunk.NEWLINE);
    }

    private static void ajouterPiedDePage(Document doc) throws DocumentException {
        doc.add(Chunk.NEWLINE);
        ajouterSeparateur(doc);
        Paragraph pied = new Paragraph("Clinique Médicale - Tous droits réservés", new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.GRAY));
        pied.setAlignment(Element.ALIGN_CENTER);
        doc.add(pied);
    }

    private static void ajouterCelluleHeader(PdfPTable table, String texte) {
        PdfPCell cell = new PdfPCell(new Phrase(texte, HEADER_FONT));
        cell.setBackgroundColor(new BaseColor(52, 73, 94));
        cell.setPadding(8);
        table.addCell(cell);
    }

    private static void ajouterCellule(PdfPTable table, String texte) {
        PdfPCell cell = new PdfPCell(new Phrase(texte, NORMAL_FONT));
        cell.setPadding(8);
        table.addCell(cell);
    }
}