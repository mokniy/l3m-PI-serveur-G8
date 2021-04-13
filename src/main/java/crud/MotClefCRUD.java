package crud;

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
import com.example.DbConnection;
import com.example.RestServer;
import classes.MotClef;

@RestController
@CrossOrigin
@RequestMapping("/api/mot_clef") 
public class MotClefCRUD {

    @Autowired
    private DataSource dataSource;
    
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

    @GetMapping("/{mot_clefId}")
    MotClef read(@PathVariable(value="mot_clefId") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            MotClefDAO motClefsDAO = new MotClefDAO(connection);
            MotClef mc = motClefsDAO.readWithId(id);
            connection.close();
            if(mc.id_mc.equals("null")) {
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

    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant du Mot Clef dans l'URL n'est pas le même que celui du Mot Clef dans le corp de la requête.
    @PostMapping("/{mot_clefId}")
    MotClef create(@PathVariable(value="mot_clefId") String id, @RequestBody MotClef mc, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(mc.id_mc.equals(id)) {
                MotClefDAO motClefsDAO = new MotClefDAO(connection);
                MotClef mcNew = motClefsDAO.readWithId(id);
                if(mcNew.id_mc == null) {
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

    //Renvoyer une erreur 404 si l'identifiant de l'utilisateur ne correspond pas à un utilisateur dans la base.
    //Renvoyer une erreur 412 si l'identifiant du Defis dans l'URL n'est pas le même que celui du Defis dans le corp de la requête.
    @PutMapping("/{mot_clefId}") 
    MotClef update(@PathVariable(value="mot_clefId") String id, @RequestBody MotClef mc, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(mc.id_mc.equals(id)) {
                MotClefDAO motClefsDAO = new MotClefDAO(connection);
                MotClef mcNew = motClefsDAO.readWithId(id);
                if(mcNew.id_mc == null) {
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

    //Renvoyer une erreur 404 si l'identifiant de l'utilisateur ne correspond pas à un utilisateur dans la base.
    @DeleteMapping("/{mot_clefId}")
    void delete(@PathVariable(value="mot_clefId") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            MotClefDAO motClefsDAO = new MotClefDAO(connection);
            MotClef mcOld = motClefsDAO.readWithId(id);
            if(mcOld.id_mc == null) {
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
}
