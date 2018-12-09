package org.aouatif.gestionBien.entites;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Commande {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long commandeId;
	@ManyToOne(cascade=CascadeType.DETACH,fetch= FetchType.EAGER)
	private Annonce annonce;
	private Boolean status;
	@Temporal(value = TemporalType.DATE)
	private Date commandeDate ;
	private String emailClient;
	public Long getCommandeId() {
		return commandeId;
	}
	public void setCommandeId(Long commandeId) {
		this.commandeId = commandeId;
	}
	public Annonce getAnnonce() {
		return annonce;
	}
	public void setAnnonce(Annonce annonce) {
		this.annonce = annonce;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Date getCommandeDate() {
		return commandeDate;
	}
	public void setCommandeDate(Date commandeDate) {
		this.commandeDate = commandeDate;
	}
	public String getEmailClient() {
		return emailClient;
	}
	public void setEmailClient(String EmailClient) {
		emailClient = EmailClient;
	}
	public Commande(Long commandeId, Annonce annonce, Boolean status, Date commandeDate, String emailClient) {
		super();
		this.commandeId = commandeId;
		this.annonce = annonce;
		this.status = status;
		this.commandeDate = commandeDate;
		this.emailClient = emailClient;
	}
	public Commande() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
