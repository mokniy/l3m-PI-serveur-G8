package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import classes.Evaluation;

public class EvaluationDAO extends DAO<Evaluation> {

    public EvaluationDAO(Connection conn) {
        super(conn);
    }

    /* ---- Création d'une nouvelle Evaluation ---- */
    @Override
    public boolean create(Evaluation obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO Evaluation VALUES ('"+obj.getId_eval()+"','"+obj.getId_vis()+"','"+obj.getId_rep()+"')");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Création d'une nouvelle Evaluation SANS ID ---- */
    
    public boolean createSansID(Evaluation obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO Evaluation (id_vis, id_rep) VALUES ('"+obj.getId_vis()+"','"+obj.getId_rep()+"')");
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
    public Evaluation read(int id) {
        return null;
    }

    /* ---- Update d'une Evaluation ---- */
    @Override
    public boolean update(Evaluation obj) {
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("UPDATE Evaluation SET id_eval = '"+obj.getId_eval()+"', id_vis = '"+obj.getId_vis()+"', id_rep = '"+obj.getId_rep()+"' WHERE id_eval = '"+obj.getId_eval()+"'");   
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Suppression d'une Evaluation ---- */
    @Override
    public boolean delete(Evaluation obj) {
        int nb = 0;
        try {
        nb = this.connect.createStatement().executeUpdate("DELETE FROM Evaluation WHERE id_eval ='"+obj.getId_eval()+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    
    /* ---- Lecture d'une Evaluation selon son id ---- */
    public Evaluation readWithId(String id) {
        Evaluation ev = new Evaluation();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Evaluation WHERE id_eval = '"+id+"'");
        if (rs.next()) {
            ev.setId_eval(rs.getString("id_eval"));
            ev.setId_vis(rs.getString("id_vis"));
            ev.setId_rep(rs.getString("id_rep"));
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ev;
    }
    
    /* ---- Lecture de toutes les Evaluations ---- */
    public ArrayList<Evaluation> readAllEvaluation() {
        ArrayList<Evaluation> L = new ArrayList<Evaluation>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Evaluation");
            while (rs.next()) {
                Evaluation ev = new Evaluation();
                ev.setId_eval(rs.getString("id_eval"));
                ev.setId_vis(rs.getString("id_vis"));
                ev.setId_rep(rs.getString("id_rep"));
                L.add(ev);
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
            ResultSet rs = stmt.executeQuery("SELECT last_value FROM seq_eval");
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
            ResultSet rs = stmt.executeQuery("select nextval('seq_eval')");
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
    public boolean EvaluationExist(Integer id) {
        boolean res = false;
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Evaluation where id_eval = 'EV"+id+"'");
            if (rs.next()) {
                res = true;
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    

}
