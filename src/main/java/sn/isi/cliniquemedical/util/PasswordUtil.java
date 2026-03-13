package sn.isi.cliniquemedical.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    private PasswordUtil() {}

    public static String hash(String motDePasse) {
        return BCrypt.hashpw(motDePasse, BCrypt.gensalt());
    }

    public static boolean verifier(String motDePasse, String hash) {
        return BCrypt.checkpw(motDePasse, hash);
    }
}