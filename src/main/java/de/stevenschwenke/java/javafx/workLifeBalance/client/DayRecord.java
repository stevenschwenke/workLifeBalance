package de.stevenschwenke.java.javafx.workLifeBalance.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all the data for one day.
 * 
 * @author Steven Schwenke
 * 
 */
public class DayRecord {

	private List<TimeRecord> timeRecordsToday = new ArrayList<TimeRecord>(4);

	public void addTimeRecord(TimeRecord newRecord) {
		timeRecordsToday.add(newRecord);
	}

	public List<TimeRecord> getTimeRecordsToday() {
		return timeRecordsToday;
	}

	public void setTimeRecordsToday(List<TimeRecord> timeRecordsToday) {
		this.timeRecordsToday = timeRecordsToday;
	}

}
