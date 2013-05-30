package de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

	private List<DayRecord> dayRecords;

	public MyBatisDao() {
		super();
		dayRecords = new ArrayList<DayRecord>();
		try {
			doStuffWithHSQLDB();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doStuffWithHSQLDB() throws ClassNotFoundException,
			SQLException {
		String database = "mem:mydb";
		String sql = "create table Bookmarks (title char(100), url char(100))";
		String sql1 = "insert into Bookmarks values ('Titel', 'URL')";
		String sql2 = "SELECT title, url FROM Bookmarks ORDER BY title";
		Connection connection;

		System.out.println("TestDemobase\n");

		Class.forName("org.hsqldb.jdbcDriver");
		connection = DriverManager.getConnection("jdbc:hsqldb:" + database,
				"sa", "");

		Statement statement = null;
		ResultSet resultSet = null;

		statement = connection.createStatement();
		resultSet = statement.executeQuery(sql);
		resultSet = statement.executeQuery(sql1);
		resultSet = statement.executeQuery(sql2);

		while (resultSet.next()) {
			System.out.println(resultSet.getString("title") + " ("
					+ resultSet.getString("url") + ")");

		}

		resultSet.close();
		statement.close();
		connection.close();
	}

	@Override
	public TimeRecord readTimeRecord(int id) {

		try {
			String resource = "de/stevenschwenke/java/javafx/workLifeBalance/client/data/mybatis-config.xml";
			InputStream inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
					.build(inputStream);

			SqlSession session = sqlSessionFactory.openSession();
			try {
				return session
						.selectOne(
								"de.stevenschwenke.java.javafx.workLifeBalance.client.data.TimeRecordMapper.selectTimeRecord",
								id);
			} finally {
				session.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// TODO Implement this function with TDD and MyBatis
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

		DayRecord totalRecord = sumUpRecords(dayRecords);
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
		return dayRecords;
	}
}
