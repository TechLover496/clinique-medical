package sn.isi.cliniquemedical.service;

import sn.isi.cliniquemedical.model.Patient;
import sn.isi.cliniquemedical.repository.PatientRepository;
import java.util.List;
import java.util.Optional;

public class PatientService {

    private final PatientRepository patientRepository = new PatientRepository();

    public Patient ajouter(Patient p) {
        return patientRepository.save(p);
    }

    public Patient modifier(Patient p) {
        return patientRepository.update(p);
    }

    public void supprimer(Long id) {
        patientRepository.delete(id);
    }

    public Optional<Patient> trouverParId(Long id) {
        return patientRepository.findById(id);
    }

    public List<Patient> tousLesPatients() {
        return patientRepository.findAll();
    }

    public List<Patient> rechercher(String keyword) {
        return patientRepository.rechercher(keyword);
    }
}