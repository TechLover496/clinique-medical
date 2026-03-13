package sn.isi.cliniquemedical.service;

import sn.isi.cliniquemedical.model.Medecin;
import sn.isi.cliniquemedical.repository.MedecinRepository;
import java.util.List;
import java.util.Optional;

public class MedecinService {

    private final MedecinRepository medecinRepository = new MedecinRepository();

    public Medecin ajouter(Medecin m) {
        return medecinRepository.save(m);
    }

    public Medecin modifier(Medecin m) {
        return medecinRepository.update(m);
    }

    public void supprimer(Long id) {
        medecinRepository.delete(id);
    }

    public Optional<Medecin> trouverParId(Long id) {
        return medecinRepository.findById(id);
    }

    public List<Medecin> tousMedecins() {
        return medecinRepository.findAll();
    }
}