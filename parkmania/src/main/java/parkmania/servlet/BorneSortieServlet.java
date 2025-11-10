package parkmania.servlet;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import parkmania.entite.Ticket;
import parkmania.service.PaiementService;
import parkmania.service.SortieService;
import parkmania.service.TicketService;

import java.io.IOException;

@WebServlet(name = "BorneSortieServlet", urlPatterns = {"/sortie"})
public class BorneSortieServlet extends HttpServlet {
    @Inject
    private TicketService ticketService;

    @Inject
    private PaiementService paiementService;

    @Inject
    private SortieService sortieService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String numeroTicket = req.getParameter("numero-sortie");

        if (numeroTicket != null) {
            Long idTicket = Long.parseLong(numeroTicket);
            Ticket ticket = ticketService.getTicket(idTicket);
            if (ticket != null) {
                req.setAttribute("peutSortir", sortieService.validerSortie(ticket));
            }else{
                req.setAttribute("erreur", "Ticket introuvable !");
            }
        }else {
            req.setAttribute("erreur", "Numéro de ticket vide !");
        }

        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}