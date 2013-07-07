package de.stevenschwenke.java.javafx.workLifeBalance.client;

/**
 * This class wrapps information needed for storing a {@link TimeRecord} into
 * the database. For further details, see where this class is used.
 * 
 * @author Steven Schwenke
 * 
 */
public class TimeRecordInsertWrapper {

	public Long id;
	public String aspect;
	public int hours;
	public long dayRecordId;

	public TimeRecordInsertWrapper(Aspect aspect, int hours, long dayRecordId) {
		super();
		this.aspect = aspect.name();
		this.hours = hours;
		this.dayRecordId = dayRecordId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAspect() {
		return aspect;
	}

	public void setAspect(String aspect) {
		this.aspect = aspect;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public long getDayRecordId() {
		return dayRecordId;
	}

	public void setDayRecordId(long dayRecordId) {
		this.dayRecordId = dayRecordId;
	}

}
