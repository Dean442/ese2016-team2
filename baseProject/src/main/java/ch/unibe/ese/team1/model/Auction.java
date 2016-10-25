package ch.unibe.ese.team1.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** Describes an advertisement that users can place and search for. */
@Entity
public class Auction extends Advertisement{
	
	@Column
	@Temporal(TemporalType.DATE)
	private Date endTime;

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}