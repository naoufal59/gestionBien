package org.aouatif.gestionBien.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aouatif.gestionBien.dao.AnnonceRepository;
import org.aouatif.gestionBien.dao.ClientRepository;
import org.aouatif.gestionBien.dao.CommandeRepository;
import org.aouatif.gestionBien.dao.NotificationRepository;
import org.aouatif.gestionBien.dao.StatisticsDataRepository;
import org.aouatif.gestionBien.dao.UserRepository;
import org.aouatif.gestionBien.entites.Annonce;
import org.aouatif.gestionBien.entites.AnnonceCategoty;
import org.aouatif.gestionBien.entites.AnnonceType;
import org.aouatif.gestionBien.entites.Commande;
import org.aouatif.gestionBien.entites.Notification;
import org.aouatif.gestionBien.entites.NotificationType;
import org.aouatif.gestionBien.entites.StatisticsDto;
import org.aouatif.gestionBien.entites.User;
import org.aouatif.gestionBien.entites.UserRole;
import org.aouatif.gestionBien.entites.statistiqueData;
import org.aouatif.gestionBien.service.mailsender;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UserControleur {
	 @Autowired
	 private mailsender ls;   
	public static Integer notificationsCount=0;
	public static Long announceVisitCount=(long) 0;
	  
    static String mail;
	@Autowired
	NotificationRepository notificationRep;
	@Autowired
	StatisticsDataRepository statsRep;
	public static boolean isConnecte=false;
	public static User userAdmin = new User();
	public static User userclient = new User();
	User user;
	static Long id;
	List<Notification> lastNotifs;
	public static User userClient ;
	@Autowired
	private CommandeRepository cm;
	  HttpSession  session;
	 Long Username;
	 @Autowired
	 private UserRepository t;
	@Autowired
	private AnnonceRepository to;
	@Value("${dir.images}")
    private String imagedir;
	
	
	@Value("${dir.Annonce}")
    private String Annoncedir;
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login(Model model) {
		return "login";
	}
	
	/// function show formulaire inscription
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String register(Model model) {
		 model.addAttribute("User",new User() );
		return "register";
	}
	
	
	///// inscription des clients
	@RequestMapping(value="/registerUser", method=RequestMethod.POST)
	public String registerUser(User et, BindingResult ty,@RequestParam(name="picture")MultipartFile tu ) throws IllegalStateException, IOException {
		if (!(tu.isEmpty())) {et.setPhoto(tu.getOriginalFilename());}
		et.setRole(UserRole.USER_CLIENT); 
		t.save(et);
		 if (!(tu.isEmpty())) {
			 et.setPhoto(tu.getOriginalFilename());
			// System.out.println(System.getProperty("user.home")+"\\cli\\"+tu.getOriginalFilename());
			tu.transferTo(new File(imagedir+et.getUserId()));
		}
		return "redirect:login";
	}
	

	
	/////checkLogin
	@RequestMapping(value="/checkLogin", method=RequestMethod.POST)
	public  String checkLogin(@RequestParam("email") String email , @RequestParam("password") String password ,HttpServletRequest request
			,Model md,
			@RequestParam(value="priceMin",required=false) Long priceMin,@RequestParam(value="priceMax",required=false) Long priceMax
			,@RequestParam(value="type",required=false) String type,@RequestParam(value="category",required=false) String category
			) {
		
		Integer userTypeIndex = userExist(email,password);
	    session = request.getSession(true);
	    
	    if(email.isEmpty() && password.isEmpty()) {
	    	return "redirect:login";
	    }
	    if(userTypeIndex==1) { 
	    	session.setAttribute("email", email);
	    	id= t.findByEmail((String) session.getAttribute("email")).getUserId();
	    	// mail =(String) session.getAttribute("email");
	    	System.out.println("votre id"+id);
		   return "redirect:/Admin/adminIndex";
		}
	   else if (userTypeIndex==2) {	
		   session.setAttribute("email", email);  
		   id= t.findByEmail((String) session.getAttribute("email")).getUserId();
		   mail =(String) session.getAttribute("email");
		 if(priceMax == null) priceMax = Long.MAX_VALUE;
			if(priceMin == null) priceMin = (long) 0.0;
			
			List<Annonce> annonces = getAllActiveAnnonceVisiteur();
			ArrayList<Annonce> filteredRusults = new ArrayList<Annonce>();
			for (Annonce annonce : annonces) {
				if(category != null) {
				if(annonce.getCategory().name().equals(category) && annonce.getType().name().equals(type) 
						&& annonce.getPrice()>priceMin && annonce.getPrice()<priceMax) {
					filteredRusults.add(annonce);
				}}else {
					filteredRusults.add(annonce);
				}
				
			}
			//System.out.println("Client"+isConnecte);
			md.addAttribute("AnnonceClient",filteredRusults);
			md.addAttribute("isConnecte",isConnecte);
		return "redirect:ClientsIndex";
		}
		else {
			return "redirect:login";
		}
		
	}
	//////index Client
	@RequestMapping(value="/ClientsIndex")
	public String indexClient(Model ms,
			@RequestParam(value="priceMin",required=false) Long priceMin,@RequestParam(value="priceMax",required=false) Long priceMax
			,@RequestParam(value="type",required=false) String type,@RequestParam(value="category",required=false) String category) {
		if(priceMax == null) priceMax = Long.MAX_VALUE;
		if(priceMin == null) priceMin = (long) 0.0;
		//isConnecte=false;
		List<Annonce> annonces = getAllActiveAnnonceVisiteur();
		ArrayList<Annonce> filteredRusults = new ArrayList<Annonce>();
		for (Annonce annonce : annonces) {
			if(category != null) {
			if(annonce.getCategory().name().equals(category) && annonce.getType().name().equals(type) 
					&& annonce.getPrice()>priceMin && annonce.getPrice()<priceMax) {
				filteredRusults.add(annonce);
			}}else {
				filteredRusults.add(annonce);
			}
			
		}
		
		 statistiqueData stat = statsRep.findAll().get(0);
		 long websiteisitCount=stat.getVisitsCount();
		 stat.setVisitsCount(websiteisitCount+1 );
		 statsRep.save(stat);
		
		
		//System.out.println("visiteur"+isConnecte);
		ms.addAttribute("AnnonceClient",filteredRusults);
		ms.addAttribute("isConnecte",isConnecte);
		if(UserControleur.isConnecte) {
		User y=  t.findUserByUserId(id);
		
		ms.addAttribute("UserConnecter",y);}
		return "ClientsIndex";
	}
	
    ////index admin
		@RequestMapping(value="/Admin/adminIndex")
		 public String adminIndex(Model model) {
			   StatisticsDto statisticsDto = getStatistics();
				List<Notification> notifications =notificationRep.findAll();
				List<Notification> lastNotifs = new ArrayList<Notification>();
				int startindex = notifications.size()-4;
				int index = 0;
				for (Notification notification : notifications) {
					if(index>=startindex) {
					lastNotifs.add(notification);
					}
					index++;
				}
				Collections.reverse(lastNotifs);
				User y=  t.findUserByUserId(id);
				
				model.addAttribute("UserConnecter",y);
				model.addAttribute("stats",statisticsDto);
				model.addAttribute("lastNotifs",lastNotifs);
				model.addAttribute("numberOfNotifs",notificationsCount);
			 return "adminIndex";
		 }
		
	/// function logout
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	   public String logout(Model model,HttpServletRequest request) {	
		 session = request.getSession();
		 session.invalidate();
		 //session.removeAttribute("email");
         //session.invalidate();
		 isConnecte=false;
		return  "redirect:login";
        
	}
	
	/// function logout Client
	@RequestMapping(value="/logoutClient", method=RequestMethod.GET)
	   public String logoutClient(Model model,HttpServletRequest request) {	
		 session = request.getSession();
		 session.invalidate();
		
		 isConnecte=false;
		return  "redirect:ClientsIndex";
     
	}

	
	////  formulaire pour ajouter annoce Client
	   @RequestMapping(value="/AnnonceAdmin",method=RequestMethod.GET)
       public String FormClients(Model model  ) {
		   model.addAttribute("isConnecte",isConnecte);
		User y=  t.findUserByUserId(id);
	
		model.addAttribute("numberOfNotifs",notificationsCount);
		model.addAttribute("UserConnecter",y);
		 model.addAttribute("Annonce",new Annonce() );
    	 return "annonceAdmin";
     }
	 
	 //// add annoce Admin
	   @RequestMapping(value="/SaveAnnoce", method=RequestMethod.POST)
	   public String SaveAnnoce(Annonce announce, BindingResult ty,@RequestParam(name="annonceImage")MultipartFile tu
				,@RequestParam("type") String type,@RequestParam("category") String category,
				@RequestParam("ascenseur") String ascenseur,Model Md ) throws IllegalStateException, IOException {
		 
		 if (!(tu.isEmpty())) {announce.setPhoto(tu.getOriginalFilename());}
			announce.setCategory(AnnonceCategoty.valueOf(category));
			announce.setType(AnnonceType.valueOf(type));
			announce.setStatus(false);
			announce.setAscenceur(ascenseur.equals("OUI"));
			announce.setUser(UserRole.USER_ADMIN);
			to.save(announce);
			 if (!(tu.isEmpty())) {
				 announce.setPhoto(tu.getOriginalFilename());
				// System.out.println(System.getProperty("user.home")+"\\cli\\"+tu.getOriginalFilename());
				tu.transferTo(new File(Annoncedir+announce.getIdAnnonce()));
			}
			 
			    Notification notif = new Notification();
				notif.setType(NotificationType.ajouter);
				notif.setTargetId(announce.getIdAnnonce());
				notificationsCount++;
				notificationRep.save(notif);
				User y=  t.findUserByUserId(id);
				
				Md.addAttribute("UserConnecter",y);
			return "redirect:ArticleAdmin";
		}
	
	 
	    @RequestMapping(value="/getphoto",produces=MediaType.IMAGE_JPEG_VALUE)
	    @ResponseBody
	    public byte[] getphoto(Long id) throws FileNotFoundException, IOException {
		 File i = new File(Annoncedir+id);
		 return IOUtils.toByteArray(new FileInputStream(i));
		 
	 }
	    @RequestMapping(value="/getpictureClient",produces=MediaType.IMAGE_JPEG_VALUE)
	    @ResponseBody
	    public byte[] getpictureClient(Long id) throws FileNotFoundException, IOException {
		 File i = new File(imagedir+UserControleur.id);
		 return IOUtils.toByteArray(new FileInputStream(i));
		 
	 }
	 /// liste des annonce client
	    @RequestMapping(value="/ArticleAdmin")
        public String Index(Model model,@RequestParam(name="page",defaultValue="0")int page,
    		 @RequestParam(name="motCle",defaultValue="")String mc
    		 ) {
		    List<Annonce> allAnnonces =to.findAll();
			List<Annonce> userAnnonces = new ArrayList<Annonce>();
			for (Annonce annonce : allAnnonces) {
				if(annonce.getUser() == UserRole.USER_ADMIN) {
					userAnnonces.add(annonce);
				}
			}
			User y=  t.findUserByUserId(id);
			
			model.addAttribute("UserConnecter",y);
		
			model.addAttribute("numberOfNotifs",notificationsCount);
    	 model.addAttribute("Annonce", userAnnonces);
  
    			 return "ArticleAdmin";
     }
	  
	 /// liste des annoce partie admin
	    @RequestMapping(value="/ArticleClient")
       public String ArticleClient(Model model,@RequestParam(name="page",defaultValue="0")int page,
    		 @RequestParam(name="motCle",defaultValue="")String mc
    		 ) {
		 List<Annonce> allAnnonces =to.findAll();
			List<Annonce> userAnnonces = new ArrayList<Annonce>();
			for (Annonce annonce : allAnnonces) {
				if(annonce.getUser() == UserRole.USER_CLIENT) {
					userAnnonces.add(annonce);
				}
			}
			User y=  t.findUserByUserId(id);
			//System.out.println("votre "+y);
			model.addAttribute("UserConnecter",y);
    
    	 model.addAttribute("ArticleClient", userAnnonces);
    	
			model.addAttribute("numberOfNotifs",notificationsCount);
    			 return "ArticleClient";
     }
	 
	 //// function delet annonce admin 
	   @RequestMapping(value="/Supprimer")
	   public String Supprimer(Long id) {
			Notification notif = new Notification();
			notif.setType(NotificationType.supprimer);
			notif.setTargetId(id);
			notificationRep.save(notif);
		    notificationsCount++;
		 to.deleteById(id);
		 return "redirect:ArticleAdmin";
	   }
	 
	 //// update client partie admin 
	   @RequestMapping(value="/editFormAnnonce")
	   public String edit(Long id , Model md) {
		 Annonce tw = to.getOne(id);
		md.addAttribute("Annonce", tw);
		User y=  t.findUserByUserId(UserControleur.id);
		//System.out.println("votre "+y);
		md.addAttribute("UserConnecter",y);
		
		md.addAttribute("numberOfNotifs",notificationsCount);
		 return "editFormAnnonce";
	 }
	 
	 /// update Annonce
	   @RequestMapping(value="/UpdateAnnoce", method=RequestMethod.POST)
	   public String UpdateAnnoce(Annonce announce, BindingResult ty,@RequestParam(name="annonceImage")MultipartFile tu
				,@RequestParam("type") String type,@RequestParam("category") String category,
				@RequestParam("ascenseur") String ascenseur ) throws IllegalStateException, IOException {
			if (!(tu.isEmpty())) {announce.setPhoto(tu.getOriginalFilename());}
			announce.setCategory(AnnonceCategoty.valueOf(category));
			announce.setType(AnnonceType.valueOf(type));
			announce.setStatus(false);
			announce.setAscenceur(ascenseur.equals("OUI"));
			announce.setUser(UserRole.USER_ADMIN);
			announce.setEmail((String) session.getAttribute("email"));
			to.save(announce);
			 if (!(tu.isEmpty())) {
				 announce.setPhoto(tu.getOriginalFilename());
				// System.out.println(System.getProperty("user.home")+"\\cli\\"+tu.getOriginalFilename());
				tu.transferTo(new File(Annoncedir+announce.getIdAnnonce()));
			}
			    Notification notif = new Notification();
				notif.setType(NotificationType.modifie);
				notif.setTargetId(announce.getIdAnnonce());
				notificationsCount++;
				notificationRep.save(notif);
			return "redirect:ArticleAdmin";
		}
	   
	   @RequestMapping(value="/EditClientAnnonce")
		 public String EditClientAnnonce(Model m,Long id) {
			     Annonce tw = to.getOne(id);
			     if(isConnecte) {
			 	User y=  t.findUserByUserId(UserControleur.id);
				
				m.addAttribute("UserConnecter",y);}
				 m.addAttribute("Annonce", tw);
				 m.addAttribute("isConnecte",isConnecte);
				return "EditClientAnnonce";
		 }
	   @RequestMapping(value="/SupprimerClient")
	   public String SupprimerClient(Long id,Model model) {
		   to.deleteById(id);
		   Notification notif = new Notification();
			notif.setType(NotificationType.supprimer);
			notif.setTargetId(id);
			notificationRep.save(notif);
		    notificationsCount++;
		   return "redirect:/MesAnnoces";
	   }
	    //// update annonce client
	   @RequestMapping(value="/editFormAnnonceClient", method=RequestMethod.POST)
	   public String editFormAnnonceClient(Annonce announce, BindingResult ty,@RequestParam(name="annonceImage")MultipartFile tu
				,@RequestParam("type") String type,@RequestParam("category") String category,
				@RequestParam("ascenseur") String ascenseur ) throws IllegalStateException, IOException {
			if (!(tu.isEmpty())) {announce.setPhoto(tu.getOriginalFilename());}
			announce.setCategory(AnnonceCategoty.valueOf(category));
			announce.setType(AnnonceType.valueOf(type));
			//announce.setStatus(false);
			announce.setAscenceur(ascenseur.equals("OUI"));
			announce.setUser(UserRole.USER_CLIENT);
			announce.setEmail((String) session.getAttribute("email"));
			to.save(announce);
			 if (!(tu.isEmpty())) {
				 announce.setPhoto(tu.getOriginalFilename());
				 tu.transferTo(new File(Annoncedir+announce.getIdAnnonce()));
			}
			 Notification notif = new Notification();
				notif.setType(NotificationType.modifie);
				notif.setTargetId(announce.getIdAnnonce());
				notificationsCount++;
				notificationRep.save(notif);  
			 
			return "redirect:/MesAnnoces";
		}
	   ////Detail of annonce admin
	   @RequestMapping(value="/ViewContentAnnonce", method=RequestMethod.GET)
	   public String ViewContent(Model model,Long id ) {
	 	 Annonce ts =to.getOne(id);
	 //	incrementAnnonceViews();
		  model.addAttribute("Annonce", ts);
		  User y=  t.findUserByUserId(UserControleur.id);
			//System.out.println("votre "+y);
			model.addAttribute("UserConnecter",y);
		
			model.addAttribute("numberOfNotifs",notificationsCount);
			return "ViewContentAnnonce";
		}
	   
	   //// mes annonces
	   @RequestMapping("/MesAnnoces")
	    public String annonces(Model model) {
		   if(isConnecte) {
		 model.addAttribute("annonces", getAllActiveAnnonce());
		 model.addAttribute("isConnecte",isConnecte);
		   if(isConnecte) {
		    User y=  t.findUserByUserId(id);
			model.addAttribute("UserConnecter",y);}
			return "mesAnnonces";
		   }	else {   
				   System.out.println("wwwwww"+isConnecte);
					return "redirect:/login";
					
				}
		}
	 
	    //////Add Annonce function
	    @RequestMapping(value="/SaveAnnoceClient", method=RequestMethod.POST)
	    public String SaveAnnoce(Annonce announce, BindingResult ty,@RequestParam(name="annonceImage")MultipartFile tu
				,@RequestParam("type") String type,@RequestParam("category") String category,Model model,
				@RequestParam("ascenseur") String ascenseur ) throws Exception {
		
		 if (!(tu.isEmpty())) {announce.setPhoto(tu.getOriginalFilename());}
			announce.setCategory(AnnonceCategoty.valueOf(category));
			announce.setType(AnnonceType.valueOf(type));
			announce.setStatus(false);
			if(ascenseur.equals("OUI"))
			{announce.setAscenceur(true);}
			else announce.setAscenceur(false);
			announce.setUser(UserRole.USER_CLIENT);
			//String Email =(String)session.getAttribute("email");
		    announce.setEmail((String) session.getAttribute("email"));
		   // System.out.println((String) session.getAttribute("email"));
			to.save(announce);
			 if (!(tu.isEmpty())) {
				 announce.setPhoto(tu.getOriginalFilename());
				// System.out.println(System.getProperty("user.home")+"\\cli\\"+tu.getOriginalFilename());
				tu.transferTo(new File(Annoncedir+announce.getIdAnnonce()));
			}
		   // ls.send("makhlouf.aouatif@gmail.com", "new annonce", "you have new annonce from client you must be approved");
			    Notification notif = new Notification();
				notif.setType(NotificationType.ajouter);
				notif.setTargetId(announce.getIdAnnonce());
				notificationsCount++;
				notificationRep.save(notif);
				
				        if(isConnecte) {
					    User y=  t.findUserByUserId(id);
						model.addAttribute("UserConnecter",y);}
				 model.addAttribute("isConnecte",isConnecte);
			model.addAttribute("annonces", getAllActiveAnnonce());
			return "mesAnnonces";
		}
	 
	    /////////mes Commandes
	    @RequestMapping(value="/mesCommandes")
		public String mesComandes(Model md) {
	    	if(isConnecte) {
			List<Commande> allAnnonces =cm.findByEmailClient((String) session.getAttribute("email"));
			List<Commande> ClientCommandes = new ArrayList<Commande>();
			for (Commande annonce : allAnnonces) {
				
					ClientCommandes.add(annonce);
				
			} 			
			//System.out.println("votre "+y);
			User y=  t.findUserByUserId(id);
			
			md.addAttribute("UserConnecter",y);
			md.addAttribute("commande", ClientCommandes);
			md.addAttribute("isConnecte",isConnecte);
			return "mesCommandes";	}else {   
				   System.out.println("wwwwww"+isConnecte);
					return "redirect:/login";
					
				}
				
		}
	    
	    
		/////// Commande function
		@RequestMapping(value="/commander")
		public String commander(Long id,Model model) {
	
			  System.out.println(isConnecte);
			if(isConnecte ) {
				Commande commande = new Commande();
				commande.setAnnonce(to.getOne(id));
				commande.setCommandeDate(new Date());
				commande.setStatus(false);
				commande.setEmailClient((String) session.getAttribute("email"));
				cm.save(commande);
				model.addAttribute("isConnecte",isConnecte);
				model.addAttribute("commande", commande);
				Notification notif = new Notification();
				notif.setType(NotificationType.NewCommand);
				notif.setTargetId(commande.getCommandeId());
				notificationsCount++;
				notificationRep.save(notif);
				User y=  t.findUserByUserId(id);
				//System.out.println("votre "+y);
				model.addAttribute("UserConnecter",y);
				return "redirect:mesCommandes";
				
			}else {   
			   System.out.println("wwwwww"+isConnecte);
				return "redirect:/login";
				
			}
			
					}
		
		@RequestMapping("/Allnotification")
		public String Allnotification(Model model) {
			    User y=  t.findUserByUserId(id);
				//System.out.println("votre "+y);
				model.addAttribute("UserConnecter",y);
				model.addAttribute("numberOfNotifs",notificationsCount);
				List<Notification> notifications = notificationRep.findAll();
				Collections.reverse(notifications);
				notificationsCount = 0;
				   
				model.addAttribute("notifications", notifications);
			
			return "inbox";
		}
		/*@RequestMapping("/notification")
		public String notification(Model model) {
			List<Notification> notifications = notificationRep.findAll();
			Collections.reverse(notifications);
			notificationsCount = 0;
			model.addAttribute("notifications", notifications);
			model.addAttribute("numberOfNotifs",notificationsCount);
			return "inbox";
		}
		 
		*/
		    ////// show Content of annonce Client
		    @RequestMapping(value="/ViewContentAnnonceClient", method=RequestMethod.GET)
			public String ViewContentC(Model model,Long id ) {
			 Annonce ts =to.getOne(id);
			 
			    statistiqueData stat = this.statsRep.findAll().get(0);
				long announceVisitCount = stat.getAnnounceVisitCount();
				stat.setAnnounceVisitCount(announceVisitCount + 1 );
				statsRep.save(stat);
			 
			 model.addAttribute("isConnecteC",UserControleur.isConnecte);
			 model.addAttribute("AnnonceClient", ts);
				model.addAttribute("isConnecte",isConnecte);
				 if(isConnecte) {
					    User y=  t.findUserByUserId(UserControleur.id);
						model.addAttribute("UserConnecter",y);}
				
				model.addAttribute("numberOfNotifs",notificationsCount);
				return "ViewContentAnnonceClient";
			}
	   
		////function
		     public List<Annonce> getAllVentesActiveAnnonce(){
				List<Annonce> allAnnonces = to.findAll();
				List<Annonce> activeAnnonces = new ArrayList<Annonce>();
				for (Annonce annonce : allAnnonces) {
					if(annonce.getStatus()==true && annonce.getType() == AnnonceType.SALE) {
						activeAnnonces.add(annonce);
					}
				}
				return activeAnnonces;
			}
		     public List<Annonce> getAllLocationsActiveAnnonce(){
				List<Annonce> allAnnonces = to.findAll();
				List<Annonce> activeAnnonces = new ArrayList<Annonce>();
				for (Annonce annonce : allAnnonces) {
					if(annonce.getStatus()==true && annonce.getType() == AnnonceType.RENT) {
						activeAnnonces.add(annonce);
					}
				}
				return activeAnnonces;
			}
			 public List<Annonce> getAllActiveAnnonce(){
					List<Annonce> allAnnonces = to.findByEmail((String) session.getAttribute("email"));
					List<Annonce> activeAnnonces = new ArrayList<Annonce>();
					 System.out.println((String) session.getAttribute("email"));
					for (Annonce annonce : allAnnonces) {
						
							activeAnnonces.add(annonce);
						
					}
					return activeAnnonces;
				}
			 public List<Annonce> getAllActiveAnnonceVisiteur(){
					List<Annonce> allAnnonces = to.findAll();
					List<Annonce> activeAnnonces = new ArrayList<Annonce>();
					// System.out.println((String) session.getAttribute("email"));
					for (Annonce annonce : allAnnonces) {
						if(annonce.getStatus()==true) {
							activeAnnonces.add(annonce);
						}
					}
					return activeAnnonces;
				}
			 public Integer userExist(String email , String password) {
					User user = this.t.findUserByEmailAndPassword(email, password);
					if(user==null)
							return 0;
					else if (user.getRole() == UserRole.USER_ADMIN ) {
						isConnecte = true;
						userAdmin = user;
						return 1;
					}
					else if (user.getRole() == UserRole.USER_CLIENT ) {
						isConnecte = true;
						userclient = user;
						return 2;
					}else {
						return 0;
					}
				}
			 
			 
		        public StatisticsDto getStatistics() {
					StatisticsDto stats = new StatisticsDto();
					statistiqueData statsData =statsRep.getOne((long) 1);
					stats.announcesViews = statsData.getAnnounceVisitCount();
					stats.visitsCount = statsData.getVisitsCount();
					stats.numberOfClients = (long) t.findAll().size() - 1; 
					
					return stats;
				}
				
		  /*      private void sendEmail() throws Exception{
		        	        MimeMessage message = sender.createMimeMessage();
		           	        MimeMessageHelper helper = new MimeMessageHelper(message);
		        	        helper.setTo("naoufal.afa@gmail.com");
		            	    helper.setText("you have new annonce need to be approved?");
		        	        helper.setSubject("new annonce");
		        	
		        	        sender.send(message);
		        	    }*/

		 /*		public void incrementAnnonceViews(){
					statistiqueData stat = new statistiqueData();
					stat.setAnnounceVisitCount(announceVisitCount + 1 );
					statsRep.save(stat);
				}
				
				public void incrementSiteVisits(){
					statistiqueData stat = new statistiqueData();
					long websiteisitCount = stat.getVisitsCount();
					stat.setVisitsCount(websiteisitCount + 1);
					statsRep.save(stat);
				}*/
}
