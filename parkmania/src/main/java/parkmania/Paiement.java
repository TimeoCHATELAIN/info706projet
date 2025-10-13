package parkmania;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Paiement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime datePaiement;
    private double montant;
    private String typePaiement; // "CB" ou "especes"

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    // Getters et setters
}
