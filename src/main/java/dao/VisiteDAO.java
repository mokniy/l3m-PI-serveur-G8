package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import classes.Visite;

public class VisiteDAO extends DAO<Visite> {

    public VisiteDAO(Connection conn) {
        super(conn);
    }

    /* ---- Création d'une nouvelle visite ---- */
    @Override
    public boolean create(Visite obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO visite VALUES ('"+obj.getId_vis()+"','"+obj.getDate_vis()+"','"+obj.getMode_vis()+"','"+obj.getStatut_vis()+"',"+obj.getPts_vis()+","+obj.getScore_vis()+",'"+obj.getTemps_vis()+"','"+obj.getId_visiteur()+"','"+obj.getId_defi()+"','"+obj.getCommentaire()+"','"+obj.getIndice_utilise_vis()+"')");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Création d'une nouvelle visite SANS ID ---- */
    
    public boolean createSansID(Visite obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO visite (date_vis, mode_vis, statut_vis, pts_vis, score_vis, temps_vis, id_visiteur, id_defis, commentaire, indice_utilise_vis) VALUES ('"+obj.getDate_vis()+"','"+obj.getMode_vis()+"','"+obj.getStatut_vis()+"',"+obj.getPts_vis()+","+obj.getScore_vis()+",'"+obj.getTemps_vis()+"','"+obj.getId_visiteur()+"','"+obj.getId_defi()+"','"+obj.getCommentaire()+"','"+obj.getIndice_utilise_vis()+"' )");
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
    public Visite read(int id) {
        return null;
    }

    /* ---- Update d'une visite ---- */
    @Override
    public boolean update(Visite obj) {
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("UPDATE visite SET id_vis = '"+obj.getId_vis()+"', date_vis = '"+obj.getDate_vis()+"', mode_vis = '"+obj.getMode_vis()+"', statut_vis = '"+obj.getStatut_vis()+"', pts_vis = "+obj.getPts_vis()+", score_vis = "+obj.getScore_vis()+", temps_vis = '"+obj.getTemps_vis()+"', id_visiteur = '"+obj.getId_visiteur()+"', id_defis = '"+obj.getId_defi()+"', commentaire='"+obj.getCommentaire()+"', indice_utilise_vis='"+obj.getIndice_utilise_vis()+"' WHERE id_vis = '"+obj.getId_vis()+"'");   
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Suppression d'une visite ---- */
    @Override
    public boolean delete(Visite obj) {
        int nb = 0;
        try {
        nb = this.connect.createStatement().executeUpdate("delete from visite where id_vis ='"+obj.getId_vis()+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Lecture d'une visite selon son id ---- */
    public Visite readWithId(String id) {
        Visite v = new Visite();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM visite WHERE id_vis = '"+id+"'");
        if (rs.next()) {
            v.setId_vis(rs.getString("id_vis"));
            v.setDate_vis(rs.getString("date_vis"));
            v.setMode_vis(rs.getString("mode_vis"));
            v.setStatut_vis(rs.getString("statut_vis"));
            v.setPts_vis(rs.getInt("pts_vis"));
            v.setScore_vis(rs.getInt("score_vis"));
            v.setTemps_vis(rs.getString("temps_vis"));
            v.setId_visiteur(rs.getString("id_visiteur"));
            v.setId_defi(rs.getString("id_defis"));
            v.setCommentaire(rs.getString("commentaire"));
            v.setIndice_utilise_vis(rs.getString("indice_utilise_vis"));
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return v;
    }
    
    /* ---- Lecture de toutes les visites ---- */
    public ArrayList<Visite> readAllVisite() {
        ArrayList<Visite> L = new ArrayList<Visite>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM visite");
            while (rs.next()) {
                Visite v = new Visite();
                v.setId_vis(rs.getString("id_vis"));
                v.setDate_vis(rs.getString("date_vis"));
                v.setMode_vis(rs.getString("mode_vis"));
                v.setStatut_vis(rs.getString("statut_vis"));
                v.setPts_vis(rs.getInt("pts_vis"));
                v.setScore_vis(rs.getInt("score_vis"));
                v.setTemps_vis(rs.getString("temps_vis"));
                v.setId_visiteur(rs.getString("id_visiteur"));
                v.setId_defi(rs.getString("id_defis"));
                v.setCommentaire(rs.getString("commentaire"));
                v.setIndice_utilise_vis(rs.getString("indice_utilise_vis"));
                L.add(v);
            }
            stmt.close();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }

    /* ---- Retourne la derniere valeur de la sequence seq_vis ---- */
    public Integer getCurrentIncrement(){
        //String id = "V";
        Integer lv = 0;
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT last_value FROM seq_vis");
            if (rs.next()) {
                lv = rs.getInt("last_value");
            }
            
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lv;
    }

    /* ---- Incremente et retourne la valeur de la sequence seq_vis ---- */
    public Integer getNext(){
        //String id = "QST";
        Integer lv = 0;
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select nextval('seq_vis')");
            if (rs.next()) {
                lv = rs.getInt("nextval");
            }
            
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lv;
    }

    /* ---- retourne true si l'id en parametre existe ---- */
    public boolean VisiteExist(Integer id) {
        boolean res = false;
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM visite where id_vis = 'V"+id+"'");
            if (rs.next()) {
                res = true;
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public ArrayList<Visite> allVisiteUnDefi(String code) {
        ArrayList<Visite> L = new ArrayList<Visite>();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM VISITE where id_defis = '"+code+"'");
        while (rs.next()) {
            Visite v = new Visite();
            v.setId_vis(rs.getString("id_vis"));
            v.setDate_vis(rs.getString("date_vis"));
            v.setMode_vis(rs.getString("mode_vis"));
            v.setStatut_vis(rs.getString("statut_vis"));
            v.setPts_vis(rs.getInt("pts_vis"));
            v.setScore_vis(rs.getInt("score_vis"));
            v.setTemps_vis(rs.getString("temps_vis"));
            v.setId_visiteur(rs.getString("id_visiteur"));
            v.setId_defi(rs.getString("id_defis"));
            v.setCommentaire(rs.getString("commentaire"));
            v.setIndice_utilise_vis(rs.getString("indice_utilise_vis"));
            L.add(v);
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }

    public ArrayList<Visite> allVisiteWithPseudo(String id) {
        ArrayList<Visite> L = new ArrayList<Visite>();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM VISITE where id_visiteur = '"+id+"'");
        while (rs.next()) {
            Visite v = new Visite();
            v.setId_vis(rs.getString("id_vis"));
            v.setDate_vis(rs.getString("date_vis"));
            v.setMode_vis(rs.getString("mode_vis"));
            v.setStatut_vis(rs.getString("statut_vis"));
            v.setPts_vis(rs.getInt("pts_vis"));
            v.setScore_vis(rs.getInt("score_vis"));
            v.setTemps_vis(rs.getString("temps_vis"));
            v.setId_visiteur(rs.getString("id_visiteur"));
            v.setId_defi(rs.getString("id_defis"));
            v.setCommentaire(rs.getString("commentaire"));
            v.setIndice_utilise_vis(rs.getString("indice_utilise_vis"));
            L.add(v);
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }
}
