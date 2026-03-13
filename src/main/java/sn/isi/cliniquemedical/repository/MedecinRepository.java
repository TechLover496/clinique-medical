package sn.isi.cliniquemedical.repository;

import sn.isi.cliniquemedical.model.Medecin;
import sn.isi.cliniquemedical.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class MedecinRepository implements GenericRepository<Medecin, Long> {

    @Override
    public Medecin save(Medecin m) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(m);
        em.getTransaction().commit();
        em.close();
        return m;
    }

    @Override
    public Medecin update(Medecin m) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Medecin merged = em.merge(m);
        em.getTransaction().commit();
        em.close();
        return merged;
    }

    @Override
    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Medecin m = em.find(Medecin.class, id);
        if (m != null) em.remove(m);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Optional<Medecin> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        Medecin m = em.find(Medecin.class, id);
        em.close();
        return Optional.ofNullable(m);
    }

    @Override
    public List<Medecin> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Medecin> list = em.createQuery("SELECT m FROM Medecin m", Medecin.class).getResultList();
        em.close();
        return list;
    }
}