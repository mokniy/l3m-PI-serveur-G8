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

    @Override
    public boolean create(MotClef obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO mot_clef VALUES ('"+obj.id_mc+"','"+obj.mot_mc+"')");
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

    @Override
    public boolean update(MotClef obj) {
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("UPDATE mot_clef SET id_mc = '"+obj.id_mc+"', mot_mc = '"+obj.mot_mc+"' WHERE id_mc = '"+obj.id_mc+"'");   
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
    public boolean delete(MotClef obj) {
        int nb = 0;
        try {
        nb = this.connect.createStatement().executeUpdate("delete from mot_clef where id_mc ='"+obj.id_mc+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    public MotClef readWithId(String id) {
        MotClef mc = new MotClef();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM mot_clef WHERE id = '"+id+"'");
        if (rs.next()) {
            mc.id_mc = rs.getString("id_mc");
            mc.mot_mc  = rs.getString("mot_mc");
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mc;
    }
    
    public ArrayList<MotClef> readAllMotClefs() {
        ArrayList<MotClef> L = new ArrayList<MotClef>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM mot_clef");
            while (rs.next()) {
                MotClef mc = new MotClef();
                mc.id_mc = rs.getString("id_mc");
                mc.mot_mc  = rs.getString("mot_mc");
                L.add(mc);
            }
            stmt.close();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }
}
