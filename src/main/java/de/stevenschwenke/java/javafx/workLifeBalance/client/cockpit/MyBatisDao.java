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

	private static Logger log = LogManager.getLogger(MyBatisDao.class.getName());

	private SqlSessionFactory sqlSessionFactory;

	public MyBatisDao(String configPath) {

		super();

		try {
			log.debug("Building up connection to database " + configPath);
			InputStream inputStream = Resources.getResourceAsStream(configPath);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (IOException e) {
			log.error("Could not build up connection to database " + configPath + ": " + e.getMessage());
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
			List<TimeRecordInsertWrapper> wrapperList = session.selectList(
					"de.stevenschwenke.java.javafx.workLifeBalance.client.data.TimeRecordMapper.selectTimeRecordOfDayRecord", dayRecordId);
			for (TimeRecordInsertWrapper w : wrapperList) {
				TimeRecord tr = new TimeRecord(Aspect.valueOf(w.aspect), w.hours);
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

			session.insert("de.stevenschwenke.java.javafx.workLifeBalance.client.data.DayRecordMapper.insertDayRecord", newRecord);
			for (TimeRecord tr : newRecord.getTimeRecordsToday()) {
				TimeRecordInsertWrapper wrapper = new TimeRecordInsertWrapper(tr.getAspect(), tr.getHours(), newRecord.getId());
				session.insert("de.stevenschwenke.java.javafx.workLifeBalance.client.data.TimeRecordMapper.insertTimeRecord", wrapper);
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
					TimeRecordInsertWrapper wrapper = new TimeRecordInsertWrapper(tr.getAspect(), tr.getHours(), existingRecord.getId());
					session.insert("de.stevenschwenke.java.javafx.workLifeBalance.client.data.TimeRecordMapper.insertTimeRecord", wrapper);
				} else {
					TimeRecordInsertWrapper wrapper = new TimeRecordInsertWrapper(tr.getAspect(), tr.getHours(), existingRecord.getId());
					wrapper.setId(tr.getId());
					session.update("de.stevenschwenke.java.javafx.workLifeBalance.client.data.TimeRecordMapper.updateTimeRecord", wrapper);
				}
			}
		} finally {
			session.commit();
			session.close();
		}
	}

	public List<DayRecord> getAllDayRecords() {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			List<DayRecord> dayRecordsWithoutTimeRecords = session
					.selectList("de.stevenschwenke.java.javafx.workLifeBalance.client.data.DayRecordMapper.selectAllDayRecords");
			for (DayRecord dr : dayRecordsWithoutTimeRecords) {
				List<TimeRecord> timeRecordsOfDayRecord = readTimeRecordOfDayRecord(dr.getId());
				dr.setTimeRecordsToday(timeRecordsOfDayRecord);
			}
			return dayRecordsWithoutTimeRecords;
		} finally {
			session.close();
		}
	}

	public boolean dayRecordExists(Date date) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			int count = session
					.selectOne("de.stevenschwenke.java.javafx.workLifeBalance.client.data.DayRecordMapper.dayRecordExists", date);
			return count > 0;
		} finally {
			session.close();
		}
	}

	public DayRecord getDayRecord(Date date) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			DayRecord dr = session.selectOne(
					"de.stevenschwenke.java.javafx.workLifeBalance.client.data.DayRecordMapper.selectDayRecordByDate", date);

			if (dr == null)
				return null;

			List<TimeRecord> timeRecordsOfDayRecord = readTimeRecordOfDayRecord(dr.getId());
			dr.setTimeRecordsToday(timeRecordsOfDayRecord);
			return dr;
		} finally {
			session.close();
		}
	}

	public Date getEarliestDayOfRecord() {
		// TODO optimize this with own SQL

		Date earliestDate = null;
		for (DayRecord dr : getAllDayRecords()) {
			if (earliestDate == null)
				earliestDate = dr.getDate();

			if (dr.getDate().before(earliestDate))
				earliestDate = dr.getDate();
		}
		return earliestDate;
	}

	public Date getLastDayOfRecord() {
		// TODO optimize this with own SQL

		Date lastDate = null;
		for (DayRecord dr : getAllDayRecords()) {
			if (lastDate == null)
				lastDate = dr.getDate();

			if (dr.getDate().after(lastDate))
				lastDate = dr.getDate();
		}
		return lastDate;
	}
}
