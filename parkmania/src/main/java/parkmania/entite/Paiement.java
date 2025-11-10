package parkmania.entite;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Entity
public class Paiement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime datePaiement;
    private double montant;

    public enum TypePaiement {CB, ESPECES};
    private TypePaiement paiement;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public Paiement() {
    }

    public Paiement(double montant, TypePaiement typePaiement, Ticket ticket) {
        this.datePaiement = LocalDateTime.now();
        this.montant = montant;
        this.paiement = typePaiement;
        this.ticket = ticket;
    }

    // Getters et setters
    public Long getId() { return id; }

    public LocalDateTime getDatePaiement() { return datePaiement; }
    public void setDatePaiement(LocalDateTime datePaiement) { this.datePaiement = datePaiement; }

    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public TypePaiement getTypePaiement() { return paiement; }
    public void setTypePaiement(TypePaiement typePaiement) { this.paiement = typePaiement; }

    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }

}
