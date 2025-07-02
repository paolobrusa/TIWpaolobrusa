package it.polimi.tiwpaolobrusa.dao;

import it.polimi.tiwpaolobrusa.beans.Asta;
import it.polimi.tiwpaolobrusa.beans.State;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AstaDAO {
    private final Connection connection;

    public AstaDAO(Connection connection) {
        this.connection = connection;
    }

    public Asta getState(int id) throws SQLException {
        String query = "SELECT * FROM Asta WHERE id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Asta a = null;
        try{
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if(rs.next()){
                a = new Asta(rs.getInt("id"), rs.getInt("prezzoiniziale"), rs.getInt("rialzominimo"), rs.getDate("scadenza"), State.valueOf(rs.getString("stato")));
            }
            else{
                throw new SQLException("Asta not found");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new SQLException("Asta not exist");
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
        return a;
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
            while (rs.next()) {
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

    public int addAsta(int initialPrice, int minBid, LocalDateTime date) throws SQLException {
        String query = "INSERT into asta (prezzoiniziale, rialzomin, scadenza) values (?, ?, ?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idAsta = 0;
        try {
            ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, initialPrice);
            ps.setInt(2, minBid);
            ps.setObject(3, Timestamp.valueOf(date));
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                throw new RuntimeException();
                //throw new SQLException("Can't add Asta");
            }
            idAsta = rs.getInt(1);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Can't add Asta");
        }
        finally {
            try {
                if(ps != null) ps.close();
            } catch (SQLException e) {
                throw new SQLException("Error closing statement");
            }
            try{
                if(rs != null) rs.close();
            }
            catch (SQLException e){
                throw new SQLException("Error closing rs");
            }
        }
        return idAsta;
    }

    public void addArticoliAsta(int idAsta, List<Integer> cods) throws SQLException {
        String query = "INSERT into articolilista (idasta, codarticolo) values (?, ?)";
        PreparedStatement ps = null;
        try{
            ps = connection.prepareStatement(query);
            for (Integer codArt : cods) {
                ps.setInt(1, idAsta);
                ps.setInt(2, codArt);
                ps.addBatch();
            }
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new SQLException("Can't add Articoli"); //QUI MANCA RIMOZIONE ULTIMA ASTA DATO CHE SE FALLISCE L'ASTA NON DEVE ESISTERE
        }
        finally {
            try{
                ps.close();
            }
            catch (SQLException e){
                throw new SQLException("Error closing statement");
            }
        }
    }
}
