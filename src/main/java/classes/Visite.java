package classes;

public class Visite {
    private String id_vis;
    private String date_vis;
    private String mode_vis;
    private String statut_vis;
    private Integer pts_vis;
    private Integer score_vis;
    private String temps_vis;
    private String id_visiteur;
    private String id_defi;
    private String commentaire;

    public Visite(){
        
    }

	public String getId_vis() {
		return this.id_vis;
	}

	public void setId_vis(String id_vis) {
		this.id_vis = id_vis;
	}


	public String getDate_vis() {
		return this.date_vis;
	}

	public void setDate_vis(String date_vis) {
		this.date_vis = date_vis;
	}

	public String getMode_vis() {
		return this.mode_vis;
	}

	public void setMode_vis(String mode_vis) {
		this.mode_vis = mode_vis;
	}

	public String getStatut_vis() {
		return this.statut_vis;
	}

	public void setStatut_vis(String statut_vis) {
		this.statut_vis = statut_vis;
	}

	public Integer getPts_vis() {
		return this.pts_vis;
	}

	public void setPts_vis(Integer pts_vis) {
		this.pts_vis = pts_vis;
	}

	public Integer getScore_vis() {
		return this.score_vis;
	}

	public void setScore_vis(Integer score_vis) {
		this.score_vis = score_vis;
	}

	public String getTemps_vis() {
		return this.temps_vis;
	}

	public void setTemps_vis(String temps_vis) {
		this.temps_vis = temps_vis;
	}

	public String getId_visiteur() {
		return this.id_visiteur;
	}

	public void setId_visiteur(String id_visiteur) {
		this.id_visiteur = id_visiteur;
	}

	public String getId_defi() {
		return this.id_defi;
	}

	public void setId_defi(String id_defi) {
		this.id_defi = id_defi;
	}

	public String getCommentaire() {
		return this.commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

}
