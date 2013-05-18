package de.stevenschwenke.java.javafx.workLifeBalance.server.newTimeRecord;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.newTimeRecord.NewTimeRecordDao;

/**
 * The data access object for the application.
 * 
 * @author Steven Schwenke
 * 
 */
public class DAO implements NewTimeRecordDao {
	private static Logger log = LogManager.getLogger(DAO.class.getName());

	private List<TimeRecord> timeRecords;

	public DAO() {
		super();
		timeRecords = new ArrayList<TimeRecord>();
	}

	public ObservableList<Aspect> getAllAspects() {

		ObservableList<Aspect> observableArrayList = FXCollections
				.observableArrayList(Aspect.values());

		return observableArrayList;
	}

	public void addNewTimeRecord(TimeRecord newRecord) {
		log.debug("addNewTimeRecord: " + newRecord);
		timeRecords.add(newRecord);
	}

	public double calculateCareer() {
		double result = 0;
		for (TimeRecord r : timeRecords) {
			if (r.getAspect().equals(Aspect.CAREER)) {
				result += r.getHours();
			}
		}
		return result;
	}

	public double calculateFamily() {
		double result = 0;
		for (TimeRecord r : timeRecords) {
			if (r.getAspect().equals(Aspect.FAMILY)) {
				result += r.getHours();
			}
		}
		return result;
	}

	public double calculateHealth() {
		double result = 0;
		for (TimeRecord r : timeRecords) {
			if (r.getAspect().equals(Aspect.HEALTH)) {
				result += r.getHours();
			}
		}
		return result;
	}

	public double calculateYou() {
		double result = 0;
		for (TimeRecord r : timeRecords) {
			if (r.getAspect().equals(Aspect.YOU)) {
				result += r.getHours();
			}
		}
		return result;
	}

	public Long calculateOverallpoints() {

		long career = 0;
		long family = 0;
		long health = 0;
		long you = 0;

		for (TimeRecord tr : timeRecords) {
			if (tr.getAspect().equals(Aspect.CAREER)) {
				career += tr.getHours();
			}
			if (tr.getAspect().equals(Aspect.FAMILY)) {
				family += tr.getHours();
			}
			if (tr.getAspect().equals(Aspect.HEALTH)) {
				health += tr.getHours();
			}
			if (tr.getAspect().equals(Aspect.YOU)) {
				you += tr.getHours();
			}
		}

		if (career + family + health + you == 0) {
			return 0L;
		}

		if (career == family && career == health && career == you) {
			return 100L;
		}

		return 0L;
	}
}
