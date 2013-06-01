package de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

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

	private static Logger log = LogManager.getLogger(MyBatisDaoTest.class.getName());

	/** path to test config for the in-memory database */
	private static final String PATH_TO_TEST_CONFIG = "de/stevenschwenke/java/javafx/workLifeBalance/client/data/mybatis-test-config.xml";

	private MyBatisDao dao;

	/** Connection to the in-memory database */
	private Connection connection;

	@Before
	public void setup() {

		createInMemoryDatabase();

		dao = new MyBatisDao(PATH_TO_TEST_CONFIG);
		BasicConfigurator.configure();
	}

	@After
	public void teardown() {
		try {
			connection.rollback();
			connection.close();
		} catch (SQLException e) {
			log.error("Error closing the connection: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Creates the in-memory database for testing
	 */
	private void createInMemoryDatabase() {
		Statement statement = null;
		connection = null;
		try {

			// TODO extract this SQL to some kind of script file to have it at
			// hand when the database has to be set up.

			Class.forName("org.hsqldb.jdbcDriver");

			connection = DriverManager.getConnection("jdbc:hsqldb:mem:mydb",
					"sa", "");

			DatabaseMetaData dbm = connection.getMetaData();
			ResultSet tables = dbm.getTables(null, null, "TIME_RECORDS", null);
			if (!tables.next()) {
				statement = connection.createStatement();
				String sql = "create table TIME_RECORDS (id int, hours int)";
				statement.executeQuery(sql);
			}

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException | NullPointerException e) {
				log.error("Unexpected error while creating the database for testing: "
						+ e.getMessage() + e.getStackTrace());
			}
		}
	}

	@Test
	public void readTimeRecordFromEmptyDatabaseTest() {
		assertNull(dao.readTimeRecord(0));
	}

	@Test
	public void readTimeRecordFromDatabaseWithOnlyThatTimeRecordTest()
			throws SQLException {

		Statement statement = connection.createStatement();
		statement
				.executeQuery("insert into TIME_RECORDS (id, hours) values (0, 42)");

		TimeRecord timeRecord = dao.readTimeRecord(0);
		assertNotNull(timeRecord);
		assertEquals(new Long(0), timeRecord.getId());
		assertEquals(42, timeRecord.getHours());
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
