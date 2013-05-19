package de.stevenschwenke.java.javafx.workLifeBalance.server.newTimeRecord;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;
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

	private List<DayRecord> dayRecords;

	public DAO() {
		super();
		dayRecords = new ArrayList<DayRecord>();
	}

	public void addNewDayRecord(DayRecord newRecord) {
		log.debug("addNewDayRecord: " + newRecord);
		dayRecords.add(newRecord);
	}

	public double calculateCareer() {
		double result = 0;
		for (DayRecord dr : dayRecords) {
			for (TimeRecord r : dr.getTimeRecordsToday()) {
				if (r.getAspect().equals(Aspect.CAREER)) {
					result += r.getHours();
				}
			}
		}
		return result;
	}

	public double calculateFamily() {
		double result = 0;
		for (DayRecord dr : dayRecords) {
			for (TimeRecord r : dr.getTimeRecordsToday()) {
				if (r.getAspect().equals(Aspect.FAMILY)) {
					result += r.getHours();
				}
			}
		}
		return result;
	}

	public double calculateHealth() {
		double result = 0;
		for (DayRecord dr : dayRecords) {
			for (TimeRecord r : dr.getTimeRecordsToday()) {
				if (r.getAspect().equals(Aspect.HEALTH)) {
					result += r.getHours();
				}
			}
		}
		return result;
	}

	public double calculateYou() {
		double result = 0;
		for (DayRecord dr : dayRecords) {
			for (TimeRecord r : dr.getTimeRecordsToday()) {
				if (r.getAspect().equals(Aspect.YOU)) {
					result += r.getHours();
				}
			}
		}
		return result;
	}

	public Long calculateOverallpoints(DayRecord dayRecord) {
		if (dayRecord.getTimeRecordsToday().isEmpty())
			return 0L;
		return 100L - calculateMalus(dayRecord) + calculateBonus(dayRecord);
	}

	/**
	 * Calculates the biggest <b>relative</b> deviation in time between the
	 * {@TimeRecord}s of a given {@link DayRecord}. For example the
	 * biggest deviation between the hours (1,1,1,9) is (9-1)/12 = 0.66. Hence,
	 * the deviation calculated by this method is always greater equal 0 and
	 * less equal 1.
	 * 
	 * @param dayRecord
	 * @return biggest deviation
	 */
	Double calculateBiggestRelativeDeviation(DayRecord dayRecord) {
		int total = 0;
		int biggestHour = 0;
		int smallestHour = 25;

		for (TimeRecord tr : dayRecord.getTimeRecordsToday()) {
			total += tr.getHours();

			if (tr.getHours() > biggestHour)
				biggestHour = tr.getHours();

			if (tr.getHours() < smallestHour)
				smallestHour = tr.getHours();
		}

		return ((biggestHour - smallestHour) / (double) total);
	}

	int calculateAmountOfZeros(DayRecord dayRecord) {
		return 0;
	}

	Long calculateMalus(DayRecord dayRecord) {
		return (long) (calculateBiggestRelativeDeviation(dayRecord) * 50 + calculateAmountOfZeros(dayRecord)
				* (50 / 3));
	}

	Long calculateBonus(DayRecord dayRecord) {
		return 0L;
	}

	@Override
	public Long calculateOverallpoints() {

		Long sum = 0L;

		for (DayRecord dr : dayRecords) {
			sum += calculateOverallpoints(dr);
		}
		return sum;
	}
}
