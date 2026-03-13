package sn.isi.cliniquemedical.repository;

import sn.isi.cliniquemedical.model.Consultation;
import sn.isi.cliniquemedical.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class ConsultationRepository implements GenericRepository<Consultation, Long> {

    @Override
    public Consultation save(Consultation c) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
        em.close();
        return c;
    }

    @Override
    public Consultation update(Consultation c) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Consultation merged = em.merge(c);
        em.getTransaction().commit();
        em.close();
        return merged;
    }

    @Override
    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Consultation c = em.find(Consultation.class, id);
        if (c != null) em.remove(c);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Optional<Consultation> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        Consultation c = em.find(Consultation.class, id);
        em.close();
        return Optional.ofNullable(c);
    }

    @Override
    public List<Consultation> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Consultation> list = em.createQuery("SELECT c FROM Consultation c", Consultation.class).getResultList();
        em.close();
        return list;
    }

    public List<Consultation> findByPatient(Long patientId) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Consultation> list = em.createQuery(
                        "SELECT c FROM Consultation c WHERE c.patient.id = :patientId", Consultation.class)
                .setParameter("patientId", patientId)
                .getResultList();
        em.close();
        return list;
    }
}