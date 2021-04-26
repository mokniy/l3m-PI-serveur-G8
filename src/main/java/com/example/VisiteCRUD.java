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
import dao.VisiteDAO;
//import com.example.DbConnection;
//import com.example.RestServer;
import classes.Visite;

@RestController
@CrossOrigin
@RequestMapping("/api/visite") 
public class VisiteCRUD {

    @Autowired
    private DataSource dataSource;
    
    /* Chercher toutes les visites de la base */
    @GetMapping("/")
    ArrayList<Visite> allVisite(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            VisiteDAO visiteDAO = new VisiteDAO(connection);
            ArrayList<Visite> L = visiteDAO.readAllVisite();
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

    /* Chercher la visite dont l'id est donne dans le path */
    @GetMapping("/{visiteId}")
    Visite read(@PathVariable(value="visiteId") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            VisiteDAO visiteDAO = new VisiteDAO(connection);
            Visite v = visiteDAO.readWithId(id);
            connection.close();
            if(v.getId_vis().equals("null")) {
                throw new Exception("Visite inexistante");
            } else {
                return v;
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

    /* Creer une visite */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant de Visite dans l'URL n'est pas le même que celui de Visite dans le corp de la requête.
    @PostMapping("/{visiteId}")
    Visite create(@PathVariable(value="visiteId") String id, @RequestBody Visite v, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(v.getId_vis().equals(id)) {
                VisiteDAO visiteDAO = new VisiteDAO(connection);
                Visite vNew = visiteDAO.readWithId(id);
                if(vNew.getId_vis() == null) {
                    visiteDAO.create(v);
                    vNew = visiteDAO.readWithId(id);
                    connection.close();
                    return vNew;
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

    /* Update la visite dont l'id est donne dans le path */
    //Renvoyer une erreur 404 si l'identifiant de visite ne correspond pas à une visite dans la base.
    //Renvoyer une erreur 412 si l'identifiant de Visite dans l'URL n'est pas le même que celui de Visite dans le corp de la requête.
    @PutMapping("/{visiteId}") 
    Visite update(@PathVariable(value="visiteId") String id, @RequestBody Visite v, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(v.getId_vis().equals(id)) {
                VisiteDAO visiteDAO = new VisiteDAO(connection);
                Visite vNew = visiteDAO.readWithId(id);
                if(vNew.getId_vis() == null) {
                    throw new Exception("ERROR404");
                } else {
                    visiteDAO.update(v);
                    vNew = visiteDAO.readWithId(id);
                    connection.close();
                    return vNew;
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
    
    /* Delete la visite dont l'id est donne dans le path */
    //Renvoyer une erreur 404 si l'identifiant de visite ne correspond pas à une visite dans la base.
    @DeleteMapping("/{visiteId}")
    void delete(@PathVariable(value="visiteId") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
                VisiteDAO visiteDAO = new VisiteDAO(connection);
                Visite vOld = visiteDAO.readWithId(id);
                if(vOld.getId_vis() == null) {
                    throw new Exception("ERROR404");
                } else {
                    visiteDAO.delete(vOld);
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
