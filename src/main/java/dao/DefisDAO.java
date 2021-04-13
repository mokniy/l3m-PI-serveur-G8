package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.example.Defis;

public class DefisDAO extends DAO<Defis> {

    public DefisDAO(Connection conn) {
        super(conn);
    }

    @Override
    public boolean create(Defis obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO defi VALUES ('"+obj.id+"',"+obj.titre+","+obj.dateDeCreation+","+obj.description+","+obj.auteur+")");
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

    @Override
    public boolean update(Defis obj) {
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("UPDATE defi SET id = "+obj.id+" where id = '"+obj.id+"'");   
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
    public boolean delete(Defis obj) {
        int nb = 0;
        try {
        nb = this.connect.createStatement().executeUpdate("delete from defi where id ='"+obj.id+"'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(nb==1) {
            return true;
        } else {
            return false;
        }
    }

    public Defis readWithId(String id) {
        Defis d = new Defis();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM defi WHERE id = '"+id+"'");
        if (rs.next()) {
            d.id = rs.getString("id");
            d.titre   = rs.getString("titre");
            d.dateDeCreation = rs.getDate("dateDeCreation");
            d.description   = rs.getString("description");
            d.auteur   = rs.getString("auteur");
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }
    
    public ArrayList<Defis> readAllDefis() {
        ArrayList<Defis> L = new ArrayList<Defis>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM defi");
            while (rs.next()) {
                Defis d = new Defis();
                d.id = rs.getString("id");
                d.dateDeCreation rs.getDate("dateDeCreation");
                d.description = rs.getString("description");
                d.id = rs.getString("titre");
                d.auteur = rs.getString("auteur");
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
