package it.polimi.tiwpaolobrusa.controllers;

import it.polimi.tiwpaolobrusa.beans.Asta;
import it.polimi.tiwpaolobrusa.beans.Offerta;
import it.polimi.tiwpaolobrusa.beans.State;
import it.polimi.tiwpaolobrusa.beans.Utente;
import it.polimi.tiwpaolobrusa.dao.AstaDAO;
import it.polimi.tiwpaolobrusa.dao.OffertaDAO;
import it.polimi.tiwpaolobrusa.dao.UtenteDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@WebServlet("/Dettaglio")
public class DettaglioAsta extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private Connection con = null;
    RequestDispatcher dispatcher = null;

    public DettaglioAsta() {
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
        String id = request.getParameter("idasta");
        if (id == null) return;
        int idasta = 0;
        try{
            idasta = Integer.parseInt(id);
        }
        catch(NumberFormatException e){
            response.sendRedirect(request.getContextPath() + "/Vendo");
            return;
        }
        AstaDAO aDao = new AstaDAO(con);
        Asta asta = null;
        try {
            asta = aDao.getState(idasta, request.getSession().getAttribute("user").toString());
        } catch (SQLException e) {
            request.setAttribute("errorMessage", e.getMessage());
            String path = "WEB-INF/dettaglioAsta.jsp";
            dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);
            return;
        }
        OffertaDAO oDao = new OffertaDAO(con);
        List<Offerta> o;
        try{
            o = oDao.getOfferta(idasta);
        } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/Vendo");
            return;
        }
        if(asta != null && asta.getState() == State.attiva){
            String path = "WEB-INF/dettaglioAsta.jsp";
            request.setAttribute("asta", asta);
            request.setAttribute("offerte", o);
            dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);
        }
        else if(asta != null && asta.getState() == State.chiusa){
            UtenteDAO uDao = new UtenteDAO(con);
            Offerta winner = null;
            winner = o.stream().max(Comparator.comparing(Offerta::getBid)).orElse(null);
            if(winner == null) {
                request.setAttribute("errorMessage", "Asta chiusa senza aggiudicatario...");
                String path = "WEB-INF/dettaglioAsta.jsp";
                dispatcher = request.getRequestDispatcher(path);
                dispatcher.forward(request, response);
                return;
            }
            request.setAttribute("asta", asta);
            request.setAttribute("offerte", o);
            Utente u = null;
            try {
                u = uDao.getWinner(winner.getUsnUser());
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Non c'è l'aggiudicatario");
                String path = "WEB-INF/dettaglioAsta.jsp";
                dispatcher = request.getRequestDispatcher(path);
                dispatcher.forward(request, response);
                return;
            }
            request.setAttribute("utente", u);
            request.setAttribute("offertaVincente", winner);
            String path = "WEB-INF/dettaglioAsta.jsp";
            dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idAsta = request.getParameter("idAsta");
        int idasta = 0;
        if (idAsta == null) return;
        try {
            idasta = Integer.parseInt(idAsta);
        }
        catch(NumberFormatException e){
            request.setAttribute("errorMessage", "Formato id non valido");
            String path = "WEB-INF/dettaglioAsta.jsp";
            dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);
            return;
        }
        AstaDAO astaDAO = new AstaDAO(con);
        try {
            astaDAO.closeState(idasta);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", e.getMessage());
            String path = "WEB-INF/dettaglioAsta.jsp";
            dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/Dettaglio?idasta=" + idasta);
    }

    public void destroy() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
