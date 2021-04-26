package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import classes.Arret;
import classes.Defis;

public class ArretDAO extends DAO<Arret> {

    public ArretDAO(Connection conn) {
        super(conn);
    }

    /* ---- Création d'un nouvel élément ---- */
    @Override
    public boolean create(Arret obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO arret VALUES ('"+obj.getCode()+"','"+obj.getLib_arret()+"','"+obj.getStreetMap()+"')");
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
            nb = this.connect.createStatement().executeUpdate("UPDATE arret SET code = '"+obj.getCode()+"', lib_arret = '"+obj.getLib_arret()+"', streetMap = '"+obj.getStreetMap()+"' where code= '"+obj.getCode()+"'");  
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
        nb = this.connect.createStatement().executeUpdate("delete from arret where code ='"+obj.getCode()+"'");
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
            a.setCode(rs.getString("code"));
            a.setLib_arret(rs.getString("lib_arret"));
            a.setStreetMap(rs.getString("streetMap"));
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }
    

    /* ---- Retourne le nombre de défi présent à un arrêt  ---- */
    public int getNbDefi(String code) {
        int nb_defi = 0;
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as nb_defi FROM defi WHERE code_arret = '"+code+"'");
        if (rs.next()) {
            nb_defi = rs.getInt("nb_defi");
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nb_defi;
    }

    /* ---- Retourne les défi présent à un arrêt  ---- */
    public ArrayList<Defis> getDefi(String code) {
        ArrayList<Defis> L = new ArrayList<Defis>();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ARRET inner join defi d2 on d2.code_arret = arret.code where code = '"+code+"'");
        while (rs.next()) {
            Defis d = new Defis();
            d.setDefi(rs.getString("defi"));
            d.setTitre(rs.getString("titre"));
            d.setDateDeCreation(rs.getString("dateDeCreation"));
            d.setDescription(rs.getString("description"));
            d.setAuteur(rs.getString("auteur"));
            d.setCode_arret(rs.getString("code_arret"));
            d.setType(rs.getString("type"));
            d.setDateDeModification(rs.getString("dateDeModification"));
            d.setVersion(rs.getInt("version"));
            d.setArret(rs.getString("arret"));
            d.setPoints(rs.getInt("points"));
            d.setDuree(rs.getString("duree"));
            d.setPrologue(rs.getString("prologue"));
            d.setEpilogue(rs.getString("epilogue"));
            d.setCommentaire(rs.getString("commentaire"));
            L.add(d);
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }

    /* ---- Affichage de la liste de tous les éléments ---- */
    public ArrayList<Arret> readAllArret() {
        ArrayList<Arret> L = new ArrayList<Arret>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM arret");
            while (rs.next()) {
                Arret a = new Arret();
                a.setCode(rs.getString("code"));
                a.setLib_arret(rs.getString("lib_arret"));
                a.setStreetMap(rs.getString("streetMap"));
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
                a.setCode(rs.getString("code"));
                a.setLib_arret(rs.getString("lib_arret"));
                a.setStreetMap(rs.getString("streetMap"));
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
