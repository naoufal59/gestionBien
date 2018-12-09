package org.aouatif.gestionBien.entites;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long userId;
	private String identifiant;
	private String email;
	private String password;
	@Enumerated(EnumType.STRING)
	UserRole role;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	   private Date date;

	   public User(String email) {
		super();
		this.email = email;
	}
	private String photo;
	   
	public User(Long userId) {
		super();
		this.userId = userId;
	}
	public User(String identifiant, String email, String password, UserRole role, Date date, String photo) {
		super();
		this.identifiant = identifiant;
		this.email = email;
		this.password = password;
		this.role = role;
		this.date = date;
		this.photo = photo;
	}
	public String getIdentifiant() {
		return identifiant;
	}
	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public UserRole getRole() {
		return role;
	}
	public void setRole(UserRole role) {
		this.role = role;
	}
	public User(Long userId, String email, String password, UserRole role) {
		super();
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.role = role;
	}
	
	
}
