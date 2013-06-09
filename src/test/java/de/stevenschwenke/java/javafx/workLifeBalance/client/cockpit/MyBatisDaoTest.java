package de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import liquibase.Liquibase;
import liquibase.database.jvm.HsqlConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;

/**
 * Test class for {@link MyBatisDao}.
 * 
 * @author Steven Schwenke
 * 
 */
public class MyBatisDaoTest {

	private static Logger log = LogManager.getLogger(MyBatisDaoTest.class
			.getName());

	/** path to test config for the in-memory database */
	private static final String PATH_TO_TEST_CONFIG = "de/stevenschwenke/java/javafx/workLifeBalance/client/data/mybatis-test-config.xml";

	private MyBatisDao dao;

	/** Connection to the in-memory database */
	private Connection connection;

	private Liquibase liquibase;

	@Before
	public void setup() {

		createInMemoryDatabase();

		dao = new MyBatisDao(PATH_TO_TEST_CONFIG);
		BasicConfigurator.configure();
	}

	private void createInMemoryDatabase() {

		String changelog = "changelog.sql";

		try {

			Class.forName("org.hsqldb.jdbcDriver");

			connection = DriverManager.getConnection("jdbc:hsqldb:mem:mydb",
					"sa", "");
			HsqlConnection hsqlConnection = new HsqlConnection(connection);

			liquibase = new Liquibase(changelog,
					new ClassLoaderResourceAccessor(), hsqlConnection);

			liquibase.update(null);

		} catch (SQLException | LiquibaseException | ClassNotFoundException e) {
			fail();
			e.printStackTrace();
		}
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
		statement
				.executeUpdate("insert into DAY_RECORDS (date) values (CURDATE())");
		statement
				.executeUpdate("insert into TIME_RECORDS (hours, day_record_id, aspect) values (42, 0,'CAREER')");
		statement
				.executeUpdate("insert into TIME_RECORDS (hours, day_record_id, aspect) values (45, 0, 'CAREER')");
		connection.commit();
		List<TimeRecord> timeRecords = dao.readTimeRecordOfDayRecord(0L);

		assertEquals(2, timeRecords.size());
		assertEquals(42, timeRecords.get(0).getHours());
		assertEquals(45, timeRecords.get(1).getHours());
	}

	// DAY RECORDS
	@Test
	public void readDayRecordFromEmptyDatabaseIsNull() {
		assertEquals(0, dao.getAllDayRecords().size());
	}

	@Test
	public void readTheOnlyExistingDayRecordFromDatabase() throws SQLException {
		Statement statement;
		statement = connection.createStatement();

		statement
				.executeUpdate("insert into DAY_RECORDS (date) values (CURDATE())");
		statement
				.executeUpdate("insert into TIME_RECORDS (hours, day_record_id, aspect) values (42, 0, 'CAREER')");
		connection.commit();

		List<DayRecord> allDayRecords = dao.getAllDayRecords();
		assertNotNull(allDayRecords);
		assertEquals(1, allDayRecords.size());
		assertEquals(1, allDayRecords.get(0).getTimeRecordsToday().size());
		assertEquals(new Long(0), allDayRecords.get(0).getTimeRecordsToday()
				.get(0).getId());

	}

