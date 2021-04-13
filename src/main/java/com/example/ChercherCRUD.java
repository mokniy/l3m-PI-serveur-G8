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
import dao.ChercherDAO;
//import com.example.DbConnection;
//import com.example.RestServer;
import classes.Chercher;

@RestController
@CrossOrigin
@RequestMapping("/api/chercher") 
public class ChercherCRUD {

    @Autowired
    private DataSource dataSource;
    
    @GetMapping("/")
    ArrayList<Chercher> allChercher(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ChercherDAO chercherDAO = new ChercherDAO(connection);
            ArrayList<Chercher> L = chercherDAO.readAllChercher();
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

    @GetMapping("/withDefi/{defiId}")
    Chercher read(@PathVariable(value="defiId") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ChercherDAO chercherDAO = new ChercherDAO(connection);
            Chercher ch = chercherDAO.readWithId_defi(id);
            connection.close();
            if(ch.id_defi.equals("null")) {
                throw new Exception("Chercher inexistant");
            } else {
                return ch;
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

    /* ---- Rechercher par mot-clef, les différents defis qui l'utilise ----

    @GetMapping("/withMc:{mcId}")
    Chercher read(@PathVariable(value="mcId") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ChercherDAO chercherDAO = new ChercherDAO(connection);
            Chercher ch = chercherDAO.readWithId_mc(id);
            connection.close();
            if(ch.id_mc.equals("null")) {
                throw new Exception("Chercher inexistant");
            } else {
                return ch;
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
    }*/




    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant du Defi dans l'URL n'est pas le même que celui du Defi dans le corp de la requête.
    @PostMapping("/{defiId}&{mcId}")
    Chercher create(@PathVariable(value="defiId") String id1, @PathVariable(value="mcId") String id2, @RequestBody Chercher ch, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(ch.id_defi.equals(id1)) {
                if(ch.id_mc.equals(id2)){
                    ChercherDAO chercherDAO = new ChercherDAO(connection);
                    Chercher chNew = chercherDAO.readWithTwoId(id1,id2);
                    if(chNew.id_defi == null) {
                        chercherDAO.create(ch);
                        chNew = chercherDAO.readWithTwoId(id1,id2);
                        connection.close();
                        return chNew;
                    } else {
                        throw new Exception("ERROR403");
                    }
                }  else {
                    throw new Exception("ERROR412");
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
    @DeleteMapping("/{defiId}&{mcId}")
    void delete(@PathVariable(value="defiId") String id1, @PathVariable(value="mcId") String id2, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
                ChercherDAO chercherDAO = new ChercherDAO(connection);
                Chercher chOld = chercherDAO.readWithTwoId(id1,id2);
                if(chOld.id_defi == null) {
                    throw new Exception("ERROR404");
                } else {
                    if (chOld.id_mc == null){
                        throw new Exception("ERROR404");
                    } else {
                        chercherDAO.delete(chOld);
                        connection.close();
                    }
                }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if(e.getMessage().equals("ERROR404")) {
                response.setStatus(404);
            }
        }

    }
}