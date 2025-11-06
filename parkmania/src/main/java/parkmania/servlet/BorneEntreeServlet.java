package parkmania.servlet;

import jakarta.ejb.EJB;
import parkmania.entite.Ticket;
import parkmania.service.TicketService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@WebServlet(name = "BorneEntreeServlet", urlPatterns = {"/entree"})
public class BorneEntreeServlet extends HttpServlet {
    @EJB
    private TicketService ticketService;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Ticket ticket = ticketService.createTicket();

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h2>Bienvenue !</h2>");
        out.println("Ticket n : " + ticket.getId() + "<br>");
        out.println("Date d'entrée : " + ticket.getDateEntree() + "<br>");
        out.println("</body></html>");
    }
}

