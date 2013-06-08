package de.stevenschwenke.java.javafx.workLifeBalance.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Holds all the data for one day.
 * 
 * @author Steven Schwenke
 * 
 */
public class DayRecord {
	private Long id;

	private Date date;

	private List<TimeRecord> timeRecordsToday = new ArrayList<TimeRecord>(4);

	private DayRecord() {
		super();
	}

	public DayRecord(Date date) {
		super();
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<TimeRecord> getTimeRecordsToday() {
		return timeRecordsToday;
	}

	public void setTimeRecordsToday(List<TimeRecord> timeRecordsToday) {
		this.timeRecordsToday = timeRecordsToday;
	}

	public void addTimeRecord(TimeRecord newRecord) {
		timeRecordsToday.add(newRecord);
	}

}
