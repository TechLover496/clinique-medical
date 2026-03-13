package sn.isi.cliniquemedical.repository;

import sn.isi.cliniquemedical.model.Facture;
import sn.isi.cliniquemedical.model.StatutPaiement;
import sn.isi.cliniquemedical.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class FactureRepository implements GenericRepository<Facture, Long> {

    @Override
    public Facture save(Facture f) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(f);
        em.getTransaction().commit();
        em.close();
        return f;
    }

    @Override
    public Facture update(Facture f) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Facture merged = em.merge(f);
        em.getTransaction().commit();
        em.close();
        return merged;
    }

    @Override
    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Facture f = em.find(Facture.class, id);
        if (f != null) em.remove(f);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Optional<Facture> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        Facture f = em.find(Facture.class, id);
        em.close();
        return Optional.ofNullable(f);
    }

    @Override
    public List<Facture> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Facture> list = em.createQuery("SELECT f FROM Facture f", Facture.class).getResultList();
        em.close();
        return list;
    }

    public List<Facture> findByStatut(StatutPaiement statut) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Facture> list = em.createQuery(
                        "SELECT f FROM Facture f WHERE f.statutPaiement = :statut", Facture.class)
                .setParameter("statut", statut)
                .getResultList();
        em.close();
        return list;
    }
}