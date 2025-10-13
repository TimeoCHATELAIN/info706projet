package parkmania.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "BorneEntreeServlet", urlPatterns = {"/entree"})
public class BorneEntreeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Créer un ticket
        // 2. Persister le ticket
        // 3. Afficher le numéro du ticket
    }
}

