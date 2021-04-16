package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import classes.Arret;

public class ArretDAO extends DAO<Arret> {

    public ArretDAO(Connection conn) {
        super(conn);
    }

    /* ---- Création d'un nouvel élément ---- */
    @Override
    public boolean create(Arret obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO arret VALUES ('"+obj.code+"','"+obj.lib_arret+"','"+obj.streetMap+"')");
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
    public Arret read(int id) {
        return null;
    }

    /* ---- Modification d'un élément ---- */
    @Override
    public boolean update(Arret obj) {
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("UPDATE arret SET lib_arret = '"+obj.lib_arret+"', streetMap = '"+obj.streetMap+"' WHERE code= '"+obj.code+"'");  
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Suppression d'un élément ---- */
    @Override
    public boolean delete(Arret obj) {
        int nb = 0;
        try {
        nb = this.connect.createStatement().executeUpdate("delete from arret where code ='"+obj.code+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Affichage de tous les éléments voulus ---- */
    public Arret readWithId_arr(String code) {
        Arret a = new Arret();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM arret WHERE code = '"+code+"'");
        if (rs.next()) {
            a.code = rs.getString("code");
            a.lib_arret   = rs.getString("lib_arret");
            a.streetMap = rs.getString("streetMap");
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }
    
    /* ---- Affichage de la liste de tous les éléments ---- */
    public ArrayList<Arret> readAllArret() {
        ArrayList<Arret> L = new ArrayList<Arret>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM arret");
            while (rs.next()) {
                Arret a = new Arret();
                a.code = rs.getString("code");
                a.lib_arret   = rs.getString("lib_arret");
                a.streetMap = rs.getString("streetMap");
                L.add(a);
            }
            stmt.close();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }
    
    public ArrayList<Arret> readAllArretInDefi() {
        ArrayList<Arret> L = new ArrayList<Arret>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT * FROM arret WHERE code IN (SELECT code_arret FROM defi)");
            while (rs.next()) {
                Arret a = new Arret();
                a.code = rs.getString("code");
                a.lib_arret   = rs.getString("lib_arret");
                a.streetMap = rs.getString("streetMap");
                L.add(a);
            }
            stmt.close();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }
}
