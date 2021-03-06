package glp.controller;

import glp.domain.Annonce;
import glp.domain.Categorie;
import glp.domain.ChampComplete;
import glp.domain.Utilisateur;
import glp.form.AnnonceForm;
import glp.services.AnnonceService;
import glp.services.CategorieService;
import glp.services.ChampCompleteService;
import glp.services.ChampService;
import glp.services.RoleService;
import glp.services.UtilisateurService;
import glp.util.EmailSender;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.sun.mail.smtp.SMTPTransport;

@Controller
@RequestMapping(value = "/annonce")
public class AnnonceController {
	@Autowired
	private AnnonceService annonceService;
	@Autowired
	private CategorieService categorieService;
	@Autowired
	private UtilisateurService utilisateurService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private ChampCompleteService ccService;
	@Autowired
	private ChampService champService;

	public AnnonceController() {
	}

	/**
	 * pour la crï¿½ation d'une annonce 1er form qui demande d'abord la
	 * categorie
	 * 
	 * @return
	 */
	@RequestMapping(value = "new/categoriechoice", method = RequestMethod.GET)
	public ModelAndView getCategorieChoice() {
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("catList", categorieService.getList());
		myModel.put("annform", new AnnonceForm());
		myModel.put("champs", champService.getList());
		myModel.put("utilisateur", utilisateurService.getUserInSession());

		return new ModelAndView("ann_form_select_cat", myModel);
	}

	/**
	 * 
	 * @param annform
	 * @return le formulaire de crï¿½ation d'une annonce, aprï¿½s que l'on ait
	 *         choisi une catï¿½gorie
	 */
	@RequestMapping("new/form")
	public ModelAndView getForm(@ModelAttribute("annform") AnnonceForm annform) {

		// si on arrive sur le formulaire sans passer par le choix de
		// catï¿½gorie
		// --> redirige
		if (annform.getCat_choisie() == null)
			return getCategorieChoice();

		else {
			annform.setAnnonce(new Annonce());
			annform.getAnnonce().setCategorie(annform.getCat_choisie());
			annform = ccService.generateForAnnForm(annform);

			Map<String, Object> myModel = new HashMap<String, Object>();
			myModel.put("catList", categorieService.getList());
			myModel.put("annform", annform);
			myModel.put("utilisateur", utilisateurService.getUserInSession());
			myModel.put("duree_vie_ann", annonceService.getStats()
					.getNb_jours_fin_annonce());

			return new ModelAndView("ann_form", myModel);
		}
	}

	@RequestMapping(value = "typechoice", method = RequestMethod.GET)
	public ModelAndView getTypeChoix() {

		List<Categorie> catList = categorieService.getList();
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("utilisateur", utilisateurService.getUserInSession());
		myModel.put("catList", catList);
		return new ModelAndView("ann_type_choix", myModel);

	}

