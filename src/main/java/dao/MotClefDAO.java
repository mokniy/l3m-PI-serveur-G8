package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import classes.MotClef;

public class MotClefDAO extends DAO<MotClef> {

    public MotClefDAO(Connection conn) {
        super(conn);
    }

    /* ---- Création d'un nouveau mot clef ---- */
    @Override
    public boolean create(MotClef obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO mot_clef VALUES ('"+obj.getId_mc()+"','"+obj.getMot_mc()+"')");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Création d'un nouveau mot clef SANS ID ---- */
    
    public boolean createSansID(MotClef obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO mot_clef (mot_mc) VALUES ('"+obj.getMot_mc()+"')");
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
    public MotClef read(int id) {
        return null;
    }

    /* ---- Update d'un mot cle ---- */
    @Override
    public boolean update(MotClef obj) {
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("UPDATE mot_clef SET id_mc = '"+obj.getId_mc()+"', mot_mc = '"+obj.getMot_mc()+"' WHERE id_mc = '"+obj.getId_mc()+"'");   
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Suppression d'un mot clef ---- */
    @Override
    public boolean delete(MotClef obj) {
        int nb = 0;
        try {
        nb = this.connect.createStatement().executeUpdate("delete from mot_clef where id_mc ='"+obj.getId_mc()+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Lecture d'un mot clef selon son id ---- */
    public MotClef readWithId(String id) {
        MotClef mc = new MotClef();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM mot_clef WHERE id_mc = '"+id+"'");
        if (rs.next()) {
            mc.setId_mc(rs.getString("id_mc"));
            mc.setMot_mc(rs.getString("mot_mc"));
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mc;
    }
    
    /* ---- Lecture de tous les mots clefs ---- */
    public ArrayList<MotClef> readAllMotClefs() {
        ArrayList<MotClef> L = new ArrayList<MotClef>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM mot_clef");
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
    
    public Integer getCurrentIncrement(){
        //String id = "MC";
        Integer lv = 0;
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT last_value FROM seq_mc");
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
        //String id = "MC";
        Integer lv = 0;
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("select nextval('seq_mc')");
            if (rs.next()) {
                lv = rs.getInt("nextval");
            }
            
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lv;
    }

    public boolean motClefExist(Integer id) {
        boolean res = false;
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM mot_clef where id_mc = 'MC"+id+"'");
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
