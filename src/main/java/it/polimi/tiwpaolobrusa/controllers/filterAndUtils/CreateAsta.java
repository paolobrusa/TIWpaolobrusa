package it.polimi.tiwpaolobrusa.controllers.filterAndUtils;

import it.polimi.tiwpaolobrusa.beans.Articolo;
import it.polimi.tiwpaolobrusa.dao.ArticoloDAO;
import it.polimi.tiwpaolobrusa.dao.AstaDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CreateAsta")
public class CreateAsta extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private Connection con = null;

    public CreateAsta() {
        super();
    }

    public void init() throws ServletException {
        ServletContext context = getServletContext();
        String user = context.getInitParameter("user");
        String pwd = context.getInitParameter("pwd");
        String driver = context.getInitParameter("driver");
        String url = context.getInitParameter("urlDb");
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't load driver");
        }
        try {
            con = DriverManager.getConnection(url, user, pwd);
        } catch (SQLException e) {
            throw new RuntimeException("Failed db connection");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/Vendo");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] c = request.getParameterValues("codice");
        String user = request.getSession().getAttribute("user").toString();
        if(c == null || c.length == 0){
            request.getSession().setAttribute("errorMessage", "Devi selezionare almeno 1 articolo");
            response.sendRedirect(request.getContextPath() + "/Vendo");
            return;
        }
        List<Integer> cods = new ArrayList<>();
        int cod = 0;
        for (String s : c) {
            try{
                cod = Integer.parseInt(s);
            }
            catch(NumberFormatException e){
                request.getSession().setAttribute("errorMessage", "Codici devono essere numeri");
                response.sendRedirect(request.getContextPath() + "/Vendo");
                return;
            }
            cods.add(cod);
        }
        ArticoloDAO aDao = new ArticoloDAO(con);
        List<Articolo> articoli;
        try {
            articoli = aDao.getArticoli(cods);
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/Vendo");
            return;
        }
        AstaDAO aDao2 = new AstaDAO(con);
        int idAsta = 0;
        String mb = request.getParameter("minBid");
        if (mb == null || mb.length() > 11) {
            request.getSession().setAttribute("errorMessage", "Minbid non valido, prezzo piu piccolo richiesto");
            response.sendRedirect(request.getContextPath() + "/Vendo");
            return;
        }
        int minBid = 0;
        try {
            minBid = Integer.parseInt(request.getParameter("minBid"));
        }
        catch(NumberFormatException e){
            request.getSession().setAttribute("errorMessage", "Minbid devono essere numeri");
            response.sendRedirect(request.getContextPath() + "/Vendo");
            return;
        }
        try {
            idAsta = aDao2.addAsta(articoli.stream().mapToInt(Articolo::getPrice).sum(), minBid, LocalDateTime.parse(request.getParameter("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/Vendo");
            return;
        }
        try {
            aDao2.addArticoliAsta(idAsta, cods, user);
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/Vendo");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/Vendo");
    }
}
