package sn.isi.cliniquemedical.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medecins")
public class Medecin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String specialite;

    @Column(nullable = false, unique = true)
    private String telephone;

    @OneToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL)
    private List<RendezVous> rendezVous;

    @OneToMany(mappedBy = "medecin", cascade = CascadeType.ALL)
    private List<Consultation> consultations;
}