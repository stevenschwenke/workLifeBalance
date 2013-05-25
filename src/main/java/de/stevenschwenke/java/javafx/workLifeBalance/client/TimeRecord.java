package de.stevenschwenke.java.javafx.workLifeBalance.client;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Table;

/**
 * A chunk of time that has been spend in a certain aspect of your life.
 * 
 * @author Steven Schwenke
 * 
 */
@Entity
@DynamicInsert
@DynamicUpdate
@SelectBeforeUpdate
@Table(appliesTo = "TIME_RECORD")
public class TimeRecord {

	private Long id;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Aspect aspect;

	private int hours;

	public TimeRecord(Aspect aspect, int hours) {
		super();
		this.aspect = aspect;
		this.hours = hours;
	}

	public Aspect getAspect() {
		return aspect;
	}

	@Column(name = "HOURS", nullable = false)
	public int getHours() {
		return hours;
	}

	@Override
	public String toString() {
		return "TimeRecord [aspect=" + aspect + ", hours=" + hours + "]";
	}

}
