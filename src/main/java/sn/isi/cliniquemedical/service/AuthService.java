package sn.isi.cliniquemedical.service;

import sn.isi.cliniquemedical.model.Utilisateur;
import sn.isi.cliniquemedical.repository.UtilisateurRepository;
import sn.isi.cliniquemedical.util.PasswordUtil;
import sn.isi.cliniquemedical.util.SessionUtilisateur;
import java.util.Optional;

public class AuthService {

    private final UtilisateurRepository utilisateurRepository = new UtilisateurRepository();

    public boolean connecter(String username, String password) {
        Optional<Utilisateur> opt = utilisateurRepository.findByUsername(username);
        if (opt.isEmpty()) return false;
        Utilisateur u = opt.get();
        if (!u.isActif()) return false;
        if (!PasswordUtil.verifier(password, u.getPassword())) return false;
        SessionUtilisateur.connecter(u);
        return true;
    }

    public void deconnecter() {
        SessionUtilisateur.deconnecter();
    }

    public Utilisateur inscrire(Utilisateur u) {
        u.setPassword(PasswordUtil.hash(u.getPassword()));
        return utilisateurRepository.save(u);
    }
}