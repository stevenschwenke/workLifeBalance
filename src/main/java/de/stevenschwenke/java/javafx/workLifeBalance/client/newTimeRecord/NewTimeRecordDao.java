package de.stevenschwenke.java.javafx.workLifeBalance.client.newTimeRecord;

import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;

/**
 * The data access object for creating a new {@link DayRecord}.
 * 
 * @author Steven Schwenke
 * 
 */
public interface NewTimeRecordDao {

	/**
	 * Writes a new or existing {@link DayRecord} to the database.
	 * 
	 * @param newRecord
	 * @return id of the record
	 */
	public int insertDayRecord(DayRecord newRecord);

	public double calculateCareer();

	public double calculateFamily();

	public double calculateHealth();

	public double calculateYou();

	/**
	 * @return overall points of the player.
	 */
	public Long calculateOverallpoints();

}
