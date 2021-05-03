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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dao.ChercherDAO;
import classes.Chercher;
import classes.Defis;
import classes.MotClef;

@RestController
@CrossOrigin
@RequestMapping("/api/chercher") 
public class ChercherCRUD {

    @Autowired
    private DataSource dataSource;
    
    /* ---- Rechercher toutes les données de la base ---- */
    @GetMapping("/")
    ArrayList<Chercher> allChercher(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ChercherDAO chercherDAO = new ChercherDAO(connection);
            ArrayList<Chercher> L = chercherDAO.readAllChercher();
            connection.close();
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

    /* ---- Rechercher la liste de tous les defis en fonction de id_mc de la base ---- */
    @GetMapping("/alldefis/{mc}")
    ArrayList<Defis> allDefis(@PathVariable(value="mc") String mc,HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ChercherDAO chercherDAO = new ChercherDAO(connection);
            ArrayList<Defis> L = chercherDAO.readAllDefiWithMot_mc(mc);
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

    /* ---- Rechercher la liste de tous les mots clefs en fonction de id_defi de la base ---- */
    @GetMapping("/allmc/{defiId}")
    ArrayList<MotClef> allMotClefs(@PathVariable(value="defiId") String id,HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ChercherDAO chercherDAO = new ChercherDAO(connection);
            ArrayList<MotClef> L = chercherDAO.readAllMcWithId_defi(id);
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
    
    /* ---- Rechercher la jonction d'un defi avec un mot_clef ---- */
    @GetMapping("/{defiId}&{mcId}")
    Chercher ChercherOne(@PathVariable(value="defiId") String id1,@PathVariable(value="mcId") String id2,HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ChercherDAO chercherDAO = new ChercherDAO(connection);
            Chercher L = chercherDAO.readWithTwoId(id1,id2);
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

    /* ---- Rechercher par defi, les différents mot_clef qu'il utilise ----*/
    @GetMapping("/withDefi/{defiId}")
    Chercher readDefiId(@PathVariable(value="defiId") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ChercherDAO chercherDAO = new ChercherDAO(connection);
            Chercher ch = chercherDAO.readWithId_defi(id);
            connection.close();
            if(ch.getId_defi().equals("null")) {
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

    /* ---- Rechercher par mot-clef, les différents defis qui l'utilise ----*/
    @GetMapping("/withMc/{mcId}")
    Chercher readMcId(@PathVariable(value="mcId") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ChercherDAO chercherDAO = new ChercherDAO(connection);
            Chercher ch = chercherDAO.readWithId_mc(id);
            connection.close();
            if(ch.getId_mc().equals("null")) {
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




    /* ---- Créer une donnée ---- */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant de chercher dans l'URL n'est pas le même que celui de chercher dans le corp de la requête.
    @PostMapping("/{defiId}&{mcId}")
    Chercher create(@PathVariable(value="defiId") String id1, @PathVariable(value="mcId") String id2, @RequestBody Chercher ch, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(ch.getId_defi().equals(id1)) {
                if(ch.getId_mc().equals(id2)){
                    ChercherDAO chercherDAO = new ChercherDAO(connection);
                    Chercher chNew = chercherDAO.readWithTwoId(id1,id2);
                    if(chNew.getId_defi() == null) {
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

    

    /* ---- Supprime une donnée ---- */
    //Renvoyer une erreur 404 si l'identifiant de chercher ne correspond pas à un chercher dans la base.
    @DeleteMapping("/{defiId}&{mcId}")
    void delete(@PathVariable(value="defiId") String id1, @PathVariable(value="mcId") String id2, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
                ChercherDAO chercherDAO = new ChercherDAO(connection);
                Chercher chOld = chercherDAO.readWithTwoId(id1,id2);
                if(chOld.getId_defi() == null) {
                    throw new Exception("ERROR404");
                } else {
                    if (chOld.getId_mc() == null){
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
