package de.stevenschwenke.java.javafx.workLifeBalance.client;


/**
 * A chunk of time that has been spend in a certain aspect of your life.
 * 
 * @author Steven Schwenke
 * 
 */
public class TimeRecord {

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Aspect aspect;

	private int hours;

	private TimeRecord() {
		super();
	}

	public TimeRecord(Aspect aspect, int hours) {
		super();
		this.aspect = aspect;
		this.hours = hours;
	}

	public Aspect getAspect() {
		return aspect;
	}

	public int getHours() {
		return hours;
	}

	@Override
	public String toString() {
		return "TimeRecord [aspect=" + aspect + ", hours=" + hours + "]";
	}
}
