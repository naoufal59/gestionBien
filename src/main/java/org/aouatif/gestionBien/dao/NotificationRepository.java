package org.aouatif.gestionBien.dao;

import org.aouatif.gestionBien.entites.Notification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification, Long>{
	
}
