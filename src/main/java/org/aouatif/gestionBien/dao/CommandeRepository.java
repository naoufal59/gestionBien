package org.aouatif.gestionBien.dao;

import java.util.List;

import org.aouatif.gestionBien.entites.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepository extends JpaRepository<Commande, Long>{

	 List<Commande> findByEmailClient(String email);
}