package parkmania.entite;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Ticket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateEntree;
    private LocalDateTime dateSortie;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Paiement> paiements;

    // Getters et setters
}
