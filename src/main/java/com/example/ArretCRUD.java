package com.example;

import java.sql.Connection;
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
import dao.ArretDAO;
//import com.example.DbConnection;
//import com.example.RestServer;
import classes.Arret;

@RestController
@CrossOrigin
@RequestMapping("/api/arret") 
public class ArretCRUD {

    @Autowired
    private DataSource dataSource;
    
    @GetMapping("/")
    ArrayList<Arret> allArret(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ArretDAO arret = new ArretDAO(connection);
            ArrayList<Arret> L = arret.readAllArret();
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

    @GetMapping("/{arretId}")
    Arret read(@PathVariable(value="arretId") String id_arr, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ArretDAO arretDAO = new ArretDAO(connection);
            Arret a = arretDAO.readWithId_arr(id_arr);
            connection.close();
            if(a.id_arr.equals("null")) {
                throw new Exception("Arret inexistant");
            } else {
                return a;
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
    //Renvoyer une erreur 412 si l'identifiant de l'arret dans l'URL n'est pas le même que celui de l'arret dans le corp de la requête.
    @PostMapping("/{arretId}")
    Arret create(@PathVariable(value="arretId") String id_arr, @RequestBody Arret a, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(a.id_arr.equals(id_arr)) {
                ArretDAO arretDAO = new ArretDAO(connection);
                Arret aNew = arretDAO.readWithId_arr(id_arr);
                if(aNew.id_arr == null) {
                    arretDAO.create(a);
                    aNew = arretDAO.readWithId_arr(id_arr);
                    connection.close();
                    return aNew;
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

    //Renvoyer une erreur 404 si l'identifiant de l'utilisateur ne correspond pas à un utilisateur dans la base.
    //Renvoyer une erreur 412 si l'identifiant du Arret dans l'URL n'est pas le même que celui de l'arret dans le corp de la requête.
    @PutMapping("/{arretId}") 
    Arret update(@PathVariable(value="arretId") String id_arr, @RequestBody Arret a, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(a.id_arr.equals(id_arr)) {
                ArretDAO arretDAO = new ArretDAO(connection);
                Arret aNew = arretDAO.readWithId_arr(id_arr);
                if(aNew.id_arr == null) {
                    throw new Exception("ERROR404");
                } else {
                    arretDAO.update(a);
                    aNew = arretDAO.readWithId_arr(id_arr);
                    connection.close();
                    return aNew;
                }
            } else {
                throw new Exception("ERROR412");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if(e.getMessage().equals("ERROR412")) {
                response.setStatus(412);
            } else if(e.getMessage().equals("ERROR404")) {
                response.setStatus(404);
            }
            return null;
        }
    }

    //Renvoyer une erreur 404 si l'identifiant de l'arret ne correspond pas à un arret dans la base.
    @DeleteMapping("/{arretId}")
    void delete(@PathVariable(value="arretId") String id_arr, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
                ArretDAO arretDAO = new ArretDAO(connection);
                Arret aOld = arretDAO.readWithId_arr(id_arr);
                if(aOld.id_arr == null) {
                    throw new Exception("ERROR404");
                } else {
                    arretDAO.delete(aOld);
                    connection.close();
                }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if(e.getMessage().equals("ERROR404")) {
                response.setStatus(404);
            }
        }

    }
}
