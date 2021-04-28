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
import dao.QuestionDAO;
//import com.example.DbConnection;
//import com.example.RestServer;
import classes.Question;

@RestController
@CrossOrigin
@RequestMapping("/api/question") 
public class QuestionCRUD {

    @Autowired
    private DataSource dataSource;
    
    /* ---- Cherche tous les questions ---- */
    @GetMapping("/")
    ArrayList<Question> allQuestion(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            QuestionDAO questions = new QuestionDAO(connection);
            ArrayList<Question> L = questions.readAllQuestion();
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

    /* ---- Rechercher la liste de toutes les questions en fonction de l'id de defi de la base ---- */
    @GetMapping("/allquestion/{Id_defi}")
    ArrayList<Question> allQuestionWithId_defi(@PathVariable(value="Id_defi") String id,HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            QuestionDAO questionDAO = new QuestionDAO(connection);
            ArrayList<Question> L = questionDAO.readAllQuestionWithId_defi(id);
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

    /* ---- Cherche une question ---- */
    
    @GetMapping("/{Id_qst}")
    Question read(@PathVariable(value="Id_qst") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            QuestionDAO questionDAO = new QuestionDAO(connection);
            Question q = questionDAO.readWithId(id);
            connection.close();
            if(q.getId_qst().equals("null")) {
                throw new Exception("Question inexistante");
            } else {
                return q;
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
    

    /* ---- Créé une question ---- */
    
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant d'une question dans l'URL n'est pas le même que celui d'une question dans le corp de la requête.
    @PostMapping("/{Id_qst}")
    Question create(@PathVariable(value="Id_qst") String id, @RequestBody Question q, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(q.getId_qst().equals(id)) {
                QuestionDAO questionDAO = new QuestionDAO(connection);
                Question qNew = questionDAO.readWithId(id);
                if(qNew.getId_qst() == null) {
                    questionDAO.create(q);
                    qNew = questionDAO.readWithId(id);
                    connection.close();
                    return qNew;
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
    
    /* ---- Créé un élément SANS ID ---- */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant d'une question dans l'URL n'est pas le même que celui d'une question dans le corp de la requête.
    @PostMapping("/")
    Question createWithoutID(@RequestBody Question q, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            QuestionDAO questionDAO = new QuestionDAO(connection);
            while (questionDAO.QuestionExist(questionDAO.getCurrentIncrement()+1)) {
                questionDAO.getNext();
            }
            String current_id = "QST"+(questionDAO.getCurrentIncrement()+1);
            Question qNew = questionDAO.readWithId(current_id);
            if(qNew.getId_qst() == null) {
                questionDAO.createSansID(q);
                qNew = questionDAO.readWithId(current_id);
                connection.close();
                return qNew;
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

    /* ---- Créé une liste d'éléments SANS ID ---- */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant d'une question dans l'URL n'est pas le même que celui d'une question dans le corp de la requête.
    @PostMapping("/list")
    ArrayList<Question> createListWithoutID(@RequestBody ArrayList<Question> q, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            QuestionDAO questionDAO = new QuestionDAO(connection);
            ArrayList<Question> L = new ArrayList<Question>();
            for (Question question : q) {
                while (questionDAO.QuestionExist(questionDAO.getCurrentIncrement()+1)) {
                    questionDAO.getNext();
                }
                String current_id = "QST"+(questionDAO.getCurrentIncrement()+1);
                Question qNew = questionDAO.readWithId(current_id);
                if(qNew.getId_qst() == null) {
                    questionDAO.createSansID(question);
                    qNew = questionDAO.readWithId(current_id);
                    L.add(qNew);
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

    /* ---- Modifie un élément ---- */
    //Renvoyer une erreur 404 si l'identifiant d'une question ne correspond pas à une question dans la base.
    //Renvoyer une erreur 412 si l'identifiant d'une question dans l'URL n'est pas le même que celui d'une question dans le corp de la requête.
    @PutMapping("/{Id_qst}") 
    Question update(@PathVariable(value="Id_qst") String id, @RequestBody Question q, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(q.getId_qst().equals(id)) {
                QuestionDAO questionDAO = new QuestionDAO(connection);
                Question qNew = questionDAO.readWithId(id);
                if(qNew.getId_qst() == null) {
                    throw new Exception("ERROR404");
                } else {
                    questionDAO.update(q);
                    qNew = questionDAO.readWithId(id);
                    connection.close();
                    return qNew;
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

    /* ---- Supprime un élément ---- */
    //Renvoyer une erreur 404 si l'identifiant d'une question ne correspond pas à une question dans la base.
    @DeleteMapping("/{Id_qst}")
    void delete(@PathVariable(value="Id_qst") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            QuestionDAO questionDAO = new QuestionDAO(connection);
            Question qOld = questionDAO.readWithId(id);
            if(qOld.getId_qst() == null) {
                throw new Exception("ERROR404");
            } else {
                questionDAO.delete(qOld);
                connection.close();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if(e.getMessage().equals("ERROR404")) {
                response.setStatus(404);
            }
        }

    }


    /* ---- Supprime la liste de questions en fonction de id_defi ---- */
    //Renvoyer une erreur 404 si l'identifiant d'une question ne correspond pas à une question dans la base.
    @DeleteMapping("/deleteallquestion/{Id_defi}")
    void deleteAllQuestionWithId_defi(@PathVariable(value="Id_defi") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            QuestionDAO questionDAO = new QuestionDAO(connection);
            ArrayList<Question> L = questionDAO.readAllQuestion();
            int found = 0;
            for (Question question : L) {
                if(question.getId_defi().equalsIgnoreCase(id))  {
                    found = 1;
                }
            }
            if (found == 0){
                throw new Exception("ERROR404");
            }
            else{
                questionDAO.deleteWithId_defi(id);
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
