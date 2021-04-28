package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import classes.Question;

public class QuestionDAO extends DAO<Question> {

    public QuestionDAO(Connection conn) {
        super(conn);
    }

    /* ---- Création d'une nouvelle question ---- */
    @Override
    public boolean create(Question obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO question VALUES ('"+obj.getId_qst()+"','"+obj.getLabel_qst()+"','"+obj.getDescription_qst()+"','"+obj.getSecret_qst()+"','"+obj.getPoints_qst()+"','"+obj.getId_defi()+"')");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Création d'une nouvelle question SANS ID ---- */
    
    public boolean createSansID(Question obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO question (label_qst, description_qst, secret_qst, points_qst, id_defi) VALUES ('"+obj.getLabel_qst()+"','"+obj.getDescription_qst()+"','"+obj.getSecret_qst()+"',"+obj.getPoints_qst()+",'"+obj.getId_defi()+"')");
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
    public Question read(int id) {
        return null;
    }

    /* ---- Update d'une question ---- */
    @Override
    public boolean update(Question obj) {
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("UPDATE question SET id_qst = '"+obj.getId_qst()+"', label_qst = '"+obj.getLabel_qst()+"', description_qst = '"+obj.getDescription_qst()+"', secret_qst = '"+obj.getSecret_qst()+"', points_qst = "+obj.getPoints_qst()+", id_defi = '"+obj.getId_defi()+"' WHERE id_qst = '"+obj.getId_qst()+"'");   
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Suppression d'une question ---- */
    @Override
    public boolean delete(Question obj) {
        int nb = 0;
        try {
        nb = this.connect.createStatement().executeUpdate("DELETE FROM question WHERE id_qst ='"+obj.getId_qst()+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Suppression la liste des questions en fonction de id_defi ---- */
    public boolean deleteWithId_defi(String iddefi) {
        int nb = 0;
        try {
        nb = this.connect.createStatement().executeUpdate("DELETE FROM question WHERE id_defi ='"+iddefi+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Lecture d'une question selon son id ---- */
    public Question readWithId(String id) {
        Question q = new Question();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM question WHERE id_qst = '"+id+"'");
        if (rs.next()) {
            q.setId_qst(rs.getString("id_qst"));
            q.setLabel_qst(rs.getString("label_qst"));
            q.setDescription_qst(rs.getString("description_qst"));
            q.setSecret_qst(rs.getString("secret_qst"));
            q.setPoints_qst(rs.getInt("points_qst"));
            q.setId_defi(rs.getString("id_defi"));
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return q;
    }
    
    /* ---- Lecture de toutes les questions ---- */
    public ArrayList<Question> readAllQuestion() {
        ArrayList<Question> L = new ArrayList<Question>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM question");
            while (rs.next()) {
                Question q = new Question();
                q.setId_qst(rs.getString("id_qst"));
                q.setLabel_qst(rs.getString("label_qst"));
                q.setDescription_qst(rs.getString("description_qst"));
                q.setSecret_qst(rs.getString("secret_qst"));
                q.setPoints_qst(rs.getInt("points_qst"));
                q.setId_defi(rs.getString("id_defi"));
                L.add(q);
            }
            stmt.close();
            //connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }
    
    public Integer getCurrentIncrement(){
        //String id = "QST";
        Integer lv = 0;
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT last_value FROM seq_qst");
            if (rs.next()) {
                lv = rs.getInt("last_value");
            }
            
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lv;
    }

    public Integer getNext(){
        //String id = "QST";
        Integer lv = 0;
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select nextval('seq_qst')");
            if (rs.next()) {
                lv = rs.getInt("nextval");
            }
            
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lv;
    }

    public boolean QuestionExist(Integer id) {
        boolean res = false;
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM question where id_qst = 'QST"+id+"'");
            if (rs.next()) {
                res = true;
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /* ---- Affichage de la liste de toutes les questions en fonction de l'id de defi ---- */
    public ArrayList<Question> readAllQuestionWithId_defi(String id) {
        ArrayList<Question> L = new ArrayList<Question>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM QUESTION WHERE id_defi = '"+id+"'");
            while (rs.next()) {
                Question q = new Question();
                q.setId_qst(rs.getString("id_qst"));
                q.setLabel_qst(rs.getString("label_qst"));
                q.setDescription_qst(rs.getString("description_qst"));
                q.setSecret_qst(rs.getString("secret_qst"));
                q.setPoints_qst(rs.getInt("points_qst"));
                q.setId_defi(rs.getString("id_defi"));
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
