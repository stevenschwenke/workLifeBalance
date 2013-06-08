package de.stevenschwenke.java.javafx.workLifeBalance.client;

/**
 * TODO Comment!
 * 
 * @author Steven Schwenke
 * 
 */
public class TimeRecordInsertWrapper {
	public int aspect;
	public int hours;
	public int dayRecordId;

	public TimeRecordInsertWrapper(Aspect aspect, int hours, int dayRecordId) {
		super();
		this.aspect = aspect.ordinal();
		this.hours = hours;
		this.dayRecordId = dayRecordId;
	}

}
