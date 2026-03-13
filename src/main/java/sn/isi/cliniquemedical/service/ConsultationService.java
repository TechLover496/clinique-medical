package sn.isi.cliniquemedical.service;

import sn.isi.cliniquemedical.model.Consultation;
import sn.isi.cliniquemedical.model.RendezVous;
import sn.isi.cliniquemedical.model.StatutRendezVous;
import sn.isi.cliniquemedical.repository.ConsultationRepository;
import sn.isi.cliniquemedical.repository.RendezVousRepository;
import java.util.List;
import java.util.Optional;

public class ConsultationService {

    private final ConsultationRepository consultationRepository = new ConsultationRepository();
    private final RendezVousRepository rendezVousRepository = new RendezVousRepository();

    public Consultation enregistrer(Consultation c) {
        Consultation saved = consultationRepository.save(c);
        // Marquer le rendez-vous comme terminé
        RendezVous r = c.getRendezVous();
        if (r != null) {
            r.setStatut(StatutRendezVous.TERMINE);
            rendezVousRepository.update(r);
        }
        return saved;
    }

    public Consultation modifier(Consultation c) {
        return consultationRepository.update(c);
    }

    public Optional<Consultation> trouverParId(Long id) {
        return consultationRepository.findById(id);
    }

    public List<Consultation> toutesLesConsultations() {
        return consultationRepository.findAll();
    }

    public List<Consultation> parPatient(Long patientId) {
        return consultationRepository.findByPatient(patientId);
    }
}