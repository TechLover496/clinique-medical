package sn.isi.cliniquemedical.repository;

import sn.isi.cliniquemedical.model.Patient;
import sn.isi.cliniquemedical.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class PatientRepository implements GenericRepository<Patient, Long> {

    @Override
    public Patient save(Patient p) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        em.close();
        return p;
    }

    @Override
    public Patient update(Patient p) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Patient merged = em.merge(p);
        em.getTransaction().commit();
        em.close();
        return merged;
    }

    @Override
    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        Patient p = em.find(Patient.class, id);
        if (p != null) em.remove(p);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Optional<Patient> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        Patient p = em.find(Patient.class, id);
        em.close();
        return Optional.ofNullable(p);
    }

    @Override
    public List<Patient> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Patient> list = em.createQuery("SELECT p FROM Patient p", Patient.class).getResultList();
        em.close();
        return list;
    }

    public List<Patient> rechercher(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Patient> list = em.createQuery(
                        "SELECT p FROM Patient p WHERE LOWER(p.nom) LIKE :kw OR LOWER(p.prenom) LIKE :kw OR p.telephone LIKE :kw", Patient.class)
                .setParameter("kw", "%" + keyword.toLowerCase() + "%")
                .getResultList();
        em.close();
        return list;
    }
}