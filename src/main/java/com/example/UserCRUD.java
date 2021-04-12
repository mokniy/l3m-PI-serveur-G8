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
    User read(@PathVariable(value="userId") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);
            User u = userDAO.readWithLogin(id);
            connection.close();
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

    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant du User dans l'URL n'est pas le même que celui du User dans le corp de la requête.
    @PostMapping("/{userId}")
    User create(@PathVariable(value="userId") String id, @RequestBody User u, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(u.login.equals(id)) {
                UserDAO userDAO = new UserDAO(connection);
                User uNew = userDAO.readWithLogin(id);
                if(uNew.login == null) {
                    userDAO.create(u);
                    uNew = userDAO.readWithLogin(id);
                    connection.close();
                    return uNew;
                } else {
                    throw new Exception("ERROR403");
                }
            } else {
                throw new Exception("ERROR412");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if(e.getMessage().equals("ERROR412")) {
                response.setStatus(412);
            } else if(e.getMessage().equals("ERROR403")) {
                response.setStatus(403);
            }
            return null;
        }
    }

}
