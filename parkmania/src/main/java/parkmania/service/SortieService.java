package parkmania.service;

import jakarta.inject.Inject;
import parkmania.entite.Ticket;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ejb.EJB;

import java.time.LocalDateTime;

@Stateless
public class SortieService {
    @PersistenceContext(unitName = "parkingPU")
    private EntityManager em;
    @Inject
    private TicketService ticketService;

    @Inject
    private PaiementService paiementService;

    @EJB
    private TimeService timeService;

    // Petite marge pour comparer des doubles (évite les égalités strictes)
    private static final double EPSILON = 1e-6;


    /**
     * Tente de valider la sortie pour le ticket fourni.
     * Si le montant restant <= 0, on enregistre la date de sortie et on retourne true.
     * Retourne false sinon.
     *
     * Remarque : on recharge le ticket via em.find pour s'assurer qu'il est managé
     * avant de modifier la date de sortie.
     */
    public boolean validerSortie(Ticket ticket) {
        if (ticket == null || ticket.getId() == null) {
            return false;
        }

        // ✅ Recharge le ticket depuis la base pour avoir les paiements les plus récents
        Ticket managedTicket = em.find(Ticket.class, ticket.getId());
        if (managedTicket == null) {
            return false;
        }

        // ✅ Vérifie le montant restant avec les paiements les plus récents
        double montantRestant = paiementService.calculerMontantAPayer(managedTicket);

        if (montantRestant <= 0.000001) {
            // Tout est payé → on enregistre la date de sortie
            managedTicket.setDateSortie(LocalDateTime.now());
            return true;
        }

        return false;
    }
}
