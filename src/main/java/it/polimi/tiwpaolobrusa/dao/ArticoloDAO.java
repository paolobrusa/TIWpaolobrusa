package it.polimi.tiwpaolobrusa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticoloDAO {
    private final Connection connection;

    public ArticoloDAO(Connection connection) {
        this.connection = connection;
    }

    public void addArticolo(int code, String name, String description, String owner, String path, int price) throws SQLException {
        String query = "INSERT into articolo (codice, proprietario, nome, descrizione, immaginepath, prezzo) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, code);
            ps.setString(2, owner);
            ps.setString(3, name);
            ps.setString(4, description);
            ps.setString(5, path);
            ps.setInt(6, price);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new SQLException(e);
        }
        finally {
            try{
                ps.close();
            }
            catch (SQLException e) {
                throw new SQLException(e);
            }
        }
    }
}
