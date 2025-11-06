package parkmania.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import parkmania.entite.Ticket;
import parkmania.entite.Paiement;

@Stateless
public class TicketService {
    @PersistenceContext
    private EntityManager em;

    public void createTicket(Ticket ticket) {em.persist(ticket);}
    public void deleteTicket(Ticket ticket) {em.remove(em.find(Ticket.class, ticket.getId()));}
    public void updateTicket(Ticket ticket) {em.merge(ticket);}

    public void ajouterPaiementAuTicket(Long ticketId, double montant, Paiement.TypePaiement typePaiement) {
        Ticket ticket = em.find(Ticket.class, ticketId);
        if (ticket != null) {
            Paiement paiement = new Paiement(montant, typePaiement, ticket);
            ticket.addPaiement(paiement);
            em.persist(paiement);
        }
    }
}
