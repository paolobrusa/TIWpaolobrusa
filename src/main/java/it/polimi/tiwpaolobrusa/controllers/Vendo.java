package it.polimi.tiwpaolobrusa.controllers;

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
import java.util.ArrayList;
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
        List<Asta> aste;
        try{
            aste = aDAO.getAste(request.getSession().getAttribute("user").toString());
            String path = "/WEB-INF/vendo.jsp";
            request.setAttribute("aste", aste);
            dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("action").equals("addArticolo")){
            String c = request.getParameter("codice");
            String n = request.getParameter("nome");
            String d = request.getParameter("descrizione");
            String o = request.getParameter("proprietario");
            String path = request.getParameter("path");
            String p = request.getParameter("prezzo");
            if (c == null || n == null || d == null || o == null || path == null || p == null) {
                request.setAttribute("errorMessage", "Parametri non validi");
                dispatcher.forward(request, response);
                return;
            }
            ArticoloDAO aDAO = new ArticoloDAO(con);
            try {
                aDAO.addArticolo(Integer.parseInt(c), n, d, o, path, Integer.parseInt(p));
            } catch (SQLException e) {
                request.setAttribute("errorMessage", e.getCause().getMessage());
                dispatcher.forward(request, response);
                return;
            }
        }
        if(request.getParameter("action").equals("createAsta")){
            String[] c = request.getParameterValues("codice");
            if(c == null || c.length == 0){
                request.setAttribute("errorMessage", "Devi selezionare almeno 1 articolo");
                dispatcher.forward(request, response);
                return;
            }
            List<Integer> cods = new ArrayList<>();
            for (String s : c) {
                try {
                    cods.add(Integer.parseInt(s));
                } catch (Exception e) {
                    request.setAttribute("errorMessage", e.getCause().getMessage());
                    dispatcher.forward(request, response);
                    return;
                }
            }
            AstaDAO aDAO = new AstaDAO(con);
        }
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
