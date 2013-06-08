package de.stevenschwenke.java.javafx.workLifeBalance.client;

/**
 * TODO Comment!
 * 
 * @author Steven Schwenke
 * 
 */
public class TimeRecordInsertWrapper {
	public long id;
	public String aspect;
	public int hours;
	public int dayRecordId;

	private TimeRecordInsertWrapper() {
		super();
	}

	public TimeRecordInsertWrapper(Aspect aspect, int hours, int dayRecordId) {
		super();
		this.aspect = aspect.name();
		this.hours = hours;
		this.dayRecordId = dayRecordId;
	}

}
