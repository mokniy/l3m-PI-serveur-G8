package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import classes.Reponse;

public class ReponseDAO extends DAO<Reponse> {

    public ReponseDAO(Connection conn) {
        super(conn);
    }

    /* ---- Création d'une nouvelle Reponse ---- */
    @Override
    public boolean create(Reponse obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO Reponse VALUES ('"+obj.getId_rep()+"','"+obj.getReponse_rep()+"','"+obj.getId_qst()+"')");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Création d'une nouvelle Reponse SANS ID ---- */
    
    public boolean createSansID(Reponse obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO Reponse (reponse_rep, id_qst) VALUES ('"+obj.getReponse_rep()+"','"+obj.getId_qst()+"')");
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
    public Reponse read(int id) {
        return null;
    }

    /* ---- Update d'une Reponse ---- */
    @Override
    public boolean update(Reponse obj) {
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("UPDATE Reponse SET id_rep = '"+obj.getId_rep()+"', reponse_rep = '"+obj.getReponse_rep()+"', id_qst = '"+obj.getId_qst()+"' WHERE id_rep = '"+obj.getId_rep()+"'");   
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Suppression d'une Reponse ---- */
    @Override
    public boolean delete(Reponse obj) {
        int nb = 0;
        try {
        nb = this.connect.createStatement().executeUpdate("DELETE FROM Reponse WHERE id_rep ='"+obj.getId_rep()+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Suppression la liste des Reponses en fonction de id_qst ---- */
    public boolean deleteWithId_qst(String idqst) {
        int nb = 0;
        try {
        nb = this.connect.createStatement().executeUpdate("DELETE FROM Reponse WHERE id_qst ='"+idqst+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Lecture d'une Reponse selon son id ---- */
    public Reponse readWithId(String id) {
        Reponse q = new Reponse();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Reponse WHERE id_rep = '"+id+"'");
        if (rs.next()) {
            q.setId_rep(rs.getString("id_rep"));
            q.setReponse_rep(rs.getString("reponse_rep"));
            q.setId_qst(rs.getString("id_qst"));
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return q;
    }
    
    /* ---- Lecture de toutes les Reponses ---- */
    public ArrayList<Reponse> readAllReponse() {
        ArrayList<Reponse> L = new ArrayList<Reponse>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Reponse");
            while (rs.next()) {
                Reponse q = new Reponse();
                q.setId_rep(rs.getString("id_rep"));
                q.setReponse_rep(rs.getString("reponse_rep"));
                q.setId_qst(rs.getString("id_qst"));
                L.add(q);
            }
            stmt.close();
            //connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }
    
    /* ---- Retourne la derniere valeur de la sequence seq_rep ---- */
    public Integer getCurrentIncrement(){
        //String id = "QST";
        Integer lv = 0;
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT last_value FROM seq_rep");
            if (rs.next()) {
                lv = rs.getInt("last_value");
            }
            
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lv;
    }

    /* ---- Incremente et retourne la derniere valeur de la sequence seq_rep ---- */
    public Integer getNext(){
        //String id = "REP";
        Integer lv = 0;
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select nextval('seq_rep')");
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
    public boolean ReponseExist(Integer id) {
        boolean res = false;
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Reponse where id_rep = 'REP"+id+"'");
            if (rs.next()) {
                res = true;
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /* ---- Affichage de la liste de toutes les Reponses en fonction de l'id de question ---- */
    public ArrayList<Reponse> readAllReponseWithId_qst(String id) {
        ArrayList<Reponse> L = new ArrayList<Reponse>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Reponse WHERE id_qst = '"+id+"'");
            while (rs.next()) {
                Reponse q = new Reponse();
                q.setId_rep(rs.getString("id_rep"));
                q.setReponse_rep(rs.getString("reponse_rep"));
                q.setId_qst(rs.getString("id_qst"));
                L.add(q);
            }
            stmt.close();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }

}
