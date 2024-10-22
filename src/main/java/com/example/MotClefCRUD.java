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
import dao.MotClefDAO;
//import com.example.DbConnection;
//import com.example.RestServer;
import classes.MotClef;
import classes.Chercher;
import dao.ChercherDAO;

@RestController
@CrossOrigin
@RequestMapping("/api/motclef") 
public class MotClefCRUD {

    @Autowired
    private DataSource dataSource;
    
    /* ---- Cherche tous les mot_clef ---- */
    @GetMapping("/")
    ArrayList<MotClef> allMotClefs(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            MotClefDAO motClefs = new MotClefDAO(connection);
            ArrayList<MotClef> L = motClefs.readAllMotClefs();
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

    
    /* ---- Cherche un mot_clef ---- */
    
    @GetMapping("/{motclefId}")
    MotClef read(@PathVariable(value="motclefId") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            MotClefDAO motClefsDAO = new MotClefDAO(connection);
            MotClef mc = motClefsDAO.readWithId(id);
            connection.close();
            if(mc.getId_mc().equals("null")) {
                throw new Exception("Mot Clef inexistant");
            } else {
                return mc;
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
    

    /* ---- Créé un mot_clef ---- */
    
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant du Mot Clef dans l'URL n'est pas le même que celui du Mot Clef dans le corp de la requête.
    @PostMapping("/{motclefId}")
    MotClef create(@PathVariable(value="motclefId") String id, @RequestBody MotClef mc, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(mc.getId_mc().equals(id)) {
                MotClefDAO motClefsDAO = new MotClefDAO(connection);
                MotClef mcNew = motClefsDAO.readWithId(id);
                if(mcNew.getId_mc() == null) {
                    motClefsDAO.create(mc);
                    mcNew = motClefsDAO.readWithId(id);
                    connection.close();
                    return mcNew;
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
    
    /* ---- Créé un mot_clef SANS ID ---- */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant du Mot Clef dans l'URL n'est pas le même que celui du Mot Clef dans le corp de la requête.
    @PostMapping("/")
    MotClef createWithoutID(@RequestBody MotClef mc, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            MotClefDAO motClefsDAO = new MotClefDAO(connection);
            while (motClefsDAO.motClefExist(motClefsDAO.getCurrentIncrement()+1)) {
                motClefsDAO.getNext();
            }
            String current_id = "MC"+(motClefsDAO.getCurrentIncrement()+1);
            MotClef mcNew = motClefsDAO.readWithId(current_id);
            if(mcNew.getId_mc() == null) {
                motClefsDAO.createSansID(mc);
                mcNew = motClefsDAO.readWithId(current_id);
                connection.close();
                return mcNew;
            } else {
                throw new Exception("ERROR403");
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

    /* ---- Créé une liste de mot_clef SANS ID ---- */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant du Mot Clef dans l'URL n'est pas le même que celui du Mot Clef dans le corp de la requête.
    @PostMapping("/list")
    ArrayList<MotClef> createListWithoutID(@RequestBody ArrayList<MotClef> mc, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            MotClefDAO motClefsDAO = new MotClefDAO(connection);
            ArrayList<MotClef> L = new ArrayList<MotClef>();
            for (MotClef motClef : mc) {
                while (motClefsDAO.motClefExist(motClefsDAO.getCurrentIncrement()+1)) {
                    motClefsDAO.getNext();
                }
                String current_id = "MC"+(motClefsDAO.getCurrentIncrement()+1);
                MotClef mcNew = motClefsDAO.readWithId(current_id);
                if(mcNew.getId_mc() == null) {
                    motClefsDAO.createSansID(motClef);
                    mcNew = motClefsDAO.readWithId(current_id);
                    L.add(mcNew);
                } else {
                    throw new Exception("ERROR403");
                }
            }
            connection.close();
            return L;
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

    /* ---- Modifie un mot_clef ---- */
    //Renvoyer une erreur 404 si l'identifiant du Mot clef ne correspond pas à un Mot clef dans la base.
    //Renvoyer une erreur 412 si l'identifiant du Mot clef dans l'URL n'est pas le même que celui du Mot clef dans le corp de la requête.
    @PutMapping("/{motclefId}") 
    MotClef update(@PathVariable(value="motclefId") String id, @RequestBody MotClef mc, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(mc.getId_mc().equals(id)) {
                MotClefDAO motClefsDAO = new MotClefDAO(connection);
                MotClef mcNew = motClefsDAO.readWithId(id);
                if(mcNew.getId_mc() == null) {
                    throw new Exception("ERROR404");
                } else {
                    motClefsDAO.update(mc);
                    mcNew = motClefsDAO.readWithId(id);
                    connection.close();
                    return mcNew;
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

    /* ---- Supprime un mot_clef ---- */
    //Renvoyer une erreur 404 si l'identifiant du Mot clef ne correspond pas à un Mot clef dans la base.
    @DeleteMapping("/{motclefId}")
    void delete(@PathVariable(value="motclefId") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            MotClefDAO motClefsDAO = new MotClefDAO(connection);
            MotClef mcOld = motClefsDAO.readWithId(id);
            if(mcOld.getId_mc() == null) {
                throw new Exception("ERROR404");
            } else {
                motClefsDAO.delete(mcOld);
                connection.close();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if(e.getMessage().equals("ERROR404")) {
                response.setStatus(404);
            }
        }

    }

    /* Supprime tous les mot_clef en fonction de l'id_defi */
    //Renvoyer une erreur 404 si l'identifiant d'une question ne correspond pas à une question dans la base.
    @DeleteMapping("/deleteallmotclef/{Id_defi}")
    void deleteAllMotClefWithId_defi(@PathVariable(value="Id_defi") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ChercherDAO chercherDAO = new ChercherDAO(connection);
            MotClefDAO motclefDAO = new MotClefDAO(connection);
            ArrayList<MotClef> L = chercherDAO.readAllMcIdWithId_defi(id);
            int found = 0;
            if(L.size() > 0 ) {
                found = 1;
            }

            if (found == 0){
                throw new Exception("ERROR404");
            }
            else{
                for(MotClef m : L) {
                    motclefDAO.delete(m);
                }
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
