package de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import liquibase.Liquibase;
import liquibase.exception.LiquibaseException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.data.TestDatabaseFacade;

/**
 * Test class for {@link MyBatisDao}.
 * 
 * @author Steven Schwenke
 * 
 */
public class MyBatisDaoTest {

	private static Logger log = LogManager.getLogger(MyBatisDaoTest.class.getName());

	private MyBatisDao dao;

	private TestDatabaseFacade testDatabaseFacade;

	/** Connection to the in-memory database */
	private Connection connection;

	private Liquibase liquibase;

	@Before
	public void setup() {

		testDatabaseFacade = new TestDatabaseFacade();
		testDatabaseFacade.createInMemoryDatabase();
		dao = testDatabaseFacade.createDao();
		connection = testDatabaseFacade.getConnection();
		liquibase = testDatabaseFacade.getLiquibase();
		BasicConfigurator.configure();
	}

	@After
	public void teardown() {
		try {
			liquibase.dropAll();
			connection.close();
		} catch (SQLException | LiquibaseException e) {
			log.error("Error closing the connection: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// TIME RECORDS
	@Test
	public void readTimeRecordByDayRecordFromEmptyDatabase() {
		assertTrue(dao.readTimeRecordOfDayRecord(0L).isEmpty());
	}

	@Test
	public void readTimeRecordByDayRecord() throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		// Record #1
		statement.executeUpdate("insert into DAY_RECORDS (date) values (CURDATE())");
		statement.executeUpdate("insert into TIME_RECORDS (hours, day_record_id, aspect) values (42, 0,'CAREER')");
		statement.executeUpdate("insert into TIME_RECORDS (hours, day_record_id, aspect) values (45, 0, 'CAREER')");
		connection.commit();
		List<TimeRecord> timeRecords = dao.readTimeRecordOfDayRecord(0L);

		assertEquals(2, timeRecords.size());
		assertEquals(42, timeRecords.get(0).getHours());
		assertEquals(45, timeRecords.get(1).getHours());
	}

	// DAY RECORDS

	@Test
	public void checkIfDayRecordExistsTrue() throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		statement.executeUpdate("insert into DAY_RECORDS (date) values ('2013-01-01')");
		connection.commit();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2013);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		assertTrue(dao.dayRecordExists(cal.getTime()));
	}

