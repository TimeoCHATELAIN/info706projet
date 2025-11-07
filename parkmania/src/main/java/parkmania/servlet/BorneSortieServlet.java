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

@WebServlet(name = "BorneSortieServlet", urlPatterns = {"/sortie"})
public class BorneSortieServlet extends HttpServlet {
    @EJB
    private TicketService ticketService;
}
