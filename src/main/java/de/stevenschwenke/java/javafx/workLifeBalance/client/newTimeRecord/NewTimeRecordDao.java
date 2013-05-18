package de.stevenschwenke.java.javafx.workLifeBalance.client.newTimeRecord;

import javafx.collections.ObservableList;
import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;

/**
 * TODO Comment!
 * 
 * @author Steven Schwenke
 * 
 */
public interface NewTimeRecordDao {

	public void addNewTimeRecord(TimeRecord newRecord);

	public ObservableList<Aspect> getAllAspects();

	public double calculateCareer();

	public double calculateFamily();

	public double calculateHealth();

	public double calculateYou();

}
