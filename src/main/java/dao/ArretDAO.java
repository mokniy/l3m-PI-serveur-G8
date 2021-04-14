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
            nb = this.connect.createStatement().executeUpdate("INSERT INTO arret VALUES ('"+obj.id_arr+"','"+obj.nom_arr+"','"+obj.adresse_arr+"', '"+obj.gps_arr+"')");
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
            nb = this.connect.createStatement().executeUpdate("UPDATE arret SET id_arr = '"+obj.id_arr+"', nom_arr = '"+obj.nom_arr+"', adresse_arr = '"+obj.adresse_arr+"', gps_arr = '"+obj.gps_arr+"'  where id_arr = '"+obj.id_arr+"'");   
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
        nb = this.connect.createStatement().executeUpdate("delete from arret where id_arr ='"+obj.id_arr+"'");
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
    public Arret readWithId_arr(String id_arr) {
        Arret a = new Arret();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM arret WHERE id_arr = '"+id_arr+"'");
        if (rs.next()) {
            a.id_arr = rs.getString("id_arr");
            a.nom_arr   = rs.getString("nom_arr");
            a.adresse_arr = rs.getString("adresse_arr");
            a.gps_arr   = rs.getString("gps_arr");
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
               a.id_arr = rs.getString("id_arr");
               a.nom_arr   = rs.getString("nom_arr");
               a.adresse_arr = rs.getString("adresse_arr");
               a.gps_arr   = rs.getString("gps_arr");
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
