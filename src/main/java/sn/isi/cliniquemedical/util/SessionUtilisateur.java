package sn.isi.cliniquemedical.util;

import sn.isi.cliniquemedical.model.Role;
import sn.isi.cliniquemedical.model.Utilisateur;

public class SessionUtilisateur {

    private static Utilisateur utilisateurConnecte;

    private SessionUtilisateur() {}

    public static void connecter(Utilisateur utilisateur) {
        utilisateurConnecte = utilisateur;
    }

    public static Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public static boolean isConnecte() {
        return utilisateurConnecte != null;
    }

    public static void deconnecter() {
        utilisateurConnecte = null;
    }

    public static boolean hasRole(Role role) {
        return isConnecte() && utilisateurConnecte.getRole() == role;
    }
}