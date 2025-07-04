package it.polimi.tiwpaolobrusa.dao;

import it.polimi.tiwpaolobrusa.beans.Offerta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OffertaDAO {
    private final Connection connection;

    public OffertaDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Offerta> getOfferta(int idasta) throws SQLException {
        List<Offerta> offerta = new ArrayList<Offerta>();
        String query = "SELECT usnutente, offertaprezzo, dataora FROM Offerta WHERE idasta = ? ORDER BY dataora DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, idasta);
            rs = ps.executeQuery();
            while (rs.next()) {
                Offerta o = new Offerta(rs.getString("usnutente"), rs.getInt("offertaprezzo"), rs.getDate("dataora"));
                offerta.add(o);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Cant't get offerta");
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
        return offerta;
    }
}
