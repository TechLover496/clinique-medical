package sn.isi.cliniquemedical.service;

import sn.isi.cliniquemedical.model.Facture;
import sn.isi.cliniquemedical.model.StatutPaiement;
import sn.isi.cliniquemedical.repository.FactureRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class FactureService {

    private final FactureRepository factureRepository = new FactureRepository();

    public Facture generer(Facture f) {
        f.setDateFacture(LocalDateTime.now());
        f.setStatutPaiement(StatutPaiement.NON_PAYE);
        return factureRepository.save(f);
    }

    public Facture marquerPaye(Long id) {
        Optional<Facture> opt = factureRepository.findById(id);
        if (opt.isEmpty()) throw new IllegalStateException("Facture introuvable !");
        Facture f = opt.get();
        f.setStatutPaiement(StatutPaiement.PAYE);
        return factureRepository.update(f);
    }

    public List<Facture> toutesLesFactures() {
        return factureRepository.findAll();
    }

    public List<Facture> parStatut(StatutPaiement statut) {
        return factureRepository.findByStatut(statut);
    }

    public Optional<Facture> trouverParId(Long id) {
        return factureRepository.findById(id);
    }
}