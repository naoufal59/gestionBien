package org.aouatif.gestionBien.dao;
import java.util.Date;
import java.util.List;

import org.aouatif.gestionBien.entites.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<User, Long>  {
	public List<User> findByIdentifiant(String n);
	public Page<User> findByIdentifiant(String n , Pageable page);
	
	@Query("select e from User e where e.identifiant like :x")
	public Page<User> rechercheclient(@Param("x")String mc , Pageable page);
	
	@Query("select e from User e where e.date >:x and e.date<:y")
	public List<User> rechercheclient(@Param("x")Date d1, @Param("y")Date d2);
}