	@RequestMapping(value = "/new/updateMailUtilisateur", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	/* permet l'appel AJAX */
	public void updateMailUser(
			@ModelAttribute("utilisateur") Utilisateur utilisateur) {
		Utilisateur u = utilisateurService.getUserInSession();

		u.setMailAutre(utilisateur.getMailAutre());
		u.setContactAutreMail(utilisateur.isContactAutreMail());

		utilisateurService.updateRow(u);
	}

	@RequestMapping(value = "new/addAnn", method = RequestMethod.GET)
	public ModelAndView addAnnonce(
			@ModelAttribute("annform")/* @Valid */AnnonceForm annform/*
																	 * ,
																	 * BindingResult
																	 * bindingResult
																	 */) {

		Annonce ann_tmp = annform.getAnnonce();

		ann_tmp.setAuteur(utilisateurService.getUserInSession());
		annonceService.insertRow(ann_tmp);
		if (ann_tmp.getChampscompletes() != null)
			for (ChampComplete cc : ann_tmp.getChampscompletes()) {
				cc.setAnn(ann_tmp);
				ccService.insertRow(cc);
			}
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("annonceTitre", annform.getAnnonce().getTitre());
		myModel.put("annonceCreee", true);
		return new ModelAndView("redirect:/", myModel);
	}


	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView getAnnList() {
		List<Annonce> annList = annonceService.getListAnnoncesProposees();
		annList.addAll(annonceService.getListAnnoncesDemandees());
		List<Categorie> catList = categorieService.getList();
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("annList", annList);
		myModel.put("catList", catList);
		myModel.put("utilisateur", utilisateurService.getUserInSession());
		return new ModelAndView("ann_list", myModel);
	}

	@RequestMapping(value = "listCov", method = RequestMethod.GET)
	public ModelAndView getCovList() {
		List<Annonce> covList = annonceService.getListByCat(categorieService
				.getIdByLib("Covoiturage"));
		List<Categorie> catList = categorieService.getList();
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("annList", covList);
		myModel.put("catList", catList);
		myModel.put("utilisateur", utilisateurService.getUserInSession());
		return new ModelAndView("ann_list", myModel);
	}

	@RequestMapping("{id}")
	public ModelAndView getAnnonce(@PathVariable("id") int idAnnSelected) {
		
		Annonce annonce = annonceService.getRowById(idAnnSelected);
		List<Categorie> catList = categorieService.getList();
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("annonce", annonce);
		myModel.put("catList", catList);
		myModel.put("champscompletes", ccService.getListByAnn(annonce));
		myModel.put("utilisateur", utilisateurService.getUserInSession());
		return new ModelAndView("consultAnn", myModel);
	}

	@RequestMapping(value = "recherche", method = RequestMethod.GET)
	public ModelAndView getAnnListMot(@RequestParam String cat,
			@RequestParam String motCle) {
		List<Annonce> annList = annonceService.getListByCatEtMot(cat, motCle);
		List<Categorie> catList = categorieService.getList();
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("annList", annList);
		myModel.put("catList", catList);
		myModel.put("utilisateur", utilisateurService.getUserInSession());
		return new ModelAndView("ann_list", myModel);
	}

	@RequestMapping("/{id}/contact")
	public ModelAndView getContactForm() {
		return new ModelAndView("contact_form");
	}

	@RequestMapping(value = "/{id}/sendmail", method = RequestMethod.POST)
	public ModelAndView sendMail(@PathVariable("id") int idAnnSelected,
			HttpServletRequest request) {
		Annonce annonce = annonceService.getRowById(idAnnSelected);
		List<Categorie> catList = categorieService.getList();
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("annonce", annonce);
		myModel.put("catList", catList);
		myModel.put("champscompletes", ccService.getListByAnn(annonce));
		myModel.put("roleList", roleService.getList());
		myModel.put("utilisateur", utilisateurService.getUserInSession());
		myModel.put("confirmMail", "mailsent");
		
		//envoie à l'email renseigné, sinon à l'email lille 1
		String mailto = annonce.getAuteur().getMailLille1();
		if(annonce.getAuteur().getMailAutre()!=null && annonce.getAuteur().getMailAutre()!="")
			mailto = annonce.getAuteur().getMailAutre();
		
		if (EmailSender.sendMail(mailto,
				utilisateurService.getUserInSession().getMailLille1(),
				request.getParameter("contentMail"), annonce) == true)
			myModel.put("confirmMail", "mailsent");
		else
			myModel.put("confirmMail", "mailNotsent");
		return new ModelAndView("consultAnn", myModel);
	}

	@RequestMapping(value = "/propose/list", method = RequestMethod.GET)
	public ModelAndView getAnnListPropose() {
		List<Annonce> annList = annonceService.getListAnnoncesProposees();
		List<Categorie> catList = categorieService.getList();
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("annList", annList);
		myModel.put("catList", catList);
		myModel.put("utilisateur", utilisateurService.getUserInSession());
		return new ModelAndView("ann_list", myModel);
	}

	@RequestMapping(value = "/demande/list", method = RequestMethod.GET)
	public ModelAndView getAnnListDemande() {
		List<Annonce> annList = annonceService.getListAnnoncesDemandees();
		List<Categorie> catList = categorieService.getList();
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("annList", annList);
		myModel.put("catList", catList);
		myModel.put("utilisateur", utilisateurService.getUserInSession());
		return new ModelAndView("ann_list", myModel);
	}

	@RequestMapping(value = "/propose/listCov", method = RequestMethod.GET)
	public ModelAndView getCovListPropose() {
		List<Annonce> annList = annonceService.getListCovProposes();
		List<Categorie> catList = categorieService.getList();
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("annList", annList);
		myModel.put("catList", catList);
		myModel.put("utilisateur", utilisateurService.getUserInSession());
		return new ModelAndView("ann_list", myModel);
	}

	@RequestMapping(value = "/demande/listCov", method = RequestMethod.GET)
	public ModelAndView getCovListDemande() {
		List<Annonce> annList = annonceService.getListCovDemandes();
		List<Categorie> catList = categorieService.getList();
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("annList", annList);
		myModel.put("catList", catList);
		myModel.put("utilisateur", utilisateurService.getUserInSession());
		return new ModelAndView("ann_list", myModel);
	}

}
