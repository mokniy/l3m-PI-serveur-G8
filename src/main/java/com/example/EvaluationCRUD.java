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
import dao.EvaluationDAO;
//import com.example.DbConnection;
//import com.example.RestServer;
import classes.Evaluation;

@RestController
@CrossOrigin
@RequestMapping("/api/evaluation") 
public class EvaluationCRUD {

    @Autowired
    private DataSource dataSource;
    
    /* ---- Cherche tous les Evaluations ---- */
    @GetMapping("/")
    ArrayList<Evaluation> allEvaluation(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            EvaluationDAO evaluationDAO = new EvaluationDAO(connection);
            ArrayList<Evaluation> L = evaluationDAO.readAllEvaluation();
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


    /* ---- Cherche une Evaluation ---- */
    @GetMapping("/{Id_eval}")
    Evaluation read(@PathVariable(value="Id_eval") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            EvaluationDAO evaluationDAO = new EvaluationDAO(connection);
            Evaluation ev = evaluationDAO.readWithId(id);
            connection.close();
            if(ev.getId_eval().equals("null")) {
                throw new Exception("Evaluation inexistante");
            } else {
                return ev;
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
    

    /* ---- Créé une Evaluation ---- */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant d'une Evaluation dans l'URL n'est pas le même que celui d'une Evaluation dans le corp de la requête.
    @PostMapping("/{Id_eval}")
    Evaluation create(@PathVariable(value="Id_eval") String id, @RequestBody Evaluation ev, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(ev.getId_eval().equals(id)) {
                EvaluationDAO evaluationDAO = new EvaluationDAO(connection);
                Evaluation evNew = evaluationDAO.readWithId(id);
                if(evNew.getId_eval() == null) {
                    evaluationDAO.create(ev);
                    evNew = evaluationDAO.readWithId(id);
                    connection.close();
                    return evNew;
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
    
    /* ---- Créé une Evaluation SANS ID ---- */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant d'une Evaluation dans l'URL n'est pas le même que celui d'une Evaluation dans le corp de la requête.
    @PostMapping("/")
    Evaluation createWithoutID(@RequestBody Evaluation ev, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            EvaluationDAO evaluationDAO = new EvaluationDAO(connection);
            while (evaluationDAO.EvaluationExist(evaluationDAO.getCurrentIncrement()+1)) {
                evaluationDAO.getNext();
            }
            String current_id = "EV"+(evaluationDAO.getCurrentIncrement()+1);
            Evaluation evNew = evaluationDAO.readWithId(current_id);
            if(evNew.getId_eval() == null) {
                evaluationDAO.createSansID(ev);
                evNew = evaluationDAO.readWithId(current_id);
                connection.close();
                return evNew;
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

    /* ---- Créé une liste de Evaluations SANS ID ---- */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant d'une Evaluation dans l'URL n'est pas le même que celui d'une Evaluation dans le corp de la requête.
    @PostMapping("/list")
    ArrayList<Evaluation> createListWithoutID(@RequestBody ArrayList<Evaluation> ev, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            EvaluationDAO evaluationDAO = new EvaluationDAO(connection);
            ArrayList<Evaluation> L = new ArrayList<Evaluation>();
            for (Evaluation evaluation : ev) {
                while (evaluationDAO.EvaluationExist(evaluationDAO.getCurrentIncrement()+1)) {
                    evaluationDAO.getNext();
                }
                String current_id = "EV"+(evaluationDAO.getCurrentIncrement()+1);
                Evaluation evNew = evaluationDAO.readWithId(current_id);
                if(evNew.getId_eval() == null) {
                    evaluationDAO.createSansID(evaluation);
                    evNew = evaluationDAO.readWithId(current_id);
                    L.add(evNew);
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

    /* ---- Modifie une Evaluation ---- */
    //Renvoyer une erreur 404 si l'identifiant d'une Evaluation ne correspond pas à une Evaluation dans la base.
    //Renvoyer une erreur 412 si l'identifiant d'une Evaluation dans l'URL n'est pas le même que celui d'une Evaluation dans le corp de la requête.
    @PutMapping("/{Id_eval}") 
    Evaluation update(@PathVariable(value="Id_eval") String id, @RequestBody Evaluation ev, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(ev.getId_eval().equals(id)) {
                EvaluationDAO evaluationDAO = new EvaluationDAO(connection);
                Evaluation evNew = evaluationDAO.readWithId(id);
                if(evNew.getId_eval() == null) {
                    throw new Exception("ERROR404");
                } else {
                    evaluationDAO.update(ev);
                    evNew = evaluationDAO.readWithId(id);
                    connection.close();
                    return evNew;
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

    /* ---- Supprime une Evaluation ---- */
    //Renvoyer une erreur 404 si l'identifiant d'une Evaluation ne correspond pas à une Evaluation dans la base.
    @DeleteMapping("/{Id_eval}")
    void delete(@PathVariable(value="Id_eval") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            EvaluationDAO evaluationDAO = new EvaluationDAO(connection);
            Evaluation evOld = evaluationDAO.readWithId(id);
            if(evOld.getId_rep() == null) {
                throw new Exception("ERROR404");
            } else {
                evaluationDAO.delete(evOld);
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
