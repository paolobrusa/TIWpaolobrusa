package it.polimi.tiwpaolobrusa.controllers;

import it.polimi.tiwpaolobrusa.beans.Articolo;
import it.polimi.tiwpaolobrusa.beans.Asta;
import it.polimi.tiwpaolobrusa.dao.ArticoloDAO;
import it.polimi.tiwpaolobrusa.dao.AstaDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.dynalink.linker.LinkerServices;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/Vendo")
public class Vendo extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private Connection con = null;
    RequestDispatcher dispatcher = null;

    public Vendo() {
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
            throw new RuntimeException("Can't load driver");     //metti qualcosa qui per disconnessione sessione
        }
        try {
            con = DriverManager.getConnection(url, user, pwd);
        } catch (SQLException e) {
            throw new RuntimeException("Failed db connection");
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AstaDAO aDAO = new AstaDAO(con);
        ArticoloDAO artDAO = new ArticoloDAO(con);
        List<Asta> aste;
        List<Articolo> articoli;
        try{
            aste = aDAO.getAste(request.getSession().getAttribute("user").toString());
            articoli = artDAO.getArticoli(request.getSession().getAttribute("user").toString());
            timeLeft(aste);
            String path = "/WEB-INF/vendo.jsp";
            request.setAttribute("aste", aste);
            request.setAttribute("articoli", articoli);
            dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);
        }
        catch (Exception e){
            e.printStackTrace(); //QUA MAGARI MANDA ALLA HOMEPAGE CON ERRORE
        }
    }

    public void timeLeft(List<Asta> aste) {
        LocalDateTime now = LocalDateTime.now();
        for (Asta asta : aste) {
            java.util.Date utilDate = new java.util.Date(asta.getDate().getTime());
            Duration duration = Duration.between(now, utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            String d;
            if(duration.isNegative()){
                d = "FINITO";
            }
            else {
                d = duration.toDays() + ":" + duration.toHoursPart() + ":" + duration.toMinutesPart() + ":" + duration.toSecondsPart();
            }
            asta.setTimeLeft(d);
        }
    }
    //TODO L’elenco riporta: codice e nome degli articoli compresi nell’asta (FALLO NEL DETTAGLIO)

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
