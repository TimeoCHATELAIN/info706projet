package parkmania.service;

import parkmania.entite.Ticket;
import parkmania.entite.Paiement;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class TicketService {
    @PersistenceContext(unitName = "parkingPU")
    private EntityManager em;

    public Ticket createTicket() {
        Ticket ticket = new Ticket();
        em.persist(ticket);
        return ticket;
    }
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

    public Ticket getTicket(Long ticketId) {
        return em.find(Ticket.class, ticketId);
    }
}
