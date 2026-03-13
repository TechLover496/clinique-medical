package sn.isi.cliniquemedical.util;

import sn.isi.cliniquemedical.model.Role;
import sn.isi.cliniquemedical.model.Utilisateur;
import sn.isi.cliniquemedical.repository.UtilisateurRepository;

public class DataInitializer {

    private DataInitializer() {}

    public static void init() {
        UtilisateurRepository repo = new UtilisateurRepository();

        // Créer admin par défaut si inexistant
        if (repo.findByUsername("admin").isEmpty()) {
            Utilisateur admin = new Utilisateur();
            admin.setUsername("admin");
            admin.setPassword(PasswordUtil.hash("admin123"));
            admin.setNom("Admin");
            admin.setPrenom("Super");
            admin.setRole(Role.ADMIN);
            admin.setActif(true);
            repo.save(admin);
            System.out.println(" Admin créé : admin / admin123");
        }

        // Créer médecin par défaut si inexistant
        if (repo.findByUsername("medecin1").isEmpty()) {
            Utilisateur medecin = new Utilisateur();
            medecin.setUsername("medecin1");
            medecin.setPassword(PasswordUtil.hash("medecin123"));
            medecin.setNom("Diallo");
            medecin.setPrenom("Mamadou");
            medecin.setRole(Role.MEDECIN);
            medecin.setActif(true);
            repo.save(medecin);
            System.out.println(" Médecin créé : medecin1 / medecin123");
        }
    }
}