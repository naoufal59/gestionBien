package org.aouatif.gestionBien.dao;

import java.util.List;

import org.aouatif.gestionBien.entites.Annonce;
import org.aouatif.gestionBien.entites.User;
import org.aouatif.gestionBien.entites.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnnonceRepository extends JpaRepository<Annonce, Long>{
		  public List<Annonce> findByTitle(String n);
		  public Page<Annonce> findByTitle(String n , Pageable page);
		  @Query("select u from Annonce u where u.type like 'RENT' OR u.type like 'SALE' ")
		  List<Annonce> findAnnonces();
	      @Query("select u from Annonce u where u.type like 'SALE'")
		  List<Annonce> findAnnoncesForSale();
	      @Query("select u from Annonce u where u.type like 'RENT'")
		  List<Annonce> findAnnoncesForRent();
		  @Query("select e from Annonce e where e.title like :x")
		  public Page<Annonce> rechercheAnnonce(@Param("x")String mc , Pageable page);
		  @Query("select e from Annonce e where e.title like :x and e.userRole like 'USER_ADMIN'")
		  public Page<Annonce> rechercheAnnonceAdmin(@Param("x")String mc , Pageable page);
			
	     List<Annonce> findByEmail(String email);
	     Annonce findByIdAnnonce(Long id);
}
