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
import java.time.format.DateTimeFormatter;

@WebServlet(name = "BorneEntreeServlet", urlPatterns = {"/entree"})
public class BorneEntreeServlet extends HttpServlet {
    @EJB
    private TicketService ticketService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Ticket ticket = ticketService.createTicket();
        // If the caller asked for JSON (AJAX fetch) respond with JSON to allow in-page popup
        String ajax = request.getParameter("ajax");
        String accept = request.getHeader("Accept");
        boolean wantsJson = (ajax != null && ajax.equalsIgnoreCase("true")) || (accept != null && accept.contains("application/json"));

        if (wantsJson) {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            String dateStr = ticket.getDateEntree().format(formatter);
            // simple JSON without external libs
            String json = "{" +
                    "\"id\":" + ticket.getId() + "," +
                    "\"dateEntree\":\"" + dateStr + "\"" +
                    "}";
            out.print(json);
            out.flush();
        } else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h2>Bienvenue !</h2>");
            out.println("Ticket n : " + ticket.getId() + "<br>");
            out.println("Date d'entrée : " + ticket.getDateEntree().format(formatter) + "<br>");
            out.println("</body></html>");
        }
    }
}

