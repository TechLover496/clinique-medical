package sn.isi.cliniquemedical.repository;

import sn.isi.cliniquemedical.model.Utilisateur;
import sn.isi.cliniquemedical.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class UtilisateurRepository implements GenericRepository<Utilisateur, Long> {

    @Override
    public Utilisateur save(Utilisateur u) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(u);
        em.getTransaction().commit();
        em.close();
        return u;
    }

    @Override
    public Utilisateur update(Utilisateur u) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Utilisateur merged = em.merge(u);
        em.getTransaction().commit();
        em.close();
        return merged;
    }

    @Override
    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Utilisateur u = em.find(Utilisateur.class, id);
        if (u != null) em.remove(u);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Optional<Utilisateur> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        Utilisateur u = em.find(Utilisateur.class, id);
        em.close();
        return Optional.ofNullable(u);
    }

    @Override
    public List<Utilisateur> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Utilisateur> list = em.createQuery("SELECT u FROM Utilisateur u", Utilisateur.class).getResultList();
        em.close();
        return list;
    }

    public Optional<Utilisateur> findByUsername(String username) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Utilisateur u = em.createQuery("SELECT u FROM Utilisateur u WHERE u.username = :username", Utilisateur.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(u);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}