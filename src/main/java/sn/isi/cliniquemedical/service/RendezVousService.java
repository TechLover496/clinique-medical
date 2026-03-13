package sn.isi.cliniquemedical.service;

import sn.isi.cliniquemedical.model.RendezVous;
import sn.isi.cliniquemedical.model.StatutRendezVous;
import sn.isi.cliniquemedical.repository.RendezVousRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RendezVousService {

    private final RendezVousRepository rendezVousRepository = new RendezVousRepository();

    public RendezVous planifier(RendezVous r) {
        if (rendezVousRepository.existsConflict(r.getMedecin().getId(), r.getDateHeure())) {
            throw new IllegalStateException("Ce médecin a déjà un rendez-vous à cet horaire !");
        }
        return rendezVousRepository.save(r);
    }

    public RendezVous modifier(RendezVous r) {
        if (rendezVousRepository.existsConflict(r.getMedecin().getId(), r.getDateHeure())) {
            throw new IllegalStateException("Ce médecin a déjà un rendez-vous à cet horaire !");
        }
        return rendezVousRepository.update(r);
    }

    public void annuler(Long id) {
        Optional<RendezVous> opt = rendezVousRepository.findById(id);
        opt.ifPresent(r -> {
            r.setStatut(StatutRendezVous.ANNULE);
            rendezVousRepository.update(r);
        });
    }

    public List<RendezVous> rendezVousDuJour() {
        return rendezVousRepository.findByDate(LocalDate.now());
    }

    public List<RendezVous> parMedecin(Long medecinId) {
        return rendezVousRepository.findByMedecin(medecinId);
    }

    public List<RendezVous> tousLesRendezVous() {
        return rendezVousRepository.findAll();
    }
}