	@Test
	public void writeOneNewDayRecordWithoutATimeRecord() throws SQLException {
		DayRecord newRecord = new DayRecord(new Date());
		dao.insertDayRecord(newRecord);

		Statement statement;
		statement = connection.createStatement();

		ResultSet resultSet = statement
				.executeQuery("select * from DAY_RECORDS");

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
		ResultSet resultSet = statement
				.executeQuery("select * from DAY_RECORDS");

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
		statement
				.executeUpdate("insert into DAY_RECORDS (date) values (CURDATE())");
		statement
				.executeUpdate("insert into TIME_RECORDS (hours, day_record_id, aspect) values (42, 0, 'CAREER')");

		// Record #2
		statement
				.executeUpdate("insert into DAY_RECORDS (date) values (CURDATE())");
		statement
				.executeUpdate("insert into TIME_RECORDS (hours, day_record_id, aspect) values (43, 1,'FAMILY')");

		// Record #3
		statement
				.executeUpdate("insert into DAY_RECORDS ( date) values (CURDATE())");
		statement
				.executeUpdate("insert into TIME_RECORDS (hours, day_record_id, aspect) values (44, 2, 'HEALTH')");
		connection.commit();

		List<DayRecord> allDayRecords = dao.getAllDayRecords();
		assertNotNull(allDayRecords);
		assertEquals(3, allDayRecords.size());

		// Record #1
		DayRecord dayRecord1 = allDayRecords.get(0);
		assertEquals(1, dayRecord1.getTimeRecordsToday().size());
		assertEquals(new Long(0), dayRecord1.getTimeRecordsToday().get(0)
				.getId());
		assertEquals(Aspect.CAREER, dayRecord1.getTimeRecordsToday().get(0)
				.getAspect());
		assertEquals(42, dayRecord1.getTimeRecordsToday().get(0).getHours());

		// Record #2
		DayRecord dayRecord2 = allDayRecords.get(1);
		assertEquals(1, dayRecord2.getTimeRecordsToday().size());
		assertEquals(new Long(1), dayRecord2.getTimeRecordsToday().get(0)
				.getId());
		assertEquals(Aspect.FAMILY, dayRecord2.getTimeRecordsToday().get(0)
				.getAspect());
		assertEquals(43, dayRecord2.getTimeRecordsToday().get(0).getHours());

		// Record #3
		DayRecord dayRecord3 = allDayRecords.get(2);
		assertEquals(1, dayRecord3.getTimeRecordsToday().size());
		assertEquals(new Long(2), dayRecord3.getTimeRecordsToday().get(0)
				.getId());
		assertEquals(Aspect.HEALTH, dayRecord3.getTimeRecordsToday().get(0)
				.getAspect());
		assertEquals(44, dayRecord3.getTimeRecordsToday().get(0).getHours());
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
		MyBatisDao dao = spy(new MyBatisDao(PATH_TO_TEST_CONFIG));

		doReturn((double) 0).when(dao).calculateBiggestRelativeDeviation(
				any(DayRecord.class));

		assertEquals(new Long(0), dao.calculateMalus(new DayRecord(new Date())));
	}

	@Test
	public void calculationOfMalusOfOne() {
		MyBatisDao dao = spy(new MyBatisDao(PATH_TO_TEST_CONFIG));

		doReturn(1d).when(dao).calculateBiggestRelativeDeviation(
				any(DayRecord.class));

		assertEquals(new Long(100),
				dao.calculateMalus(new DayRecord(new Date())));
	}

	@Test
	public void calculationOfMalusOfX() {
		MyBatisDao dao = spy(new MyBatisDao(PATH_TO_TEST_CONFIG));

		doReturn((double) 0.5).when(dao).calculateBiggestRelativeDeviation(
				any(DayRecord.class));

		assertEquals(new Long(50),
				dao.calculateMalus(new DayRecord(new Date())));
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
		assertEquals(new Long(0),
				dao.calculateOverallpoints(new DayRecord(new Date())));
	}

	@Test
	public void noMalusAndNoBonusResultsIn100Points() {
		MyBatisDao mock = spy(new MyBatisDao(PATH_TO_TEST_CONFIG));
		when(mock.calculateBonus(any(DayRecord.class))).thenReturn(0L);
		doReturn(0L).when(mock).calculateMalus(any(DayRecord.class));

		DayRecord fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero = new DayRecord(
				new Date());
		fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero
				.addTimeRecord(new TimeRecord(Aspect.CAREER, 1));
		assertEquals(
				new Long(100),
				mock.calculateOverallpoints(fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero));
	}

	@Test
	public void malusOf100ResultsIn0PointsTotal() {
		MyBatisDao mock = spy(new MyBatisDao(PATH_TO_TEST_CONFIG));
		when(mock.calculateBonus(any(DayRecord.class))).thenReturn(0L);
		doReturn(100L).when(mock).calculateMalus(any(DayRecord.class));

		assertEquals(new Long(0),
				mock.calculateOverallpoints(new DayRecord(new Date())));
	}

	@Test
	public void malusOf50ResultsIn0PointsTotal() {
		MyBatisDao mock = spy(new MyBatisDao(PATH_TO_TEST_CONFIG));
		when(mock.calculateBonus(any(DayRecord.class))).thenReturn(0L);
		doReturn(50L).when(mock).calculateMalus(any(DayRecord.class));

		DayRecord fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero = new DayRecord(
				new Date());
		fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero
				.addTimeRecord(new TimeRecord(Aspect.CAREER, 1));
		assertEquals(
				new Long(50),
				mock.calculateOverallpoints(fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero));
	}

}
