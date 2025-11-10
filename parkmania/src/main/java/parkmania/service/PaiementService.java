package parkmania.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import parkmania.entite.Paiement;
import parkmania.entite.Ticket;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service responsable de la gestion des paiements liés aux tickets de parking.
 * Ce service gère :
 *  - l’ajout, la mise à jour et la suppression de paiements ;
 *  - le calcul du montant dû et du montant total déjà payé ;
 *  - la création de justificatifs de paiement pour la borne de paiement.
 */
@Stateless
public class PaiementService {

    @PersistenceContext(unitName = "parkingPU")
    private EntityManager em;

    /** Tarif en euros par minute (2 centimes d'euro la minute). */
    private static final double TARIF_PAR_MINUTE = 0.02;

    // -------------------------------------------------------------
    //                      MÉTHODES MÉTIER
    // -------------------------------------------------------------

    /**
     * Calcule le montant à payer entre la date d’entrée et la date actuelle.
     * @param dateEntree date d’entrée du véhicule
     * @param datePaiement date du paiement (souvent LocalDateTime.now())
     * @return montant total à payer en euros
     */
    public double calculerMontantAPayer(LocalDateTime dateEntree, LocalDateTime datePaiement) {
        long minutes = Duration.between(dateEntree, datePaiement).toMinutes();
        return minutes * TARIF_PAR_MINUTE;
    }

    /**
     * Retourne la somme des paiements déjà effectués pour un ticket donné.
     * @param ticket Ticket concerné
     * @return montant total déjà payé
     */
    public double totalPaye(Ticket ticket) {
        List<Paiement> paiements = em.createQuery(
                        "SELECT p FROM Paiement p WHERE p.ticket.id = :ticketId", Paiement.class)
                .setParameter("ticketId", ticket.getId())
                .getResultList();

        return paiements.stream()
                .mapToDouble(Paiement::getMontant)
                .sum();
    }

    /**
     * Crée un nouveau paiement pour un ticket (sans le persister).
     * @param ticket ticket associé
     * @param montant montant payé
     * @param typePaiement type de paiement (CB ou ESPECES)
     * @return l’objet Paiement prêt à être sauvegardé
     */
    public Paiement creerPaiement(Ticket ticket, double montant, Paiement.TypePaiement typePaiement) {
        Paiement paiement = new Paiement();
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setMontant(montant);
        paiement.setTypePaiement(typePaiement);
        paiement.setTicket(ticket);
        return paiement;
    }

    /**
     * Sauvegarde un paiement en base de données.
     * @param paiement Paiement à enregistrer
     */
    public void enregistrerPaiement(Paiement paiement) {
        // S'assurer que le ticket est bien attaché au contexte JPA
        Ticket ticket = em.find(Ticket.class, paiement.getTicket().getId());
        paiement.setTicket(ticket);
        em.persist(paiement);
    }

    /**
     * Retourne la liste de tous les paiements effectués pour un ticket donné.
     * @param ticketId identifiant du ticket
     * @return liste des paiements
     */
    public List<Paiement> listerPaiementsParTicket(Long ticketId) {
        return em.createQuery(
                        "SELECT p FROM Paiement p WHERE p.ticket.id = :ticketId ORDER BY p.datePaiement ASC",
                        Paiement.class)
                .setParameter("ticketId", ticketId)
                .getResultList();
    }

    // -------------------------------------------------------------
    //                   GÉNÉRATION DU JUSTIFICATIF
    // -------------------------------------------------------------

    /**
     * Génère un justificatif de paiement pour un ticket donné.
     * @param ticket ticket concerné
     * @return objet Justificatif à afficher ou imprimer
     */
    public Justificatif genererJustificatif(Ticket ticket) {
        List<Paiement> paiements = listerPaiementsParTicket(ticket.getId());

        double totalPaye = paiements.stream()
                .mapToDouble(Paiement::getMontant)
                .sum();

        LocalDateTime dernierPaiement = paiements.stream()
                .map(Paiement::getDatePaiement)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        return new Justificatif(
                ticket.getId(),
                ticket.getDateEntree(),
                dernierPaiement,
                totalPaye
        );
    }

    // -------------------------------------------------------------
    //                     CLASSE JUSTIFICATIF
    // -------------------------------------------------------------

    /**
     * Représente un justificatif de paiement à afficher sur la borne.
     */
    public static class Justificatif {
        private final Long numeroTicket;
        private final LocalDateTime dateEntree;
        private final LocalDateTime dateDernierPaiement;
        private final double montantTotal;

        public Justificatif(Long numeroTicket, LocalDateTime dateEntree, LocalDateTime dateDernierPaiement, double montantTotal) {
            this.numeroTicket = numeroTicket;
            this.dateEntree = dateEntree;
            this.dateDernierPaiement = dateDernierPaiement;
            this.montantTotal = montantTotal;
        }

        public Long getNumeroTicket() { return numeroTicket; }
        public LocalDateTime getDateEntree() { return dateEntree; }
        public LocalDateTime getDateDernierPaiement() { return dateDernierPaiement; }
        public double getMontantTotal() { return montantTotal; }
    }
}
