package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import classes.Defis;

public class DefisDAO extends DAO<Defis> {

    public DefisDAO(Connection conn) {
        super(conn);
    }

    /* ---- Création d'un nouveau defi ---- */
    @Override
    public boolean create(Defis obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO defi VALUES ('"+obj.defi+"','"+obj.titre+"','"+obj.dateDeCreation+"','"+obj.description+"','"+obj.auteur+"','"+obj.code_arret+"','"+obj.type+"','"+obj.dateDeModification+"',"+obj.version+",'"+obj.arret+"',"+obj.points+",'"+obj.duree+"','"+obj.prologue+"','"+obj.epilogue+"','"+obj.commentaire+"')");
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
    public Defis read(int id) {
        return null;
    }

    /* ---- Modification d'un defi ---- */
    @Override
    public boolean update(Defis obj) {
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("UPDATE defi SET titre = '"+obj.titre+"', dateDeCreation = '"+obj.dateDeCreation+"',  description = '"+obj.description+"', auteur = '"+obj.auteur+"', code_arret = '"+obj.code_arret+"', type = '"+obj.type+"', dateDeModification = '"+obj.dateDeModification+"', version = '"+obj.version+"',  arret = '"+obj.arret+"', points = '"+obj.points+"', duree = '"+obj.duree+"', prologue = '"+obj.prologue+"', epilogue = '"+obj.epilogue+"', commentaire = '"+obj.commentaire+"'WHERE defi = '"+obj.defi+"'");   
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Suppression d'un defi ---- */
    @Override
    public boolean delete(Defis obj) {
        int nb = 0;
        try {
        nb = this.connect.createStatement().executeUpdate("delete from defi where defi ='"+obj.defi+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    /* ---- Affichage de tous les defis voulus ---- */
    public Defis readWithId(String id) {
        Defis d = new Defis();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM defi WHERE defi = '"+id+"'");
        if (rs.next()) {
            d.defi = rs.getString("defi");
            d.titre   = rs.getString("titre");
            d.dateDeCreation   = rs.getString("dateDeCreation");
            d.description   = rs.getString("description");
            d.auteur   = rs.getString("auteur");
            d.code_arret   = rs.getString("code_arret");
            d.type = rs.getString("type");
            d.dateDeModification = rs.getString("dateDeModification");
            d.version = rs.getInt("version");
            d.arret   = rs.getString("arret");
            d.points   = rs.getInt("points");
            d.duree   = rs.getString("duree");
            d.prologue = rs.getString("prologue");
            d.epilogue   = rs.getString("epilogue");
            d.commentaire = rs.getString("commentaire");
            
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }
    
    /* ---- Affichage de la liste de tous les defis ---- */
    public ArrayList<Defis> readAllDefis() {
        ArrayList<Defis> L = new ArrayList<Defis>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM defi");
            while (rs.next()) {
                Defis d = new Defis();
                d.defi = rs.getString("defi");
                d.titre   = rs.getString("titre");
                d.dateDeCreation   = rs.getString("dateDeCreation");
                d.description   = rs.getString("description");
                d.auteur   = rs.getString("auteur");
                d.code_arret   = rs.getString("code_arret");
                d.type = rs.getString("type");
                d.dateDeModification = rs.getString("dateDeModification");
                d.version = rs.getInt("version");
                d.arret   = rs.getString("arret");
                d.points   = rs.getInt("points");
                d.duree   = rs.getString("duree");
                d.prologue = rs.getString("prologue");
                d.epilogue   = rs.getString("epilogue");
                d.commentaire = rs.getString("commentaire");
                L.add(d);
            }
            stmt.close();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }

    public ArrayList<Defis> readAllDefisByAuteur(String auteur) {
        ArrayList<Defis> L = new ArrayList<Defis>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM defi WHERE auteur = '"+auteur+"'");
            while (rs.next()) {
                Defis d = new Defis();
                d.defi = rs.getString("defi");
                d.titre   = rs.getString("titre");
                d.dateDeCreation   = rs.getString("dateDeCreation");
                d.description   = rs.getString("description");
                d.auteur   = rs.getString("auteur");
                d.code_arret   = rs.getString("code_arret");
                d.type = rs.getString("type");
                d.dateDeModification = rs.getString("dateDeModification");
                d.version = rs.getInt("version");
                d.arret   = rs.getString("arret");
                d.points   = rs.getInt("points");
                d.duree   = rs.getString("duree");
                d.prologue = rs.getString("prologue");
                d.epilogue   = rs.getString("epilogue");
                d.commentaire = rs.getString("commentaire");
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
