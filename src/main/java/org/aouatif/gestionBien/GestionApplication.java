package org.aouatif.gestionBien;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;

import java.util.Date;

import org.aouatif.gestionBien.dao.AnnonceRepository;
import org.aouatif.gestionBien.dao.ClientRepository;
import org.aouatif.gestionBien.dao.NotificationRepository;
import org.aouatif.gestionBien.dao.StatisticsDataRepository;
import org.aouatif.gestionBien.dao.UserRepository;
import org.aouatif.gestionBien.entites.Annonce;
import org.aouatif.gestionBien.entites.AnnonceCategoty;
import org.aouatif.gestionBien.entites.AnnonceType;
import org.aouatif.gestionBien.entites.Notification;
import org.aouatif.gestionBien.entites.NotificationType;
import org.aouatif.gestionBien.entites.User;
import org.aouatif.gestionBien.entites.UserRole;
import org.aouatif.gestionBien.entites.statistiqueData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootApplication
public class GestionApplication {

	public static void main(String[] args) {
	ApplicationContext ctx=	SpringApplication.run(GestionApplication.class, args);
	ClientRepository tx = ctx.getBean(ClientRepository.class);   
	StatisticsDataRepository to = ctx.getBean(StatisticsDataRepository.class);
    AnnonceRepository tp = ctx.getBean(AnnonceRepository.class);
    NotificationRepository tr = ctx.getBean(NotificationRepository.class);    
  /* tr.save(new Notification((long)1,(long)1,NotificationType.modifie));*/
    to.save(new statistiqueData(1,1,1));   
	//ty.save( new User((long) 1,"aouatifmakhlouf","123456",UserRole.USER_ADMIN));
	tx.save(new User("aouatif","makhlouf.aouatif@gmail.com","123456",UserRole.USER_ADMIN,new Date(),"aoautif.jpg"));
	tx.save(new User("naoufal","naoufal.afa@gmail.com","123456",UserRole.USER_CLIENT,new Date(),"naoufal.jpg"));
	tx.save(new User("sara","sara.makhlouf@gmail.com","123456",UserRole.USER_CLIENT,new Date(),"sara.jpg"));
	tp.save(new Annonce((long)1,"appartement","Dans certaines grandes villes, trouver une offre d'achat d'appartement qui soit accessible "
			,(long)1530240,"appartement.jpg",true,5,(long)100,true,AnnonceCategoty.APPARTEMENT,AnnonceType.RENT,UserRole.USER_ADMIN
			));
	tp.save(new Annonce((long)2,"appartement","Je mets a location mon appartement 2chambres 1salon et salle de bain cuisine dans une résidence fermée sécurisé 24/24 camera "
			,(long)2000,"appartement1.jpg",true,0,(long)55,false,AnnonceCategoty.APPARTEMENT,AnnonceType.SALE,UserRole.USER_ADMIN
			));
	Page<User> ets = tx.findAll(new PageRequest(0, 5));
	ets.forEach(e->System.out.println(e.getIdentifiant()));
	}
}
