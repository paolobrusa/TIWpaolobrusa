package it.polimi.tiwpaolobrusa.dao;

import it.polimi.tiwpaolobrusa.beans.Asta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AstaDAO {
    private final Connection connection;

    public AstaDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Asta> getAsteAperte(String username) throws SQLException {
        List<Asta> asta = new ArrayList<Asta>();
        String query = "SELECT * FROM Asta JOIN Articolilista ON Asta.id = Articolilista.idasta JOIN Articolo ON Articolilista.codarticolo = Articolo.codice WHERE Articolo.proprietario = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = connection.prepareStatement(query);
            ps.setString(1, username);
            rs = ps.executeQuery();
            while (rs.next()) {
                                                                            //MANCA TUTTO IL CODICE
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
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
        return asta;
    }
}
