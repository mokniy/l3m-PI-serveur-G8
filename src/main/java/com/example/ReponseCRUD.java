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
import dao.ReponseDAO;
//import com.example.DbConnection;
//import com.example.RestServer;
import classes.Reponse;

@RestController
@CrossOrigin
@RequestMapping("/api/reponse") 
public class ReponseCRUD {

    @Autowired
    private DataSource dataSource;
    
    /* ---- Cherche tous les Reponses ---- */
    @GetMapping("/")
    ArrayList<Reponse> allReponse(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ReponseDAO reponseDAO = new ReponseDAO(connection);
            ArrayList<Reponse> L = reponseDAO.readAllReponse();
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

    /* Cherche toutes les Reponses en fonction de l'id_qst */
    /* ---- Rechercher la liste de toutes les Reponses en fonction de l'id de defi de la base ---- */
    @GetMapping("/allReponse/{Id_qst}")
    ArrayList<Reponse> allReponseWithId_defi(@PathVariable(value="Id_qst") String id,HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ReponseDAO reponseDAO = new ReponseDAO(connection);
            ArrayList<Reponse> L = reponseDAO.readAllReponseWithId_qst(id);
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

    /* ---- Cherche une Reponse ---- */
    @GetMapping("/{Id_rep}")
    Reponse read(@PathVariable(value="Id_rep") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ReponseDAO reponseDAO = new ReponseDAO(connection);
            Reponse r = reponseDAO.readWithId(id);
            connection.close();
            if(r.getId_rep().equals("null")) {
                throw new Exception("Reponse inexistante");
            } else {
                return r;
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
    

    /* ---- Créé une Reponse ---- */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant d'une Reponse dans l'URL n'est pas le même que celui d'une Reponse dans le corp de la requête.
    @PostMapping("/{Id_rep}")
    Reponse create(@PathVariable(value="Id_rep") String id, @RequestBody Reponse r, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(r.getId_rep().equals(id)) {
                ReponseDAO reponseDAO = new ReponseDAO(connection);
                Reponse rNew = reponseDAO.readWithId(id);
                if(rNew.getId_qst() == null) {
                    reponseDAO.create(r);
                    rNew = reponseDAO.readWithId(id);
                    connection.close();
                    return rNew;
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
    
    /* ---- Créé une Reponse SANS ID ---- */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant d'une Reponse dans l'URL n'est pas le même que celui d'une Reponse dans le corp de la requête.
    @PostMapping("/")
    Reponse createWithoutID(@RequestBody Reponse r, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ReponseDAO reponseDAO = new ReponseDAO(connection);
            while (reponseDAO.ReponseExist(reponseDAO.getCurrentIncrement()+1)) {
                reponseDAO.getNext();
            }
            String current_id = "REP"+(reponseDAO.getCurrentIncrement()+1);
            Reponse rNew = reponseDAO.readWithId(current_id);
            if(rNew.getId_rep() == null) {
                reponseDAO.createSansID(r);
                rNew = reponseDAO.readWithId(current_id);
                connection.close();
                return rNew;
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

    /* ---- Créé une liste de Reponses SANS ID ---- */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant d'une Reponse dans l'URL n'est pas le même que celui d'une Reponse dans le corp de la requête.
    @PostMapping("/list")
    ArrayList<Reponse> createListWithoutID(@RequestBody ArrayList<Reponse> r, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ReponseDAO reponseDAO = new ReponseDAO(connection);
            ArrayList<Reponse> L = new ArrayList<Reponse>();
            for (Reponse reponse : r) {
                while (reponseDAO.ReponseExist(reponseDAO.getCurrentIncrement()+1)) {
                    reponseDAO.getNext();
                }
                String current_id = "REP"+(reponseDAO.getCurrentIncrement()+1);
                Reponse rNew = reponseDAO.readWithId(current_id);
                if(rNew.getId_rep() == null) {
                    reponseDAO.createSansID(reponse);
                    rNew = reponseDAO.readWithId(current_id);
                    L.add(rNew);
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

    /* ---- Modifie une Reponse ---- */
    //Renvoyer une erreur 404 si l'identifiant d'une Reponse ne correspond pas à une Reponse dans la base.
    //Renvoyer une erreur 412 si l'identifiant d'une Reponse dans l'URL n'est pas le même que celui d'une Reponse dans le corp de la requête.
    @PutMapping("/{Id_rep}") 
    Reponse update(@PathVariable(value="Id_rep") String id, @RequestBody Reponse r, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(r.getId_rep().equals(id)) {
                ReponseDAO reponseDAO = new ReponseDAO(connection);
                Reponse rNew = reponseDAO.readWithId(id);
                if(rNew.getId_rep() == null) {
                    throw new Exception("ERROR404");
                } else {
                    reponseDAO.update(r);
                    rNew = reponseDAO.readWithId(id);
                    connection.close();
                    return rNew;
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

    /* ---- Supprime une Reponse ---- */
    //Renvoyer une erreur 404 si l'identifiant d'une Reponse ne correspond pas à une Reponse dans la base.
    @DeleteMapping("/{Id_rep}")
    void delete(@PathVariable(value="Id_rep") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ReponseDAO reponseDAO = new ReponseDAO(connection);
            Reponse rOld = reponseDAO.readWithId(id);
            if(rOld.getId_rep() == null) {
                throw new Exception("ERROR404");
            } else {
                reponseDAO.delete(rOld);
                connection.close();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if(e.getMessage().equals("ERROR404")) {
                response.setStatus(404);
            }
        }

    }


    /* ---- Supprime la liste de Reponses en fonction de id_defi ---- */
    //Renvoyer une erreur 404 si l'identifiant d'une Reponse ne correspond pas à une Reponse dans la base.
    @DeleteMapping("/deleteallReponse/{Id_qst}")
    void deleteAllReponseWithId_qst(@PathVariable(value="Id_qst") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            ReponseDAO reponseDAO = new ReponseDAO(connection);
            ArrayList<Reponse> L = reponseDAO.readAllReponse();
            int found = 0;
            for (Reponse reponse : L) {
                if(reponse.getId_qst().equalsIgnoreCase(id))  {
                    found = 1;
                }
            }
            if (found == 0){
                throw new Exception("ERROR404");
            }
            else{
                reponseDAO.deleteWithId_qst(id);
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
