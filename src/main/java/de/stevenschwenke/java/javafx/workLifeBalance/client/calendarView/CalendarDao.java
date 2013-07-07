package de.stevenschwenke.java.javafx.workLifeBalance.client.calendarView;

import java.util.Date;
import java.util.List;

import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;

/**
 * Data Access Object to load all data for the calendar view.
 * 
 * @author Steven Schwenke
 * 
 */
public interface CalendarDao {

	public List<DayRecord> getAllDayRecords();

	/**
	 * @return earliest day for which a {@link DayRecord} exists.
	 */
	public Date getEarliestDayOfRecord();

	/**
	 * @return last day for which a {@link DayRecord} exists.
	 */
	public Date getLastDayOfRecord();

	/**
	 * Gets a {@link DayRecord} of a given day.
	 * 
	 * @param date
	 *            that the day Record is from
	 * @return record of the day. If no record is found, an exception gets
	 *         thrown.
	 */
	public DayRecord getDayRecord(Date date);

}
