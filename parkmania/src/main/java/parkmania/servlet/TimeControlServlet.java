package parkmania.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import parkmania.service.TimeService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "TimeControlServlet", urlPatterns = {"/time"})
public class TimeControlServlet extends HttpServlet {

    @EJB
    private TimeService timeService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        double m = timeService.getMultiplier();
        out.print("{" + "\"multiplier\": " + m + "}");
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String mult = req.getParameter("multiplier");
        if (mult != null) {
            try {
                double m = Double.parseDouble(mult);
                if (m <= 0) m = 1.0;
                timeService.setMultiplier(m);
            } catch (NumberFormatException ignored) {
            }
        }
        // Return current multiplier
        doGet(req, resp);
    }
}
