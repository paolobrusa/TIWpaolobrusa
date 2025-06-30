package it.polimi.tiwpaolobrusa.dao;

import it.polimi.tiwpaolobrusa.beans.Asta;
import it.polimi.tiwpaolobrusa.beans.State;

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

    public List<Asta> getAste(String username) throws SQLException {
        List<Asta> asta = new ArrayList<Asta>();
        String query = "SELECT DISTINCT id, prezzoiniziale, rialzomin, scadenza, stato FROM Asta JOIN Articolilista ON Asta.id = Articolilista.idasta JOIN Articolo ON Articolilista.codarticolo = Articolo.codice WHERE Articolo.proprietario = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = connection.prepareStatement(query);
            ps.setString(1, username);
            rs = ps.executeQuery();
            while (rs.next()) {//MANCA TUTTO IL CODICE
                Asta a = new Asta(rs.getInt("id"), rs.getInt("prezzoiniziale"), rs.getInt("rialzomin"),
                        rs.getDate("scadenza"), State.valueOf(rs.getString("stato")));
                asta.add(a);
            }
        }
        catch (SQLException e) {
            throw new SQLException("Can't get Asta");
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

    public void addAsta(List<Integer> cods) throws SQLException {

    }
}
