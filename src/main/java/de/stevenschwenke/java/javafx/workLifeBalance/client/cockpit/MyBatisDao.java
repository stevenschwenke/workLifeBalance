package de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecordInsertWrapper;
import de.stevenschwenke.java.javafx.workLifeBalance.client.calendarView.CalendarDao;
import de.stevenschwenke.java.javafx.workLifeBalance.client.newTimeRecord.NewTimeRecordDao;

/**
 * The data access object for the application.
 * 
 * @author Steven Schwenke
 * 
 */
public class MyBatisDao implements NewTimeRecordDao, CalendarDao, CockpitDao {

	private static Logger log = LogManager
			.getLogger(MyBatisDao.class.getName());

	private SqlSessionFactory sqlSessionFactory;

	public MyBatisDao(String configPath) {

		super();

		try {
			log.debug("Building up connection to database " + configPath);
			InputStream inputStream = Resources.getResourceAsStream(configPath);
			sqlSessionFactory = new SqlSessionFactoryBuilder()
					.build(inputStream);
		} catch (IOException e) {
			log.error("Could not build up connection to database " + configPath
					+ ": " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Reads all {@link TimeRecord}s of a specific {@link DayRecord}.
	 * 
	 * @param dayRecordId
	 *            id of the {@link DayRecord}
	 * @return
	 */
	public List<TimeRecord> readTimeRecordOfDayRecord(Long dayRecordId) {

		SqlSession session = sqlSessionFactory.openSession();
		try {
			List<TimeRecord> resultList = new ArrayList<TimeRecord>();
			List<TimeRecordInsertWrapper> wrapperList = session
					.selectList(
							"de.stevenschwenke.java.javafx.workLifeBalance.client.data.TimeRecordMapper.selectTimeRecordOfDayRecord",
							dayRecordId);
			for (TimeRecordInsertWrapper w : wrapperList) {
				TimeRecord tr = new TimeRecord(Aspect.valueOf(w.aspect),
						w.hours);
				tr.setId(w.id);
				resultList.add(tr);

			}
			return resultList;
		} finally {
			session.close();
		}
	}

	public long insertDayRecord(DayRecord newRecord) {
		SqlSession session = sqlSessionFactory.openSession();
		try {

			session.insert(
					"de.stevenschwenke.java.javafx.workLifeBalance.client.data.DayRecordMapper.insertDayRecord",
					newRecord);
			for (TimeRecord tr : newRecord.getTimeRecordsToday()) {
				TimeRecordInsertWrapper wrapper = new TimeRecordInsertWrapper(
						tr.getAspect(), tr.getHours(), newRecord.getId());
				session.insert(
						"de.stevenschwenke.java.javafx.workLifeBalance.client.data.TimeRecordMapper.insertTimeRecord",
						wrapper);
			}
			return newRecord.getId();
		} finally {
			session.commit();
			session.close();
		}
	}

	public void updateDayRecord(DayRecord existingRecord) {
		SqlSession session = sqlSessionFactory.openSession();
		try {

			for (TimeRecord tr : existingRecord.getTimeRecordsToday()) {

				if (tr.getId() == null) {
					TimeRecordInsertWrapper wrapper = new TimeRecordInsertWrapper(
							tr.getAspect(), tr.getHours(),
							existingRecord.getId());
					session.insert(
							"de.stevenschwenke.java.javafx.workLifeBalance.client.data.TimeRecordMapper.insertTimeRecord",
							wrapper);
				} else {
					TimeRecordInsertWrapper wrapper = new TimeRecordInsertWrapper(
							tr.getAspect(), tr.getHours(),
							existingRecord.getId());
					wrapper.setId(tr.getId());
					session.update(
							"de.stevenschwenke.java.javafx.workLifeBalance.client.data.TimeRecordMapper.updateTimeRecord",
							wrapper);
				}
			}
		} finally {
			session.commit();
			session.close();
		}
	}

	public double calculateCareer() {
		double result = 0;
		for (DayRecord dr : getAllDayRecords()) {
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
		for (DayRecord dr : getAllDayRecords()) {
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
		for (DayRecord dr : getAllDayRecords()) {
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
		for (DayRecord dr : getAllDayRecords()) {
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

	Long calculateMalus(DayRecord dayRecord) {
		return (long) (calculateBiggestRelativeDeviation(dayRecord) * 100);
	}

	Long calculateBonus(DayRecord dayRecord) {
		return 0L;
	}

	@Override
	public Long calculateOverallpoints() {
		// To calculate the overall points, all records have to be aggregated
		// into one record that is then used to calculate the points. Another
		// way would be to sum the points for each record and divide it by the
		// number of records. However, that would result in a strange behavior.

		DayRecord totalRecord = sumUpRecords(getAllDayRecords());
		return calculateOverallpoints(totalRecord);

	}

	/**
	 * Sums the value of all given {@link DayRecord}s into one {@link DayRecord}
	 * .
	 * 
	 * @param dayRecords
	 *            that should be summed up
	 * @return Record with the sum of all given records
	 */
	private DayRecord sumUpRecords(List<DayRecord> dayRecords) {
		int healthTotal = 0;
		int careerTotal = 0;
		int familyTotal = 0;
		int youTotal = 0;

		for (DayRecord dr : dayRecords) {
			for (TimeRecord tr : dr.getTimeRecordsToday()) {
				if (tr.getAspect().equals(Aspect.CAREER))
					careerTotal += tr.getHours();

				if (tr.getAspect().equals(Aspect.FAMILY))
					familyTotal += tr.getHours();

				if (tr.getAspect().equals(Aspect.HEALTH))
					healthTotal += tr.getHours();

				if (tr.getAspect().equals(Aspect.YOU))
					youTotal += tr.getHours();
			}
		}
		DayRecord totalRecord = new DayRecord(new Date());
		totalRecord.addTimeRecord(new TimeRecord(Aspect.HEALTH, healthTotal));
		totalRecord.addTimeRecord(new TimeRecord(Aspect.CAREER, careerTotal));
		totalRecord.addTimeRecord(new TimeRecord(Aspect.FAMILY, familyTotal));
		totalRecord.addTimeRecord(new TimeRecord(Aspect.YOU, youTotal));
		return totalRecord;
	}

	@Override
	public List<DayRecord> getAllDayRecords() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			List<DayRecord> dayRecordsWithoutTimeRecords = session
					.selectList("de.stevenschwenke.java.javafx.workLifeBalance.client.data.DayRecordMapper.selectAllDayRecords");
			for (DayRecord dr : dayRecordsWithoutTimeRecords) {
				List<TimeRecord> timeRecordsOfDayRecord = readTimeRecordOfDayRecord(dr
						.getId());
				dr.setTimeRecordsToday(timeRecordsOfDayRecord);
			}
			return dayRecordsWithoutTimeRecords;
		} finally {
			session.close();
		}
	}

	@Override
	public boolean dayRecordExists(Date date) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			int count = session
					.selectOne(
							"de.stevenschwenke.java.javafx.workLifeBalance.client.data.DayRecordMapper.dayRecordExists",
							date);
			return count > 0;
		} finally {
			session.close();
		}
	}

	@Override
	public DayRecord getDayRecord(Date date) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			DayRecord dr = session
					.selectOne(
							"de.stevenschwenke.java.javafx.workLifeBalance.client.data.DayRecordMapper.selectDayRecordByDate",
							date);
			List<TimeRecord> timeRecordsOfDayRecord = readTimeRecordOfDayRecord(dr
					.getId());
			dr.setTimeRecordsToday(timeRecordsOfDayRecord);
			return dr;
		} finally {
			session.close();
		}
	}
}
