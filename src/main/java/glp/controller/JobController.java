package glp.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import glp.domain.Job;
import glp.domain.Utilisateur;
import glp.services.JobService;
import glp.services.UtilisateurService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/job")
public class JobController {


		@Autowired
		private JobService jobService;
		@Autowired
		private UtilisateurService utilisateurService;
		public JobController() {
		}
		
		@RequestMapping(value = "new", method = RequestMethod.GET)
		public ModelAndView getFormulaireJob(@ModelAttribute Job job) {
			Utilisateur utilisateur = utilisateurService.getUserInSession();
			Map<String, Object> myModel = new HashMap<String, Object>();
			myModel.put("utilisateur", utilisateur);
			return new ModelAndView("job_form", myModel);
		}
		
		
		@RequestMapping(value="addjob")/*@RequestParam(value="file", required = false) MultipartFile file*/
		public ModelAndView addJob(Model model ,@RequestParam String titre,
												@RequestParam String desc,
												@RequestParam Date date_fin,
												@RequestParam String prix) {
			Job job = new Job(titre, desc, date_fin, prix);
			job.setAuteur(utilisateurService.getUserInSession());
			jobService.insertRow(job);
			return new ModelAndView("redirect:/");
			//return new ModelAndView("redirect:/");
		}
		
		@RequestMapping(value = "list", method = RequestMethod.GET)
		public ModelAndView getJobList() {
			List<Job> jobList = jobService.getList();
			Map<String, Object> myModel = new HashMap<String, Object>();
			myModel.put("jobList", jobList);
			return new ModelAndView("job_list",myModel);	
		}
		
		@RequestMapping("{id}")
		public ModelAndView getJob(@PathVariable("id") int idJobSelected) {
			System.out.println("l'identifiant c'est "+idJobSelected);
			Job job = jobService.getRowById(idJobSelected);
			
			Map<String, Object> myModel = new HashMap<String, Object>();
			myModel.put("job", job);
			return new ModelAndView("voirtJob", myModel);
		}
		
		
		
		
		
}
