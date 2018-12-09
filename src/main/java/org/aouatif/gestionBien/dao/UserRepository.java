package org.aouatif.gestionBien.dao;

import org.aouatif.gestionBien.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>{
	@Query("select u FROM User u where u.email = :email and u.password = :password") 
    User findUserByEmailAndPassword(@Param("email") String email , @Param("password") String password);
	
	User findByEmail(String mail);
	
	User findUserByUserId(long id);
}