package it.polimi.tiwpaolobrusa.controllers;

import it.polimi.tiwpaolobrusa.beans.Asta;
import it.polimi.tiwpaolobrusa.beans.Offerta;
import it.polimi.tiwpaolobrusa.controllers.filterAndUtils.TimeLeft;
import it.polimi.tiwpaolobrusa.dao.AstaDAO;
import it.polimi.tiwpaolobrusa.dao.OffertaDAO;
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
import java.util.List;

@WebServlet("/Acquisto")
public class Acquisto extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private Connection con;
    RequestDispatcher dispatcher = null;

    public Acquisto() {
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
        OffertaDAO oDao = new OffertaDAO(con);
        List<Offerta> aggiud;
        try {
            aggiud = oDao.getOfferteAggiudicate(request.getSession().getAttribute("user").toString());
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Errore caricamento aste");
            String path = "/WEB-INF/acquisto.jsp";
            dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);
            return;
        }
        String errorMessage = (String) request.getSession().getAttribute("errorMessage");
        if (errorMessage != null) {
            request.getSession().removeAttribute("errorMessage");
            request.setAttribute("errorMessage", errorMessage);
        }
        List<Asta> aste;
        AstaDAO aDao = new AstaDAO(con);
        String keyWord = request.getParameter("search");
        if (keyWord != null) {
            try {
                aste = aDao.getAstaByKeyword(keyWord, request.getSession().getAttribute("user").toString());
                TimeLeft.timeLeft(aste);
            } catch (SQLException e) {
                request.getSession().setAttribute("errorMessage", e.getMessage());
                response.sendRedirect(request.getContextPath() + "/Acquisto");
                return;
            }
            if (aste.isEmpty()) {
                request.setAttribute("errorMessage", "Non trovato");
            }
            request.getSession().setAttribute("aste", aste);
        }
        request.setAttribute("aggiud", aggiud);
        String path = "/WEB-INF/acquisto.jsp";
        dispatcher = request.getRequestDispatcher(path);
        dispatcher.forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyWord = request.getParameter("keyWord");
        if(keyWord == null){
            request.getSession().setAttribute("errorMessage", "Parametro non puo essere null");
            response.sendRedirect(request.getContextPath() + "/Acquisto");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/Acquisto?search=" + keyWord);
    }
}
