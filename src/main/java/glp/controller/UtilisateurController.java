package glp.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import glp.domain.Annonce;
import glp.domain.Categorie;
import glp.domain.Job;
import glp.domain.Utilisateur;
import glp.services.AnnonceService;
import glp.services.CategorieService;
import glp.services.UtilisateurService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.sun.org.glassfish.external.statistics.annotations.Reset;

@Controller
@RequestMapping("/utilisateur")
public class UtilisateurController {

	@Autowired
	private UtilisateurService utilisateurService;
	@Autowired
	private CategorieService categorieService;

	@Autowired
	private AnnonceService annonceService;
	
	@Autowired
	HttpServletRequest request;

	// @RequestMapping("/")
	// public ModelAndView getIndex() {
	// return new ModelAndView("index");
	// }

	@RequestMapping("new")
	public ModelAndView getAnnForm(@ModelAttribute Utilisateur uti) {
		return new ModelAndView("uti_form");
	}

	@RequestMapping("/addUser")
	public ModelAndView addUser(@ModelAttribute("utilisateur") Utilisateur uti) {
		utilisateurService.insertRow(uti);
		return new ModelAndView("redirect:/");
	}

	@RequestMapping(value = "ModifCompte", method = RequestMethod.GET)
	public ModelAndView getFormulaireJob(@ModelAttribute Job job) {
		Utilisateur utilisateur = utilisateurService.getUserInSession();
		Utilisateur util = utilisateurService.getRowById(1);
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("utilisateurConnecte", util);
		List<Categorie> catList = categorieService.getList();

		myModel.put("catList", catList);
		return new ModelAndView("user_update", myModel);
	}

	@RequestMapping(value = "/updateMailUtilisateur", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	/* permet l'appel AJAX */
	public void updateMailUser(
			@ModelAttribute("utilisateur") Utilisateur utilisateur) {
		Utilisateur u = utilisateurService.getUserInSession();

		u.setMailAutre(utilisateur.getMailAutre());
		u.setContactAutreMail(utilisateur.isContactAutreMail());

		utilisateurService.updateRow(u);
	}

	@RequestMapping(value = "monCompte", method = RequestMethod.GET)
	public ModelAndView getAnnListValides() {
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("catList", categorieService.getList());
		
//		AttributePrincipal principal = (AttributePrincipal)request.getUserPrincipal();
//		Map attributes = principal.getAttributes();
//		Iterator attributeNames = attributes.keySet().iterator();
//		
//		new Utilisateur(prenom, nom, mailLille1)
//		utilisateurService.insertRow();
		//Utilisateur u= utilisateurService.getRowById(10);
		Utilisateur u = utilisateurService.getUserInSession();
		if (u != null) {
//			u = utilisateurService.getRowById(utilisateurService
//					.getUserInSession().getId());
			myModel.put("utilisateur", u);
			myModel.put("listeAnnoncesPubliees",
					utilisateurService.listAnnoncePublie(u));
			myModel.put("listeAnnoncesAModerer",
					utilisateurService.listAnnonceEnCourModeration(u));
			myModel.put("listeAnnoncesPerimees",
					utilisateurService.listAnnoncePerimees(u));
			myModel.put("duree_vie_ann", annonceService.getStats().getNb_jours_fin_annonce());
		}
		myModel.put("catList", categorieService.getList());
		return new ModelAndView("voirCompte", myModel);
	}

	@RequestMapping(value = "/SupprimerAnnonceUtilisateur", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void SupprimerAnnonceUtilisateur(HttpServletRequest request) {

		String action = request.getParameter("clickedbutton");
		String id = request.getParameter("idAnnonce");
System.out.println("l'action est :"+action);
		if (action.equals("changeaction")) {
			String datefin = request.getParameter("datefin");
			annonceService.updateDateAnnonce(Integer.parseInt(id), datefin);
		} else if (action.equals("deleteaction")) {
			annonceService.deleteRow(Integer.parseInt(request
					.getParameter("idAnnonce")));
		} else {
			System.out.println("erreur");
		}

		// Utilisateur util =utilisateurService.getRowById(1);
		// int identifiant = Integer.parseInt(idAnnonce);
		//
		// if(date_fin==null)
		// {
		// utilisateurService.supprimerAnnonce(identifiant);
		// }
		// else
		//
		// {
		// String date =date_fin;
		// System.out.println("la date "+ date);
		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		// Date d = null;
		//
		//
		//
		// try {
		// d = sdf.parse(date);
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// //System.out.println("La date "+d);
		//
		// // TODO Auto-generated catch block
		// //utilisateurService.updateDate(identifiant,d);
		// }
		//
		// List <Annonce> listePublie =
		// utilisateurService.listAnnoncePublie(util);
		// List <Annonce>
		// listeAmoderer=utilisateurService.listAnnonceEnCourModeration(util);
		// List<Categorie> catList = categorieService.getList();
		// Map<String, Object> myModel = new HashMap<String, Object>();
		// myModel.put("utilisateurConnecte", util);
		// myModel.put("catList", catList);
		// myModel.put("listePublie", listePublie);
		// myModel.put("listeAmoderer", listeAmoderer);
		//
		//
		// return new ModelAndView("voirCompte",myModel);

	}

	@RequestMapping(value = "update")
	/* @RequestParam(value="file", required = false) MultipartFile file */
	public ModelAndView addJob(Model model, @RequestParam String nom,
			@RequestParam String prenom, @RequestParam String tel,
			@RequestParam String mail, @RequestParam String mailautre) {
		// int idUtilisateur= utilisateurService.getUserInSession().getId();
		utilisateurService.updateUser(1, nom, prenom, tel, mail, mailautre);

		return new ModelAndView("redirect:/");
		// return new ModelAndView("redirect:/");
	}
}
