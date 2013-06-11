package de.stevenschwenke.java.javafx.workLifeBalance.client.newTimeRecord;

import java.util.Date;

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
	public long insertDayRecord(DayRecord newRecord);

	/**
	 * Checks if there is a {@link DayRecord} for a specific day.
	 * 
	 * @param date
	 *            for which to check
	 * @return true if there is a DayRecord else false
	 */
	public boolean dayRecordExists(Date date);

	/**
	 * Gets a {@link DayRecord} of a given day.
	 * 
	 * @param date
	 *            that the day Record is from
	 * @return record of the day. If no record is found, an exception gets
	 *         thrown.
	 */
	public DayRecord getDayRecord(Date date);

	public double calculateCareer();

	public double calculateFamily();

	public double calculateHealth();

	public double calculateYou();

	/**
	 * @return overall points of the player.
	 */
	public Long calculateOverallpoints();

}
