package org.aouatif.gestionBien.web;


import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

//import javax.transaction.Transactional;

import org.aouatif.gestionBien.dao.AnnonceRepository;
import org.aouatif.gestionBien.dao.CommandeRepository;
import org.aouatif.gestionBien.dao.UserRepository;
import org.aouatif.gestionBien.entites.Annonce;

import org.aouatif.gestionBien.entites.Commande;
import org.aouatif.gestionBien.entites.User;
import org.aouatif.gestionBien.service.mailsender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AnnonceControleur {
	@Autowired
	private AnnonceRepository to;
	@Value("${dir.Annonce}")
    private String Annoncedir;
	@Autowired
	private CommandeRepository cm;
	 @Autowired
	 private mailsender ls;
	 @Autowired
 	 private UserRepository t;
	@RequestMapping("/aboutus")
	public String aboutus(Model model) {
		model.addAttribute("isConnecte",UserControleur.isConnecte);
   	 if(UserControleur.isConnecte) {
		    User y=  t.findUserByUserId(UserControleur.id);
			model.addAttribute("UserConnecter",y);}
   	 return "aboutus";
	}
    /* @RequestMapping(name="/SaleAnnonce")
	 public String S(Model mR) {
		 List<Annonce> gT = to.findAnnoncesForSale();
			mR.addAttribute("AnnonceSale",gT);
			return "SaleAnnonce";
	 }*/
    @RequestMapping("/Contactus")
    public String contactus(Model model ) {
    	 model.addAttribute("isConnecte",UserControleur.isConnecte);
    	 if(UserControleur.isConnecte) {
 		    User y=  t.findUserByUserId(UserControleur.id);
 			model.addAttribute("UserConnecter",y);}
    	return "contact";
    }
    @RequestMapping("/sendform")
	public String sendform(@RequestParam("name")  String fullname,@RequestParam("email")  String email,
			@RequestParam("phone")  String phone,@RequestParam("message")  String message) throws MessagingException {
		if(UserControleur.isConnecte) {
			ls.sendContact(fullname,email,phone, message);
			return "contact";
		}
		else {
			return "redirect:login";
		}
	}
	 @RequestMapping("/addAnnonce")
		public String addAnnonce(Model model) {
		 if(UserControleur.isConnecte) {
		 model.addAttribute("AnnonceClient",new Annonce());
		 if(UserControleur.isConnecte) {
			    User y=  t.findUserByUserId(UserControleur.id);
				model.addAttribute("UserConnecter",y);}
		 model.addAttribute("isConnecte",UserControleur.isConnecte);
			return "CreateAnnonceClient";}	else {   
				 
					return "redirect:/login";
					
				}
		}
	  
	 @RequestMapping("/ValiderAnnoce")
		public String ValiderAnnoce(Long id) throws MessagingException {
			 validAnnouce(id);
			 ls.send(UserControleur.mail, "validation", "votre annonce a ete approver vous pouvez consulter");
			 System.out.println(UserControleur.mail);
			return "redirect:ArticleClient";
		}
	 @RequestMapping("/ValiderAnnoceAdmin")
		public String ValiderAnnoceAdmin(Long id) {
			 validAnnouce(id);
			return "redirect:ArticleAdmin";
		}
		@RequestMapping("/rejetAnnoce")
		public String rejetAnnoce( Long id) throws MessagingException {
			rejectAnnouce(id);
			 ls.send(UserControleur.mail, "validation", "votre annonce a ete rejeter ");
			return "redirect:ArticleClient";
		}
		@RequestMapping("/rejetAnnoceAdmin")
		public String rejetAnnoceAdmin( Long id) {
			rejectAnnouce(id);
			return "redirect:ArticleAdmin";
		}
	    
		@RequestMapping(value="/tableCommandeAdmin")
		public String tablesCommandesCLients(Model md) {
			List<Commande> allAnnonces =cm.findAll();
			List<Commande> ClientCommandes = new ArrayList<Commande>();
			for (Commande annonce : allAnnonces) {
				
					ClientCommandes.add(annonce);
				
			}
			 User y=  t.findUserByUserId(UserControleur.id);
				//System.out.println("votre "+y);
	    	 md.addAttribute("UserConnecter",y);
	    	 md.addAttribute("numberOfNotifs",UserControleur.notificationsCount);
	     	
			md.addAttribute("Commandes", ClientCommandes);
			return "tableCommandeAdmin";
		}
	 
		@RequestMapping(value="/ValiderCommande")
		public String ValiderCommande(Long id) throws MessagingException {
			Commande a =  cm.getOne(id);
			a.setStatus(true);
			cm.save(a);
			ls.send(UserControleur.mail, "validation", "votre commande  a ete valider ");
			return "redirect:tableCommandeAdmin";
		}
		@RequestMapping(value="/rejetCommande")
		public String rejetCommande(Long id) throws MessagingException {
			Commande a =  cm.getOne(id);
			a.setStatus(false);
			cm.save(a);
			ls.send(UserControleur.mail, "validation", "votre commande a ete rejeter ");
			return "redirect:tableCommandeAdmin";
		}
		///// function
	
     public void validAnnouce(Long id) {
			Annonce a =  to.getOne(id);
			a.setStatus(true);
			to.save(a);
			}
	 public void rejectAnnouce(Long id) {
		 Annonce a =  to.getOne(id);
			a.setStatus(false);
			to.save(a);
		}
	 
}
