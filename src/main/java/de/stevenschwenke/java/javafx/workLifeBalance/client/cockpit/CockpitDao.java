package de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit;

import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;

/**
 * The data access object for the cockpit.
 * 
 * @author Steven Schwenke
 * 
 */
public interface CockpitDao {
	/**
	 * Reads a {@link TimeRecord} with a specific id from the database.
	 * 
	 * @param id
	 *            of the record that should be loaded
	 * @return loaded record or null if no record with the specified id could be
	 *         found
	 */
	public TimeRecord readTimeRecord(int id);
}
