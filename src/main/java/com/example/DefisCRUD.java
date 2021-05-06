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
import dao.DefisDAO;
//import com.example.DbConnection;
//import com.example.RestServer;
import classes.Defis;

@RestController
@CrossOrigin
@RequestMapping("/api/defis") 
public class DefisCRUD {

    @Autowired
    private DataSource dataSource;
    
    /* Chercher tous les defis de la base */
    @GetMapping("/")
    ArrayList<Defis> allDefis(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            DefisDAO defis = new DefisDAO(connection);
            ArrayList<Defis> L = defis.readAllDefis();
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

    /* Chercher le defi dont l'id est donne dans le path */
    @GetMapping("/{defiId}")
    Defis read(@PathVariable(value="defiId") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            DefisDAO defisDAO = new DefisDAO(connection);
            Defis d = defisDAO.readWithId(id);
            connection.close();
            if(d.getDefi().equals("null")) {
                throw new Exception("Defi inexistant");
            } else {
                return d;
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

    /* Creer un defi */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant du Defi dans l'URL n'est pas le même que celui du Defi dans le corp de la requête.
    @PostMapping("/{defiId}")
    Defis create(@PathVariable(value="defiId") String id, @RequestBody Defis d, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(d.getDefi().equals(id)) {
                DefisDAO defisDAO = new DefisDAO(connection);
                Defis dNew = defisDAO.readWithId(id);
                if(dNew.getDefi() == null) {
                    defisDAO.create(d);
                    dNew = defisDAO.readWithId(id);
                    connection.close();
                    return dNew;
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

    /* ---- Créé un defi SANS ID ---- */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant du Mot Clef dans l'URL n'est pas le même que celui du Mot Clef dans le corp de la requête.
    @PostMapping("/")
    Defis createWithoutId(@RequestBody Defis d, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            DefisDAO defisDAO = new DefisDAO(connection);
            while (defisDAO.defiExist(defisDAO.getCurrentIncrement()+1)) {
                defisDAO.getNext();
            }
            String current_id = "D"+(defisDAO.getCurrentIncrement()+1);
            Defis dNew = defisDAO.readWithId(current_id);
            if(dNew.getDefi() == null) {
                defisDAO.createSansID(d);
                dNew = defisDAO.readWithId(current_id);
                connection.close();
                return dNew;
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

    /* Update le defi dont l'id est donne dans le path */
    //Renvoyer une erreur 404 si l'identifiant du defi ne correspond pas à un defi dans la base.
    //Renvoyer une erreur 412 si l'identifiant du defi dans l'URL n'est pas le même que celui du defi dans le corps de la requête.
    @PutMapping("/{defiId}") 
    Defis update(@PathVariable(value="defiId") String id, @RequestBody Defis d, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(d.getDefi().equals(id)) {
                DefisDAO defisDAO = new DefisDAO(connection);
                Defis dNew = defisDAO.readWithId(id);
                if(dNew.getDefi() == null) {
                    throw new Exception("ERROR404");
                } else {
                    defisDAO.update(d);
                    dNew = defisDAO.readWithId(id);
                    connection.close();
                    return dNew;
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

    /* Delete le defi dont l'id est donne dans le path */
    //Renvoyer une erreur 404 si l'identifiant du defi ne correspond pas à un defi dans la base.
    @DeleteMapping("/{defiId}")
    void delete(@PathVariable(value="defiId") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
                DefisDAO defisDAO = new DefisDAO(connection);
                Defis dOld = defisDAO.readWithId(id);
                if(dOld.getDefi() == null) {
                    throw new Exception("ERROR404");
                } else {
                    defisDAO.delete(dOld);
                    connection.close();
                }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if(e.getMessage().equals("ERROR404")) {
                response.setStatus(404);
            }
        }

    }

    /* Cherche tous les defis en fonction de l'id_auteur */
    @GetMapping("/auteur/{auteurId}")
    ArrayList<Defis> allDefisByAuteur(@PathVariable(value="auteurId") String auteur, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            DefisDAO defis = new DefisDAO(connection);
            ArrayList<Defis> L = defis.readAllDefisByAuteur(auteur);
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

    /* Cherche tous les defis en fonction du titre et du type */
    @GetMapping("/titre/{titre}&{type}")
    ArrayList<Defis> allDefisByTitreAndType(@PathVariable(value="titre") String titre,@PathVariable(value="type") String type, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            DefisDAO defis = new DefisDAO(connection);
            ArrayList<Defis> L = defis.readAllDefisByTitreAndType(titre,type);
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

    /* Chercher le defi dont l'id est donne dans le path */
    @GetMapping("/type/{defiId}&{type}")
    Defis read(@PathVariable(value="defiId") String id, @PathVariable(value="type") String type, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            DefisDAO defisDAO = new DefisDAO(connection);
            Defis d = defisDAO.readWithIdAndType(id,type);
            connection.close();
            if(d.getDefi().equals("null")) {
                throw new Exception("Defi inexistant");
            } else {
                return d;
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

    
    /* Chercher tous les defis de la base type enigme */
    @GetMapping("/enigme/")
    ArrayList<Defis> readAllDefisEnigme(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            DefisDAO defis = new DefisDAO(connection);
            ArrayList<Defis> L = defis.readAllDefisEnigme();
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

    
    
    /* Chercher tous les defis de la base type challenge */
    @GetMapping("/challenge")
    ArrayList<Defis> readAllDefisChallenge(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            DefisDAO defis = new DefisDAO(connection);
            ArrayList<Defis> L = defis.readAllDefisChallenge();
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
}
