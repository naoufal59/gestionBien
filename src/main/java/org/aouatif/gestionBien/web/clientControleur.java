package org.aouatif.gestionBien.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.aouatif.gestionBien.dao.ClientRepository;
import org.aouatif.gestionBien.dao.UserRepository;
import org.aouatif.gestionBien.entites.Notification;
import org.aouatif.gestionBien.entites.StatisticsDto;
import org.aouatif.gestionBien.entites.User;
import org.aouatif.gestionBien.entites.UserRole;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping(value="/Admin")
public class clientControleur {
     @Autowired
	 private ClientRepository tx;
      @Value("${dir.images}")
     private String imagedir;        
      @Autowired
 	 private UserRepository t;
     @RequestMapping(value="/Index")
     public String Index(Model model,@RequestParam(name="page",defaultValue="0")int page,
    		 @RequestParam(name="motCle",defaultValue="")String mc
    		 ) {
    	 Page<User> tw =tx.rechercheclient("%"+mc+"%",new PageRequest(page, 5));
    	 int pageCounts= tw.getTotalPages();
    	 int[]pagex = new int[pageCounts];
    	 for (int i = 0; i < pageCounts; i++) {
    		 pagex[i]=i;
		}
    	 User y=  t.findUserByUserId(UserControleur.id);
			//System.out.println("votre "+y);
    	 model.addAttribute("UserConnecter",y);
    	 model.addAttribute("page", pagex);
    	 model.addAttribute("Clients", tw);
    	 model.addAttribute("motCle", mc);
    	 model.addAttribute("numberOfNotifs",UserControleur.notificationsCount);
    	 model.addAttribute("pagecourante",page);
    			 return "clients";
     }
	 @RequestMapping(value="/form",method=RequestMethod.GET)
     public String FormClients(Model model  ) {
		 model.addAttribute("User",new User() );
		 User y=  t.findUserByUserId(UserControleur.id);
			//System.out.println("votre "+y);
		 model.addAttribute("UserConnecter",y);
		 model.addAttribute("numberOfNotifs",UserControleur.notificationsCount);
	    	
    	 return "FormClients";
     }
	 
	 @RequestMapping(value="/SaveClients",method=RequestMethod.POST)
     public String Save(User et, BindingResult ty,@RequestParam(name="picture")MultipartFile tu) throws IllegalStateException, IOException {
		 if (!(tu.isEmpty())) {et.setPhoto(tu.getOriginalFilename());}
		 tx.save(et);
		 if (!(tu.isEmpty())) {
			 et.setPhoto(tu.getOriginalFilename());
			// System.out.println(System.getProperty("user.home")+"\\cli\\"+tu.getOriginalFilename());
			tu.transferTo(new File(imagedir+et.getUserId()));
		}
		 if (ty.hasErrors()) {
			 return "FormClients";
		}
		
    	 return "redirect:Index";
}
	 @RequestMapping(value="/getphoto",produces=MediaType.IMAGE_JPEG_VALUE)
	 @ResponseBody
	 public byte[] getphoto(Long id) throws FileNotFoundException, IOException {
		 File i = new File(imagedir+id);
		 return IOUtils.toByteArray(new FileInputStream(i));
		 
	 }
	 @RequestMapping(value="/Supprimer")
	 public String Supprimer(Long id) {
		 tx.deleteById(id);
		 return "redirect:Index";
	 }
	 @RequestMapping(value="/edit")
	 public String edit(Long id , Model md) {
		 User tw = tx.getOne(id);
		 User y=  t.findUserByUserId(UserControleur.id);
			//System.out.println("votre "+y);
			md.addAttribute("UserConnecter",y);
		md.addAttribute("User", tw);
		 return "editForm";
	 }
	
	 
	 @RequestMapping(value="/UpdateClients",method=RequestMethod.POST)
     public String Update(User et, BindingResult ty,@RequestParam(name="picture")MultipartFile tu) throws IllegalStateException, IOException {
		 if (!(tu.isEmpty())) {et.setPhoto(tu.getOriginalFilename());}
		 
		 if(et.getEmail().equals("makhlouf.aouatif@gmail.com"))
		 {et.setRole(UserRole.USER_ADMIN);
		
		 }
		 else 		 {et.setRole(UserRole.USER_CLIENT);
		 			 
		 }
		 tx.save(et);
		 if (!(tu.isEmpty())) {
			 et.setPhoto(tu.getOriginalFilename());
			// System.out.println(System.getProperty("user.home")+"\\cli\\"+tu.getOriginalFilename());
			tu.transferTo(new File(imagedir+et.getUserId()));
		}
		 if (ty.hasErrors()) {
			 return "editForm";
		}
		
    	 return "redirect:Index";
}
	 /*@RequestMapping(value="/login")
	 public String login() {
		 return "login";
	 }*/
	 
	 @RequestMapping(value="/ViewContent", method=RequestMethod.GET)
		public String ViewContent(Model model,Long id ) {
		 User ts =tx.getOne(id);
		  model.addAttribute("CLient", ts);
		  User y=  t.findUserByUserId(UserControleur.id);
			//System.out.println("votre "+y);
		  model.addAttribute("numberOfNotifs",UserControleur.notificationsCount);
	     	
		   model.addAttribute("UserConnecter",y);
			return "ViewContent";
		}
}