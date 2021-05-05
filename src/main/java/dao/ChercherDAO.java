package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import classes.Chercher;
import classes.Defis;
import classes.MotClef;

public class ChercherDAO extends DAO<Chercher> {

    public ChercherDAO(Connection conn) {
        super(conn);
    }

    /* ---- Création d'un nouvel élément ---- */
    @Override
    public boolean create(Chercher obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO chercher VALUES ('"+obj.getId_defi()+"','"+obj.getId_mc()+"')");
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


    /* ---- Suppression d'un élément ---- */
    @Override
    public boolean delete(Chercher obj) {
        int nb = 0;
        try {
        nb = this.connect.createStatement().executeUpdate("DELETE FROM chercher WHERE id_defi ='"+obj.getId_defi()+"' AND id_mc = '"+obj.getId_mc()+"'");
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

    /* ---- Affichage de tous les éléments voulus en fonction de id_defi ---- */
    public Chercher readWithId_defi(String id) {
        Chercher ch = new Chercher();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM chercher WHERE id_defi = '"+id+"'");
        if (rs.next()) {
            ch.setId_defi(rs.getString("id_defi"));
            ch.setId_mc(rs.getString("id_mc"));
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ch;
    }

    /* ---- Affichage de tous les éléments voulus en fonction de id_mc---- */
    public Chercher readWithId_mc(String id) {
        Chercher ch = new Chercher();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM chercher WHERE id_mc = '"+id+"'");
        if (rs.next()) {
            ch.setId_defi(rs.getString("id_defi"));
            ch.setId_mc(rs.getString("id_mc"));
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ch;
    }

    

    /* ---- Affichage de l'élément voulu---- */
    public Chercher readWithTwoId(String id1,String id2) {
        Chercher ch = new Chercher();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM chercher WHERE id_defi = '"+id1+"' AND id_mc = '"+id2+"'");
        if (rs.next()) {
            ch.setId_defi(rs.getString("id_defi"));
            ch.setId_mc(rs.getString("id_mc"));
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ch;
    }

    /* ---- Affichage de la liste de tous les éléments ---- */
    public ArrayList<Chercher> readAllChercher() {
        ArrayList<Chercher> L = new ArrayList<Chercher>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM chercher");
            while (rs.next()) {
                Chercher ch = new Chercher();
                ch.setId_defi(rs.getString("id_defi"));
                ch.setId_mc(rs.getString("id_mc"));
                L.add(ch);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }

    /* ---- Affichage de la liste de tous les defis en fonction de id_mc ---- */
    public ArrayList<Defis> readAllDefiWithMot_mc(String mc) {
        ArrayList<Defis> L = new ArrayList<Defis>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM defi INNER JOIN CHERCHER ON DEFI.defi=CHERCHER.id_defi INNER JOIN MOT_CLEF ON CHERCHER.id_mc=MOT_CLEF.id_mc WHERE mot_mc = '"+mc+"'");
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
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }

    /* ---- Affichage de la liste de tous les mots clefs en fonction de id_defi ---- */
    public ArrayList<MotClef> readAllMcWithId_defi(String id) {
        ArrayList<MotClef> L = new ArrayList<MotClef>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM mot_clef INNER JOIN CHERCHER ON mot_clef.id_mc=CHERCHER.id_mc INNER JOIN defi ON CHERCHER.id_defi=DEFI.defi WHERE defi = '"+id+"'");
            while (rs.next()) {
                MotClef mc = new MotClef();
                mc.setId_mc(rs.getString("id_mc"));
                mc.setMot_mc(rs.getString("mot_mc"));
                L.add(mc);
            }
            stmt.close();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }
    
    /* ---- Affichage de la liste de tous les Id_mc selon l'id defi en parametre ---- */
    public ArrayList<MotClef> readAllMcIdWithId_defi(String id) {
        ArrayList<MotClef> L = new ArrayList<MotClef>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM chercher WHERE id_defi = '"+id+"'");
            while (rs.next()) {
                MotClef mc = new MotClef();
                mc.setId_mc(rs.getString("id_mc"));
                mc.setMot_mc("");
                L.add(mc);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }

    /* ---- Affichage de la liste de tous les defis en fonction de id_mc ---- */
    public ArrayList<Defis> readAllDefiWithMot_mcAndType(String mc, String type) {
        ArrayList<Defis> L = new ArrayList<Defis>();
        try {
            System.out.println(mc+" "+type);
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM defi INNER JOIN CHERCHER ON DEFI.defi=CHERCHER.id_defi INNER JOIN MOT_CLEF ON CHERCHER.id_mc=MOT_CLEF.id_mc WHERE mot_mc = '"+mc+"' AND type = '"+type+"'");
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
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }
}
