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
import dao.IndiceDAO;
//import com.example.DbConnection;
//import com.example.RestServer;
import classes.Indice;

@RestController
@CrossOrigin
@RequestMapping("/api/indice") 
public class IndiceCRUD {

    @Autowired
    private DataSource dataSource;
    
    /* Chercher tous les indices de la base */
    @GetMapping("/")
    ArrayList<Indice> allIndice(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            IndiceDAO indice = new IndiceDAO(connection);
            ArrayList<Indice> L = indice.readAllIndice();
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

    /* ---- Rechercher la liste de tous les indices en fonction de l'id de defi de la base ---- */
    @GetMapping("/allindice/{Id_defi}")
    ArrayList<Indice> allIndiceWithId_defi(@PathVariable(value="Id_defi") String id,HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            IndiceDAO indiceDAO = new IndiceDAO(connection);
            ArrayList<Indice> L = indiceDAO.readAllIndiceWithId_defi(id);
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

    /* Chercher l'indice dont l'id est donne dans le path */
    @GetMapping("/{idInd}")
    Indice read(@PathVariable(value="idInd") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            IndiceDAO indiceDAO = new IndiceDAO(connection);
            Indice i = indiceDAO.readWithId(id);
            connection.close();
            if(i.getId_ind().equals("null")) {
                throw new Exception("Indice inexistant");
            } else {
                return i;
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

    /* Creer un Indice */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant de l'indice dans l'URL n'est pas le même que celui de l'indice dans le corps de la requête.
    @PostMapping("/{idInd}")
    Indice create(@PathVariable(value="idInd") String id, @RequestBody Indice i, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(i.getId_ind().equals(id)) {
                IndiceDAO indiceDAO = new IndiceDAO(connection);
                Indice iNew = indiceDAO.readWithId(id);
                if(iNew.getId_ind() == null) {
                    indiceDAO.create(i);
                    iNew = indiceDAO.readWithId(id);
                    connection.close();
                    return iNew;
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

    /* ---- Créé un indice SANS ID ---- */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant du indice dans l'URL n'est pas le même que celui du indice dans le corp de la requête.
    @PostMapping("/")
    Indice createWithoutId(@RequestBody Indice i, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            IndiceDAO indiceDAO = new IndiceDAO(connection);
            while (indiceDAO.indiceExist(indiceDAO.getCurrentIncrement()+1)) {
                indiceDAO.getNext();
            }
            String current_id = "IND"+(indiceDAO.getCurrentIncrement()+1);
            Indice iNew = indiceDAO.readWithId(current_id);
            if(iNew.getId_ind() == null) {
                indiceDAO.createSansID(i);
                iNew = indiceDAO.readWithId(current_id);
                connection.close();
                return iNew;
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

    /* ---- Créé une liste d'indices SANS ID ---- */
    //Renvoyez une erreur 403 si une ressource existe déjà avec le même identifiant.
    //Renvoyer une erreur 412 si l'identifiant d'un indice dans l'URL n'est pas le même que celui d'un indice dans le corp de la requête.
    @PostMapping("/list")
    ArrayList<Indice> createListWithoutID(@RequestBody ArrayList<Indice> i, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            IndiceDAO indiceDAO = new IndiceDAO(connection);
            ArrayList<Indice> L = new ArrayList<Indice>();
            for (Indice indice : i) {
                while (indiceDAO.indiceExist(indiceDAO.getCurrentIncrement()+1)) {
                    indiceDAO.getNext();
                }
                String current_id = "IND"+(indiceDAO.getCurrentIncrement()+1);
                Indice iNew = indiceDAO.readWithId(current_id);
                if(iNew.getId_ind() == null) {
                    indiceDAO.createSansID(indice);
                    iNew = indiceDAO.readWithId(current_id);
                    L.add(iNew);
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

    /* Update l'indice dont l'id est donne dans le path */
    //Renvoyer une erreur 404 si l'identifiant du indice ne correspond pas à un indice dans la base.
    //Renvoyer une erreur 412 si l'identifiant du indice dans l'URL n'est pas le même que celui du indice dans le corps de la requête.
    @PutMapping("/{indiceId}") 
    Indice update(@PathVariable(value="indiceId") String id, @RequestBody Indice i, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            if(i.getId_ind().equals(id)) {
                IndiceDAO indiceDAO = new IndiceDAO(connection);
                Indice iNew = indiceDAO.readWithId(id);
                if(iNew.getId_ind() == null) {
                    throw new Exception("ERROR404");
                } else {
                    indiceDAO.update(i);
                    iNew = indiceDAO.readWithId(id);
                    connection.close();
                    return iNew;
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

    /* Delete l'indice dont l'id est donne dans le path */
    //Renvoyer une erreur 404 si l'identifiant du indice ne correspond pas à un indice dans la base.
    @DeleteMapping("/{indiceId}")
    void delete(@PathVariable(value="indiceId") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
                IndiceDAO indiceDAO = new IndiceDAO(connection);
                Indice iOld = indiceDAO.readWithId(id);
                if(iOld.getId_ind() == null) {
                    throw new Exception("ERROR404");
                } else {
                    indiceDAO.delete(iOld);
                    connection.close();
                }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            if(e.getMessage().equals("ERROR404")) {
                response.setStatus(404);
            }
        }

    }

    /* ---- Supprime la liste d'indices en fonction de id_defi ---- */
    //Renvoyer une erreur 404 si l'identifiant d'une question ne correspond pas à une question dans la base.
    @DeleteMapping("/deleteallindice/{Id_defi}")
    void deleteAllIndiceWithId_defi(@PathVariable(value="Id_defi") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            IndiceDAO indiceDAO = new IndiceDAO(connection);
            ArrayList<Indice> L = indiceDAO.readAllIndice();
            int found = 0;
            for (Indice indice : L) {
                if(indice.getId_defi().equalsIgnoreCase(id))  {
                    found = 1;
                }
            }
            if (found == 0){
                throw new Exception("ERROR404");
            }
            else{
                indiceDAO.deleteWithId_defi(id);
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
