package de.stevenschwenke.java.javafx.workLifeBalance.client.calendarView;

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

}
