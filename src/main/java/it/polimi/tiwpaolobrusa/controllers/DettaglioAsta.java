package it.polimi.tiwpaolobrusa.controllers;

import it.polimi.tiwpaolobrusa.beans.Asta;
import it.polimi.tiwpaolobrusa.beans.Offerta;
import it.polimi.tiwpaolobrusa.beans.State;
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
        }
        AstaDAO aDao = new AstaDAO(con);
        Asta asta = null;
        try {
            asta = aDao.getState(idasta);
        } catch (SQLException e) {
            e.printStackTrace();
            //response.sendRedirect(request.getContextPath() + "/Vendo");
        }
        OffertaDAO oDao = new OffertaDAO(con);
        List<Offerta> o = new ArrayList<Offerta>();
        try{
            o = oDao.getOfferta(idasta);
        } catch (SQLException e) {
            e.printStackTrace();
            //response.sendRedirect(request.getContextPath() + "/Vendo");
        }
        if(asta != null && asta.getState() == State.attiva){
            String path = "WEB-INF/dettaglioAttiva.jsp";
            request.setAttribute("asta", asta);
            request.setAttribute("offerte", o);
            dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);
        }
        else if(asta != null && asta.getState() == State.chiusa){
            String path = "WEB-INF/dettaglioChiusa.jsp";
            request.setAttribute("asta", asta);
            request.setAttribute("offerte", o); //TODO SBAGLIATO VA CORRETTO SECONDO LA SPECIFICA
            dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);
        }
    }
}
