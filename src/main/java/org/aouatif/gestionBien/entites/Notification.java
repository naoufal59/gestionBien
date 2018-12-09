package org.aouatif.gestionBien.entites;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Notification {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long notificationId;
	@Enumerated(EnumType.STRING)
	private NotificationType type;
	private Long targetId;
	
	public Notification() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Notification(Long notificationId, NotificationType type) {
		super();
		this.notificationId = notificationId;
		this.type = type;

	}

	public Notification(Long i, Long j, NotificationType deleteannonce) {
		this.notificationId = i;
		this.type = deleteannonce;
		this.targetId=j;
	}

	public Long getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}
	public NotificationType getType() {
		return type;
	}
	public void setType(NotificationType type) {
		this.type = type;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	
	
}
