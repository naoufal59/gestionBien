package org.aouatif.gestionBien.entites;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class statistiqueData {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long statisticId;
	private Long visitsCount;
	private Long annoncevisitCount;
	
	public statistiqueData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public statistiqueData(long i, long j, long k) {
		super();
		this.statisticId = i;
		this.visitsCount = j;
		this.annoncevisitCount = k;
	}

	public Long getStatisticId() {
		return statisticId;
	}

	public void setStatisticId(Long statisticId) {
		this.statisticId = statisticId;
	}

	public Long getVisitsCount() {
		return visitsCount;
	}

	public void setVisitsCount(Long visitsCount) {
		this.visitsCount = visitsCount;
	}

	public Long getAnnounceVisitCount() {
		return annoncevisitCount;
	}

	public void setAnnounceVisitCount(Long announceVisitCount) {
		this.annoncevisitCount = announceVisitCount;
	}
	
}
