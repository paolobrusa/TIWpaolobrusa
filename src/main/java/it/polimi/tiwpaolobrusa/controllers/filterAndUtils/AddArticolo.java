package it.polimi.tiwpaolobrusa.controllers.filterAndUtils;

import it.polimi.tiwpaolobrusa.dao.ArticoloDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet ("/AddArticolo")
@MultipartConfig
public class AddArticolo extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private Connection con = null;
    private static final String dir = System.getProperty("user.home") + File.separator + "TIWImage";

    public AddArticolo() {
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
        String n = request.getParameter("nome");
        String d = request.getParameter("descrizione");
        String o = request.getSession().getAttribute("user").toString();
        String p = request.getParameter("prezzo");
        if (n == null || d == null || o == null || p == null) {
            request.getSession().setAttribute("errorMessage", "Parametri non validi");
            response.sendRedirect(request.getContextPath() + "/Vendo");
            return;
        }
        int prezzo;
        try {
            prezzo = Integer.parseInt(p);
        }
        catch (NumberFormatException e){
            request.getSession().setAttribute("errorMessage", "Formato non valido");
            response.sendRedirect(request.getContextPath() + "/Vendo");
            return;
        }
        if(prezzo <= 0){
            request.getSession().setAttribute("errorMessage", "Prezzo non puo essere negativo");
            response.sendRedirect(request.getContextPath() + "/Vendo");
            return;
        }
        Part image = request.getPart("immagine");
        if(image == null || (!image.getContentType().equals("image/jpeg") && !image.getContentType().equals("image/png"))){
            request.getSession().setAttribute("errorMessage", "Immagine non valido");
            response.sendRedirect(request.getContextPath() + "/Vendo");
            return;
        }
        String path = UUID.randomUUID() + "." + image.getContentType().replace("image/", "");
        File upload = new File(dir);
        if (!upload.exists()) {
            boolean a = upload.mkdir();
            if (!a) {
                request.getSession().setAttribute("errorMessage", "Errore creazione cartella");
                response.sendRedirect(request.getContextPath() + "/Vendo");
                return;
            }
        }
        String filePath = dir + File.separator + path;
        image.write(filePath);
        ArticoloDAO aDAO = new ArticoloDAO(con);
        try {
            aDAO.addArticolo(n, d, o, path, prezzo);
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", e.getCause().getMessage());
            response.sendRedirect(request.getContextPath() + "/Vendo");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/Vendo");
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
