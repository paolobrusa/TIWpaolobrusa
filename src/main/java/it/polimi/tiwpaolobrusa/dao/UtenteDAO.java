package it.polimi.tiwpaolobrusa.dao;

import it.polimi.tiwpaolobrusa.beans.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteDAO {
    private final Connection connection;

    public UtenteDAO(Connection connection) {
        this.connection = connection;
    }

    public Utente getUtente(String username, String pwd) throws SQLException {
        Utente u = null;
        String query = "SELECT * FROM Utente WHERE username = ? AND pwd = ?";
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, pwd);
            rs = ps.executeQuery();
            if (rs.next())
                u = new Utente(rs.getString("username"), rs.getString("pwd"), rs.getString("nome"),rs.getString("cognome"),rs.getString("indirizzo"));
        }catch (SQLException e){
            throw new SQLException(e);
        }
        finally {
            try{
                if(rs != null) rs.close();
            }catch (SQLException e){
                throw new SQLException("Error closing resultSet");
            }
            try{
                if(ps != null) ps.close();
            }catch (SQLException e){
                throw new SQLException("Error closing statement");
            }
        }
        return u;
    }
}
