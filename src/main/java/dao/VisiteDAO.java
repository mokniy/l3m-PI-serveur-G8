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

    @Override
    public boolean create(Visite obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO visite VALUES ('"+obj.id_vis+"','"+obj.libelle_vis+"','"+obj.date_vis+"','"+obj.mode_vis+"','"+obj.statut_vis+"',"+obj.pts_vis+","+obj.score_vis+",'"+obj.temps_vis+"','"+obj.id_visiteur+"','"+obj.id_defi+"')");
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

    @Override
    public boolean update(Visite obj) {
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("UPDATE visite SET id_vis = '"+obj.id_vis+"', libelle_vis = '"+obj.libelle_vis+"', date_vis = '"+obj.date_vis+"', mode_vis = '"+obj.mode_vis+"', statut_vis = '"+obj.statut_vis+"', pts_vis = "+obj.pts_vis+", score_vis = "+obj.score_vis+", temps_vis = '"+obj.temps_vis+"', id_visiteur = '"+obj.id_visiteur+"', id_defis = '"+obj.id_defi+"' WHERE id_vis = '"+obj.id_vis+"'");   
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
    public boolean delete(Visite obj) {
        int nb = 0;
        try {
        nb = this.connect.createStatement().executeUpdate("delete from visite where id_vis ='"+obj.id_vis+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    public Visite readWithId(String id) {
        Visite v = new Visite();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM visite WHERE id_vis = '"+id+"'");
        if (rs.next()) {
            v.id_vis = rs.getString("id_vis");
            v.libelle_vis   = rs.getString("libelle_vis");
            v.date_vis = rs.getString("date_vis");
            v.mode_vis   = rs.getString("mode_vis");
            v.statut_vis   = rs.getString("statut_vis");
            v.pts_vis   = rs.getInt("pts_vis");
            v.score_vis   = rs.getInt("score_vis");
            v.temps_vis   = rs.getString("temps_vis");
            v.id_visiteur   = rs.getString("id_visiteur");
            v.id_defi   = rs.getString("id_defis");
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return v;
    }
    
    public ArrayList<Visite> readAllVisite() {
        ArrayList<Visite> L = new ArrayList<Visite>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM visite");
            while (rs.next()) {
                Visite v = new Visite();
                v.id_vis = rs.getString("id_vis");
                v.libelle_vis   = rs.getString("libelle_vis");
                v.date_vis = rs.getString("date_vis");
                v.mode_vis   = rs.getString("mode_vis");
                v.statut_vis   = rs.getString("statut_vis");
                v.pts_vis   = rs.getInt("pts_vis");
                v.score_vis   = rs.getInt("score_vis");
                v.temps_vis   = rs.getString("temps_vis");
                v.id_visiteur   = rs.getString("id_visiteur");
                v.id_defi   = rs.getString("id_defis");
                L.add(v);
            }
            stmt.close();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }
}
