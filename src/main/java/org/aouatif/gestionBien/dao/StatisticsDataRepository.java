package org.aouatif.gestionBien.dao;

import org.aouatif.gestionBien.entites.statistiqueData;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StatisticsDataRepository extends JpaRepository<statistiqueData, Long>{

	
}
