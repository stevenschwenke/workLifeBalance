package de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit;

import java.util.List;

import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;

/**
 * The data access object for the cockpit.
 * 
 * @author Steven Schwenke
 * 
 */
public interface CockpitDao {
	public List<DayRecord> getAllDayRecords();
}
