package org.aouatif.gestionBien.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
@Entity
public class Annonce {
   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idAnnonce;
	private String title;
	@Size(min=5 , max = 900)
	private String description;
	private Long price;
	private String photo;
	private boolean ascenceur;
	private int etage;
	private Long surface;
	private Boolean status;
	@Enumerated(EnumType.STRING)
	private AnnonceCategoty category;
	
	@Enumerated(EnumType.STRING)
	private AnnonceType type;
	@Enumerated(EnumType.STRING)
	private UserRole userRole;
    private String email;
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Annonce(Long idAnnonce, String title, String description, Long price, String photo, boolean ascenceur,
			int etage, Long surface, Boolean status, AnnonceCategoty category, AnnonceType type, UserRole userRole,
			String email) {
		super();
		this.idAnnonce = idAnnonce;
		this.title = title;
		this.description = description;
		this.price = price;
		this.photo = photo;
		this.ascenceur = ascenceur;
		this.etage = etage;
		this.surface = surface;
		this.status = status;
		this.category = category;
		this.type = type;
		this.userRole = userRole;
		this.email = email;
	}

	public Annonce() {
			
	}

	public Annonce(Long idAnnonce, String title, String description, Long price, String photo, boolean ascenceur,
			int etage, Long surface, Boolean status, AnnonceCategoty category, AnnonceType type, UserRole user) {
		super();
		this.idAnnonce = idAnnonce;
		this.title = title;
		this.description = description;
		this.price = price;
		this.photo = photo;
		this.ascenceur = ascenceur;
		this.etage = etage;
		this.surface = surface;
		this.status = status;
		this.category = category;
		this.type = type;
		this.userRole = user;
	}

	public Long getIdAnnonce() {
		return idAnnonce;
	}

	public void setIdAnnonce(Long idAnnonce) {
		this.idAnnonce = idAnnonce;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public boolean isAscenceur() {
		return ascenceur;
	}

	public void setAscenceur(boolean ascenceur) {
		this.ascenceur = ascenceur;
	}

	public int getEtage() {
		return etage;
	}

	public void setEtage(int etage) {
		this.etage = etage;
	}

	public Long getSurface() {
		return surface;
	}

	public void setSurface(Long surface) {
		this.surface = surface;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public AnnonceCategoty getCategory() {
		return category;
	}

	public void setCategory(AnnonceCategoty category) {
		this.category = category;
	}

	public AnnonceType getType() {
		return type;
	}

	public void setType(AnnonceType type) {
		this.type = type;
	}

	public UserRole getUser() {
		return userRole;
	}

	public void setUser(UserRole userAdmin) {
		this.userRole = userAdmin;
	}
	
}
