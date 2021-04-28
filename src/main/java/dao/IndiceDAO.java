package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import classes.Indice;

public class IndiceDAO extends DAO<Indice> {

    public IndiceDAO(Connection conn) {
        super(conn);
    }
    /*-------creation d'une nouvelle Indice ---------------*/
    @Override
    public boolean create(Indice obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO INDICE VALUES ('"+obj.getId_ind()+"','"+obj.getLabel_ind()+"','"+obj.getDescription_ind()+"',"+obj.getPoints_ind()+", '"+obj.getId_defi()+"' )");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Création d'un nouvel indice SANS ID ---- */
    
    public boolean createSansID(Indice obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO indice (label_ind, description_ind, points_ind, id_defi) VALUES ('"+obj.getLabel_ind()+"','"+obj.getDescription_ind()+"',"+obj.getPoints_ind()+", '"+obj.getId_defi()+"' )");
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
    public Indice read(int id) {
        return null;
    }
    /*-------Mise à jour d'une Indice ---------------*/
    @Override
    public boolean update(Indice obj) {
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("UPDATE indice SET label_ind = '"+obj.getLabel_ind()+"',description_ind = '"+obj.getDescription_ind()+"', points_ind = "+obj.getPoints_ind()+", id_defi = '"+obj.getId_defi()+"'  where id_ind = '"+obj.getId_ind()+"'");   
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }
    /*-------suppression d'une Indice ---------------*/
    @Override
    public boolean delete(Indice obj) {
        int nb = 0;
        try {
        nb = this.connect.createStatement().executeUpdate("delete from indice where id_ind ='"+obj.getId_ind()+"'");
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
        nb = this.connect.createStatement().executeUpdate("DELETE FROM indice WHERE id_defi ='"+iddefi+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /*------- Lecture d'une Indice selon son id ---------------*/
    public Indice readWithId(String id) {
        Indice i = new Indice();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM INDICE WHERE id_ind = '"+id+"'");
        if (rs.next()) {
            i.setId_ind(rs.getString("id_ind"));
            i.setLabel_ind(rs.getString("label_ind"));
            i.setDescription_ind(rs.getString("description_ind"));
            i.setPoints_ind(rs.getInt("points_ind"));
            i.setId_defi(rs.getString("id_defi"));
            
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }
    /*-------Affichage de la liste des Indices ---------------*/
    public ArrayList<Indice> readAllIndice() {
        ArrayList<Indice> L = new ArrayList<Indice>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM indice");
            while (rs.next()) {
                Indice i = new Indice();
                i.setId_ind(rs.getString("id_ind"));
                i.setLabel_ind(rs.getString("label_ind"));
                i.setDescription_ind(rs.getString("description_ind"));
                i.setPoints_ind(rs.getInt("points_ind"));
                i.setId_defi(rs.getString("id_defi"));
                L.add(i);
            }
            stmt.close();
            //connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }

    public Integer getCurrentIncrement(){
        Integer lv = 0;
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT last_value FROM seq_ind");
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
        Integer lv = 0;
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select nextval('seq_ind')");
            if (rs.next()) {
                lv = rs.getInt("nextval");
            }
            
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lv;
    }

    public boolean indiceExist(Integer id) {
        boolean res = false;
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM indice where id_ind = 'IND"+id+"'");
            if (rs.next()) {
                res = true;
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /* ---- Affichage de la liste de tous les indices en fonction de l'id de defi ---- */
    public ArrayList<Indice> readAllIndiceWithId_defi(String id) {
        ArrayList<Indice> L = new ArrayList<Indice>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM indice WHERE id_defi = '"+id+"'");
            while (rs.next()) {
                Indice i = new Indice();
                i.setId_ind(rs.getString("id_ind"));
                i.setLabel_ind(rs.getString("label_ind"));
                i.setDescription_ind(rs.getString("description_ind"));
                i.setPoints_ind(rs.getInt("points_ind"));
                i.setId_defi(rs.getString("id_defi"));
                L.add(i);
            }
            stmt.close();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }

}
