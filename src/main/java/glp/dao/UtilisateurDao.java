package glp.dao;

import glp.domain.Annonce;
import glp.domain.Utilisateur;

import java.util.Date;
import java.util.List;

public interface UtilisateurDao {
	
	public Integer insertRow(Utilisateur utilisateur);	
	
	public List<Utilisateur> getList();
	
	public Utilisateur getRowById(Integer id);
	
	public Integer updateRow(Utilisateur utilisateur);
	
	public Integer deleteRow(Integer id);
	
	public void updateContactAutreMail(Utilisateur u, boolean contactAutreMail);
	
	public List<Utilisateur> getListByRole(String role);
	
	public boolean isModerateur(Utilisateur utilisateur);
	
	public boolean isAdministrateur(Utilisateur utilisateur);
	
	public List<Annonce> listAnnoncePublie(Utilisateur utilisateur);
	public List<Annonce> listAnnonceEnCourModeration(Utilisateur utilisateur);
	public List<Annonce> listAnnoncePerimees(Utilisateur utilisateur);
	public Utilisateur updateUser(int id,String nom, String prenom,String tel,String mail,String mailAutre);
	public void supprimerAnnonce (int id);

	public Utilisateur getRowByMailLille1(String mail);

	boolean isRepresentant(Utilisateur utilisateur);
	public void updateDate(int id,Date d);
	public int nbUtilisateur();



	
	
	

}
