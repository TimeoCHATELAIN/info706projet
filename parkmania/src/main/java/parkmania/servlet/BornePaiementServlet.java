package parkmania.servlet;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import parkmania.entite.Paiement;
import parkmania.entite.Ticket;
import parkmania.service.PaiementService;
import parkmania.service.TicketService;
import parkmania.util.DateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "BornePaiementServlet", urlPatterns = {"/bornePaiement"})
public class BornePaiementServlet extends HttpServlet {

    @Inject
    private TicketService ticketService;

    @Inject
    private PaiementService paiementService;


    private void getHistorique(HttpServletRequest req, Ticket ticket) {
        double totalPaye = paiementService.totalPaye(ticket);
        double montantTotal = paiementService.calculerMontantAPayer(ticket);
        double montantRestant = Math.max(0, montantTotal - totalPaye);

        List<Paiement> paiements = paiementService.listerPaiementsParTicket(ticket.getId());

        req.setAttribute("ticket", ticket);
        req.setAttribute("totalPaye", totalPaye);
        req.setAttribute("montantRestant", montantRestant);
        req.setAttribute("paiements", paiements);

        List<Map<String, Object>> paiementsFormates = paiements.stream()
                .map(p -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", p.getId());
                    m.put("datePaiement", DateUtils.format(p.getDatePaiement())); // formaté
                    m.put("montant", p.getMontant());
                    m.put("typePaiement", p.getTypePaiement());
                    return m;
                })
                .collect(Collectors.toList());

        req.setAttribute("paiementsFormates", paiementsFormates);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String numeroParam = req.getParameter("numero-paiement");

        if (numeroParam != null) {
            try {
                Long numeroTicket = Long.parseLong(numeroParam);
                Ticket ticket = ticketService.getTicket(numeroTicket);

                if (ticket != null) {
                    getHistorique(req, ticket);
                    req.setAttribute("dateEntree", DateUtils.format(ticket.getDateEntree()));
                } else {
                    req.setAttribute("erreur", "Ticket introuvable !");
                }


            } catch (NumberFormatException e) {
                req.setAttribute("erreur", "Numéro de ticket invalide !");
            }
        }

        req.getRequestDispatcher("bornePaiement.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String numeroParam = req.getParameter("numero-paiement");
        String montantParam = req.getParameter("montant");
        String typeParam = req.getParameter("typePaiement");

        if (numeroParam == null || montantParam == null || typeParam == null) {
            req.setAttribute("erreur", "Champs manquants !");
            doGet(req, resp);
        }

        try {
            Long numeroTicket = Long.parseLong(numeroParam);
            double montant = Double.parseDouble(montantParam);
            Paiement.TypePaiement type = Paiement.TypePaiement.valueOf(typeParam.trim().toUpperCase());

            Ticket ticket = ticketService.getTicket(numeroTicket);
            if (ticket == null) {
                req.setAttribute("erreur", "Ticket introuvable !");
                doGet(req, resp);
                return;
            }

            if (montant > 0){
                Paiement paiement = paiementService.creerPaiement(ticket, montant, type);
                paiementService.enregistrerPaiement(paiement);
                req.setAttribute("message", "Paiement enregistré avec succès !");

                List<Paiement> paiements = paiementService.listerPaiementsParTicket(ticket.getId());

                // après persist du paiement
                List<Map<String,Object>> paiementsFormates = paiements.stream().map(p -> {
                    Map<String,Object> m = new HashMap<>();
                    m.put("dateEntree", DateUtils.format(p.getTicket().getDateEntree()));
                    m.put("datePaiement", DateUtils.format(p.getDatePaiement())); // String formatée
                    m.put("montant", p.getMontant());
                    m.put("typePaiement", p.getTypePaiement().name()); // "CB" ou "ESPECES"
                    return m;
                }).collect(Collectors.toList());

                req.setAttribute("paiementsFormates", paiementsFormates);

                // pour le justificatif
                if (!paiementsFormates.isEmpty()) {
                    Map<String,Object> last = paiementsFormates.get(paiementsFormates.size()-1);
                    req.setAttribute("dernierPaiement", last.get("datePaiement"));
                    req.setAttribute("dernierTypePaiement", last.get("typePaiement"));
                }

            }else{
                req.setAttribute("erreur", "Montant invalide !");
            }

            getHistorique(req, ticket);

        } catch (Exception e) {
            req.setAttribute("erreur", "Erreur : " + e.getMessage());
        }

        req.getRequestDispatcher("bornePaiement.jsp").forward(req, resp);
    }
}