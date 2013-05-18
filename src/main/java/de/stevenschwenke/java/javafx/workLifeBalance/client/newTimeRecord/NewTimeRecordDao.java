package de.stevenschwenke.java.javafx.workLifeBalance.client.newTimeRecord;

import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;

/**
 * TODO Comment!
 * 
 * @author Steven Schwenke
 * 
 */
public interface NewTimeRecordDao {

	public void addNewDayRecord(DayRecord newRecord);

	public double calculateCareer();

	public double calculateFamily();

	public double calculateHealth();

	public double calculateYou();

	/**
	 * @return overall points of the player
	 */
	public Long calculateOverallpoints();

}
