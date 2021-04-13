package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import classes.Chercher;

public class ChercherDAO extends DAO<Chercher> {

    public ChercherDAO(Connection conn) {
        super(conn);
    }

    @Override
    public boolean create(Chercher obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO chercher VALUES ('"+obj.id_defi+"','"+obj.id_mc+"')");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Chercher read(int id) {
        return null;
    }


    @Override
    public boolean delete(Chercher obj) {
        int nb = 0;
        try {
        nb = this.connect.createStatement().executeUpdate("DELETE FROM chercher WHERE id_defi ='"+obj.id_defi+"' AND id_mc = '"+obj.id_mc+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean update(Chercher obj) {
            return false;
        
    }

    public Chercher readWithId_defi(String id) {
        Chercher ch = new Chercher();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM chercher WHERE id_defi = '"+id+"'");
        if (rs.next()) {
            ch.id_defi = rs.getString("id_defi");
            ch.id_mc = rs.getString("id_mc");
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ch;
    }

    public Chercher readWithId_mc(String id) {
        Chercher ch = new Chercher();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM chercher WHERE id_mc = '"+id+"'");
        if (rs.next()) {
            ch.id_defi = rs.getString("id_defi");
            ch.id_mc = rs.getString("id_mc");
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ch;
    }

    public Chercher readWithTwoId(String id1,String id2) {
        Chercher ch = new Chercher();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM chercher WHERE id_defi = '"+id1+"' AND id_mc = '"+id2+"'");
        if (rs.next()) {
            ch.id_defi = rs.getString("id_defi");
            ch.id_mc = rs.getString("id_mc");
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ch;
    }

    
    public ArrayList<Chercher> readAllChercher() {
        ArrayList<Chercher> L = new ArrayList<Chercher>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM chercher");
            while (rs.next()) {
                Chercher ch = new Chercher();
                ch.id_defi = rs.getString("id_defi");
                ch.id_mc = rs.getString("id_mc");
                L.add(ch);
            }
            stmt.close();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }
}