	@Test
	public void checkIfDayRecordExistsFalse() throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		statement.executeUpdate("insert into DAY_RECORDS (date) values ('2013-01-01')");
		connection.commit();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2017);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		assertFalse(dao.dayRecordExists(cal.getTime()));
	}

	@Test
	public void getADayRecordOfASpecificDate() throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		statement.executeUpdate("insert into DAY_RECORDS (date) values ('2012-01-01')");
		statement.executeUpdate("insert into DAY_RECORDS (date) values ('2013-01-01')");
		statement.executeUpdate("insert into DAY_RECORDS (date) values ('2014-01-01')");
		connection.commit();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2013);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		DayRecord dayRecord = dao.getDayRecord(cal.getTime());
		assertNotNull(dayRecord);
		assertEquals(cal.getTime(), dayRecord.getDate());
	}

	@Test
	public void getADayRecordReturnsNullIfThereIsNoDayRecordForTheSpecifiedDate() throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		statement.executeUpdate("insert into DAY_RECORDS (date) values ('2012-01-01')");
		connection.commit();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2013);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		DayRecord dayRecord = dao.getDayRecord(cal.getTime());
		assertNull(dayRecord);
	}

	@Test
	public void readDayRecordFromEmptyDatabaseIsNull() {
		assertEquals(0, dao.getAllDayRecords().size());
	}

	@Test
	public void readTheOnlyExistingDayRecordFromDatabase() throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		statement.executeUpdate("insert into DAY_RECORDS (date) values (CURDATE())");
		statement.executeUpdate("insert into TIME_RECORDS (hours, day_record_id, aspect) values (42, 0, 'CAREER')");
		connection.commit();

		List<DayRecord> allDayRecords = dao.getAllDayRecords();
		assertNotNull(allDayRecords);
		assertEquals(1, allDayRecords.size());
		assertEquals(1, allDayRecords.get(0).getTimeRecordsToday().size());
		assertEquals(new Long(0), allDayRecords.get(0).getTimeRecordsToday().get(0).getId());

	}

	@Test
	public void writeOneNewDayRecordWithoutATimeRecord() throws SQLException {
		DayRecord newRecord = new DayRecord(new Date());
		dao.insertDayRecord(newRecord);

		Statement statement;
		statement = connection.createStatement();

		ResultSet resultSet = statement.executeQuery("select * from DAY_RECORDS");

		int numberOfRows = 0;
		while (resultSet.next()) {
			numberOfRows++;
			assertEquals(0, resultSet.getInt("id"));
		}

		assertEquals(1, numberOfRows);
	}

	@Test
	public void writeOneNewDayRecordWithTwoTimeRecords() throws SQLException {
		DayRecord newRecord = new DayRecord(new Date());
		TimeRecord tr1 = new TimeRecord(Aspect.CAREER, 42);
		newRecord.addTimeRecord(tr1);
		TimeRecord tr2 = new TimeRecord(Aspect.CAREER, 50);
		newRecord.addTimeRecord(tr2);

		long dayRecordId = dao.insertDayRecord(newRecord);

		// DayRecord saved properly?
		assertEquals(0, dayRecordId);
		Statement statement;
		statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from DAY_RECORDS");

		int numberOfRows = 0;
		while (resultSet.next()) {
			numberOfRows++;
			assertEquals(0, resultSet.getInt("id"));
		}
		assertEquals(1, numberOfRows);

		// TimeRecord saved properly?
		statement = connection.createStatement();
		resultSet = statement.executeQuery("select * from TIME_RECORDS");

		numberOfRows = 0;
		while (resultSet.next()) {
			numberOfRows++;
			if (numberOfRows == 1) {
				assertEquals(0, resultSet.getInt("id"));
				assertEquals(42, resultSet.getInt("hours"));
			}
			if (numberOfRows == 2) {
				assertEquals(1, resultSet.getInt("id"));
				assertEquals(50, resultSet.getInt("hours"));
			}
		}
		assertEquals(2, numberOfRows);
	}

	@Test
	public void readThreeDayRecordsFromDatabase() throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		// Record #1
		statement.executeUpdate("insert into DAY_RECORDS (date) values ('2013-01-01')");
		statement.executeUpdate("insert into TIME_RECORDS (hours, day_record_id, aspect) values (42, 0, 'CAREER')");

		// Record #2
		statement.executeUpdate("insert into DAY_RECORDS (date) values ('2013-01-02')");
		statement.executeUpdate("insert into TIME_RECORDS (hours, day_record_id, aspect) values (43, 1,'FAMILY')");

		// Record #3
		statement.executeUpdate("insert into DAY_RECORDS ( date) values ('2013-01-03')");
		statement.executeUpdate("insert into TIME_RECORDS (hours, day_record_id, aspect) values (44, 2, 'HEALTH')");
		connection.commit();

		List<DayRecord> allDayRecords = dao.getAllDayRecords();
		assertNotNull(allDayRecords);
		assertEquals(3, allDayRecords.size());

		// Record #1
		DayRecord dayRecord1 = allDayRecords.get(0);
		assertEquals(1, dayRecord1.getTimeRecordsToday().size());
		assertEquals(new Long(0), dayRecord1.getTimeRecordsToday().get(0).getId());
		assertEquals(Aspect.CAREER, dayRecord1.getTimeRecordsToday().get(0).getAspect());
		assertEquals(42, dayRecord1.getTimeRecordsToday().get(0).getHours());

		// Record #2
		DayRecord dayRecord2 = allDayRecords.get(1);
		assertEquals(1, dayRecord2.getTimeRecordsToday().size());
		assertEquals(new Long(1), dayRecord2.getTimeRecordsToday().get(0).getId());
		assertEquals(Aspect.FAMILY, dayRecord2.getTimeRecordsToday().get(0).getAspect());
		assertEquals(43, dayRecord2.getTimeRecordsToday().get(0).getHours());

		// Record #3
		DayRecord dayRecord3 = allDayRecords.get(2);
		assertEquals(1, dayRecord3.getTimeRecordsToday().size());
		assertEquals(new Long(2), dayRecord3.getTimeRecordsToday().get(0).getId());
		assertEquals(Aspect.HEALTH, dayRecord3.getTimeRecordsToday().get(0).getAspect());
		assertEquals(44, dayRecord3.getTimeRecordsToday().get(0).getHours());
	}

	@Test
	public void dayRecordsAreReadInAscendingTimeOrder() throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		// Record #1
		statement.executeUpdate("insert into DAY_RECORDS (date) values ('2011-01-01')");

		statement.executeUpdate("insert into DAY_RECORDS (date) values ('2012-01-01')");

		statement.executeUpdate("insert into DAY_RECORDS (date) values ('2010-01-01')");

		statement.executeUpdate("insert into DAY_RECORDS (date) values ('2013-01-02')");

		statement.executeUpdate("insert into DAY_RECORDS (date) values ('2011-02-01')");

		connection.commit();

		List<DayRecord> allDayRecords = dao.getAllDayRecords();
		assertNotNull(allDayRecords);
		assertEquals(5, allDayRecords.size());

		DayRecord dayRecord1 = allDayRecords.get(0);
		assertEquals("2010", new SimpleDateFormat("yyyy").format(dayRecord1.getDate()));

		DayRecord dayRecord2 = allDayRecords.get(1);
		assertEquals("2011", new SimpleDateFormat("yyyy").format(dayRecord2.getDate()));
		assertEquals("01", new SimpleDateFormat("MM").format(dayRecord2.getDate()));

		DayRecord dayRecord3 = allDayRecords.get(2);
		assertEquals("2011", new SimpleDateFormat("yyyy").format(dayRecord3.getDate()));
		assertEquals("02", new SimpleDateFormat("MM").format(dayRecord3.getDate()));

		DayRecord dayRecord4 = allDayRecords.get(3);
		assertEquals("2012", new SimpleDateFormat("yyyy").format(dayRecord4.getDate()));

		DayRecord dayRecord5 = allDayRecords.get(4);
		assertEquals("2013", new SimpleDateFormat("yyyy").format(dayRecord5.getDate()));

	}

	// //////////////////////////////////////////////////////////
	// Computing of Biggest relative deviation
	// //////////////////////////////////////////////////////////

	@Test
	public void recognizeBiggestDeviationOfZero() {
		DayRecord dr = new DayRecord(new Date());
		dr.addTimeRecord(new TimeRecord(Aspect.CAREER, 3));
		dr.addTimeRecord(new TimeRecord(Aspect.FAMILY, 3));
		dr.addTimeRecord(new TimeRecord(Aspect.HEALTH, 3));
		dr.addTimeRecord(new TimeRecord(Aspect.YOU, 3));

		assertEquals(new Double(0), dao.calculateBiggestRelativeDeviation(dr));
	}

	@Test
	public void recognizeBiggestDeviationOfOne() {
		DayRecord dr = new DayRecord(new Date());
		dr.addTimeRecord(new TimeRecord(Aspect.CAREER, 8));
		dr.addTimeRecord(new TimeRecord(Aspect.FAMILY, 0));
		dr.addTimeRecord(new TimeRecord(Aspect.HEALTH, 0));
		dr.addTimeRecord(new TimeRecord(Aspect.YOU, 0));

		assertEquals(new Double(1), dao.calculateBiggestRelativeDeviation(dr));
	}

	@Test
	public void recognizeBiggestDeviationOfX() {
		DayRecord dr = new DayRecord(new Date());
		dr.addTimeRecord(new TimeRecord(Aspect.CAREER, 2));
		dr.addTimeRecord(new TimeRecord(Aspect.FAMILY, 2));
		dr.addTimeRecord(new TimeRecord(Aspect.HEALTH, 2));
		dr.addTimeRecord(new TimeRecord(Aspect.YOU, 8));

		double expected = (8 - 2) / (double) (2 + 2 + 2 + 8);

		assertEquals(expected, dao.calculateBiggestRelativeDeviation(dr), 0.005);
	}

	// //////////////////////////////////////////////////////////
	// Computing of amount of time records with zero hours
	// //////////////////////////////////////////////////////////

	// TODO

	// //////////////////////////////////////////////////////////
	// Computing of malus of a day record
	// //////////////////////////////////////////////////////////

	@Test
	public void calculationOfMalusOfZero() {
		MyBatisDao dao = spy(new MyBatisDao(testDatabaseFacade.getPathToTestConfig()));

		doReturn((double) 0).when(dao).calculateBiggestRelativeDeviation(any(DayRecord.class));

		assertEquals(new Long(0), dao.calculateMalus(new DayRecord(new Date())));
	}

	@Test
	public void calculationOfMalusOfOne() {
		MyBatisDao dao = spy(new MyBatisDao(testDatabaseFacade.getPathToTestConfig()));

		doReturn(1d).when(dao).calculateBiggestRelativeDeviation(any(DayRecord.class));

		assertEquals(new Long(100), dao.calculateMalus(new DayRecord(new Date())));
	}

	@Test
	public void calculationOfMalusOfX() {
		MyBatisDao dao = spy(new MyBatisDao(testDatabaseFacade.getPathToTestConfig()));

		doReturn((double) 0.5).when(dao).calculateBiggestRelativeDeviation(any(DayRecord.class));

		assertEquals(new Long(50), dao.calculateMalus(new DayRecord(new Date())));
	}

	// //////////////////////////////////////////////////////////
	// Computing of bonus of one DayRecord
	// //////////////////////////////////////////////////////////

	// TODO write some tests when bonuses can be given to a TimeRecord

	// //////////////////////////////////////////////////////////
	// Computing of overall points of one DayRecord using malus and bonus
	// //////////////////////////////////////////////////////////

	@Test
	public void noRecordsResultsIn0Points() {
		assertEquals(new Long(0), dao.calculateOverallpoints(new DayRecord(new Date())));
	}

	@Test
	public void noMalusAndNoBonusResultsIn100Points() {
		MyBatisDao mock = spy(new MyBatisDao(testDatabaseFacade.getPathToTestConfig()));
		when(mock.calculateBonus(any(DayRecord.class))).thenReturn(0L);
		doReturn(0L).when(mock).calculateMalus(any(DayRecord.class));

		DayRecord fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero = new DayRecord(new Date());
		fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero.addTimeRecord(new TimeRecord(Aspect.CAREER, 1));
		assertEquals(new Long(100), mock.calculateOverallpoints(fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero));
	}

	@Test
	public void malusOf100ResultsIn0PointsTotal() {
		MyBatisDao mock = spy(new MyBatisDao(testDatabaseFacade.getPathToTestConfig()));
		when(mock.calculateBonus(any(DayRecord.class))).thenReturn(0L);
		doReturn(100L).when(mock).calculateMalus(any(DayRecord.class));

		assertEquals(new Long(0), mock.calculateOverallpoints(new DayRecord(new Date())));
	}

	@Test
	public void malusOf50ResultsIn0PointsTotal() {
		MyBatisDao mock = spy(new MyBatisDao(testDatabaseFacade.getPathToTestConfig()));
		when(mock.calculateBonus(any(DayRecord.class))).thenReturn(0L);
		doReturn(50L).when(mock).calculateMalus(any(DayRecord.class));

		DayRecord fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero = new DayRecord(new Date());
		fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero.addTimeRecord(new TimeRecord(Aspect.CAREER, 1));
		assertEquals(new Long(50), mock.calculateOverallpoints(fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero));
	}

}
