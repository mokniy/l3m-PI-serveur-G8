package com.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dao.UserDAO;

@RestController
@CrossOrigin
@RequestMapping("/api/users") 
public class UserCRUD {

    @Autowired
    private DataSource dataSource;
    
    @GetMapping("/")
    ArrayList<User> allUsers(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            UserDAO user = new UserDAO(connection);
            ArrayList<User> L = user.readAllUser();
            return L;
        } catch (Exception e) {
            response.setStatus(500);
            try {
                response.getOutputStream().print( e.getMessage() );
            } catch (Exception e2) {
                System.err.println(e2.getMessage());
            }
            System.err.println(e.getMessage());
            return null;
        }
    }

    @GetMapping("/{userId}")
    User read(@PathVariable("userId") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            UserDAO user = new UserDAO(connection);
            User u = user.readWithLogin(id);
            if(u.login.equals("null")) {
                throw new Exception("User inexistant");
            } else {
                return u;
            }
        } catch (Exception e) {
            response.setStatus(404);
            try {
                response.getOutputStream().print( e.getMessage() );
            } catch (Exception e2) {
                System.err.println(e2.getMessage());
            }
            System.err.println(e.getMessage());
            return null;
        }
    }

}
