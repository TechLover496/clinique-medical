package sn.isi.cliniquemedical.repository;

import sn.isi.cliniquemedical.model.RendezVous;
import sn.isi.cliniquemedical.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RendezVousRepository implements GenericRepository<RendezVous, Long> {

    @Override
    public RendezVous save(RendezVous r) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(r);
        em.getTransaction().commit();
        em.close();
        return r;
    }

    @Override
    public RendezVous update(RendezVous r) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        RendezVous merged = em.merge(r);
        em.getTransaction().commit();
        em.close();
        return merged;
    }

    @Override
    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        RendezVous r = em.find(RendezVous.class, id);
        if (r != null) em.remove(r);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Optional<RendezVous> findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        RendezVous r = em.find(RendezVous.class, id);
        em.close();
        return Optional.ofNullable(r);
    }

    @Override
    public List<RendezVous> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        List<RendezVous> list = em.createQuery("SELECT r FROM RendezVous r", RendezVous.class).getResultList();
        em.close();
        return list;
    }

    // Rendez-vous du jour
    public List<RendezVous> findByDate(LocalDate date) {
        EntityManager em = JPAUtil.getEntityManager();
        LocalDateTime debut = date.atStartOfDay();
        LocalDateTime fin = date.atTime(23, 59, 59);
        List<RendezVous> list = em.createQuery(
                        "SELECT r FROM RendezVous r WHERE r.dateHeure BETWEEN :debut AND :fin", RendezVous.class)
                .setParameter("debut", debut)
                .setParameter("fin", fin)
                .getResultList();
        em.close();
        return list;
    }

    // Vérifier conflit horaire médecin
    public boolean existsConflict(Long medecinId, LocalDateTime dateHeure) {
        EntityManager em = JPAUtil.getEntityManager();
        Long count = em.createQuery(
                        "SELECT COUNT(r) FROM RendezVous r WHERE r.medecin.id = :medecinId AND r.dateHeure = :dateHeure AND r.statut != 'ANNULE'", Long.class)
                .setParameter("medecinId", medecinId)
                .setParameter("dateHeure", dateHeure)
                .getSingleResult();
        em.close();
        return count > 0;
    }

    // Filtrer par médecin
    public List<RendezVous> findByMedecin(Long medecinId) {
        EntityManager em = JPAUtil.getEntityManager();
        List<RendezVous> list = em.createQuery(
                        "SELECT r FROM RendezVous r WHERE r.medecin.id = :medecinId", RendezVous.class)
                .setParameter("medecinId", medecinId)
                .getResultList();
        em.close();
        return list;
    }
}
