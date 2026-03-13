package sn.isi.cliniquemedical.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "factures")
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateFacture;

    @Column(nullable = false)
    private Double montantTotal;

    @Column(nullable = false)
    private String modePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPaiement statutPaiement = StatutPaiement.NON_PAYE;

    @OneToOne
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;
}