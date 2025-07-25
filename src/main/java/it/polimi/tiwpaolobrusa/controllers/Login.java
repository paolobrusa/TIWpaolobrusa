package it.polimi.tiwpaolobrusa.controllers;

import it.polimi.tiwpaolobrusa.beans.Utente;
import it.polimi.tiwpaolobrusa.dao.UtenteDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/Login")
public class Login extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private Connection con = null;
    RequestDispatcher dispatcher = null;

    public Login() {
        super();
    }

    public void init() throws ServletException{
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
        String errorMessage = (String) request.getSession().getAttribute("errorMessage");
        if (errorMessage != null) {
            request.getSession().removeAttribute("errorMessage");
            request.setAttribute("errorMessage", errorMessage);
        }
        String path = "/WEB-INF/login.jsp";
        dispatcher = request.getRequestDispatcher(path);
        dispatcher.forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UtenteDAO uDAO = new UtenteDAO(con);
        Utente user = null;
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null || password == null || username.length() > 50 || password.length() > 50) {
            request.getSession().setAttribute("errorMessage", "Lunghezza deve essere minore di 50 e non null");
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }
        try {
            user = uDAO.getUtente(username, password);
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", e.getCause().getMessage());
            response.sendRedirect(request.getContextPath() + "/Login");
            return;
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user.getUsername());
        response.sendRedirect(request.getContextPath() + "/Homepage");
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
