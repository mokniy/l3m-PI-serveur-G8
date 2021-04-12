package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.example.User;

public class UserDAO extends DAO<User> {

    public UserDAO(Connection conn) {
        super(conn);
    }

    @Override
    public boolean create(User obj){
        int nb = 0;
        try {
            nb = this.connect.createStatement().executeUpdate("INSERT INTO users VALUES ('"+obj.login+"',"+obj.age+")");
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
    public User read(int id) {
        return null;
    }

    @Override
    public boolean update(User obj) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean delete(User obj) {
        // TODO Auto-generated method stub
        return false;
    }

    
    public User readWithLogin(String id) {
        User u = new User();
        try {
        Statement stmt = connect.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE login = '"+id+"'");
        if (rs.next()) {
            u.login = rs.getString("login");
            u.age   = rs.getInt("age");
        }
        stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }
    
    public ArrayList<User> readAllUser() {
        ArrayList<User> L = new ArrayList<User>();
        try {
            Statement stmt = connect.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                User u = new User();
                u.login = rs.getString("login");
                u.age   = rs.getInt("age");
                L.add(u);
            }
            stmt.close();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return L;
    }
}
