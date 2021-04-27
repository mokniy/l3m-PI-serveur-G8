package classes;

public class Question {
    private String id_qst;
    private String label_qst;
    private String description_qst;
    private String secret_qst;
    private Integer points_qst;
    private String id_defi;


    public Question() {
    }
    


    public String getDescription_qst() {
        return this.description_qst;
    }

    public void setDescription_qst(String description_qst) {
        this.description_qst = description_qst;
    }

    public String getId_qst() {
        return this.id_qst;
    }

    public void setId_qst(String id_qst) {
        this.id_qst = id_qst;
    }

    public String getLabel_qst() {
        return this.label_qst;
    }

    public void setLabel_qst(String label_qst) {
        this.label_qst = label_qst;
    }

    public String getSecret_qst() {
        return this.secret_qst;
    }

    public void setSecret_qst(String secret_qst) {
        this.secret_qst = secret_qst;
    }

    public Integer getPoints_qst() {
        return this.points_qst;
    }

    public void setPoints_qst(Integer points_qst) {
        this.points_qst = points_qst;
    }

    public String getId_defi() {
        return this.id_defi;
    }

    public void setId_defi(String id_defi) {
        this.id_defi = id_defi;
    }

    
    
}

