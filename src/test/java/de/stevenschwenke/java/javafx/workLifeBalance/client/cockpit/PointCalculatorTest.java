package de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

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
 * Tests for {@link PointCalculator}.
 * 
 * @author Steven Schwenke
 * 
 */
public class PointCalculatorTest {

	private static Logger log = LogManager.getLogger(PointCalculatorTest.class.getName());

	private TestDatabaseFacade testDatabaseFacade;

	/** Connection to the in-memory database */
	private Connection connection;

	private Liquibase liquibase;

	@Before
	public void setup() {

		testDatabaseFacade = new TestDatabaseFacade();
		testDatabaseFacade.createInMemoryDatabase();
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

		assertEquals(new Double(0), new PointCalculator(null).calculateBiggestRelativeDeviation(dr));
	}

	@Test
	public void recognizeBiggestDeviationOfOne() {
		DayRecord dr = new DayRecord(new Date());
		dr.addTimeRecord(new TimeRecord(Aspect.CAREER, 8));
		dr.addTimeRecord(new TimeRecord(Aspect.FAMILY, 0));
		dr.addTimeRecord(new TimeRecord(Aspect.HEALTH, 0));
		dr.addTimeRecord(new TimeRecord(Aspect.YOU, 0));

		assertEquals(new Double(1), new PointCalculator(null).calculateBiggestRelativeDeviation(dr));
	}

	@Test
	public void recognizeBiggestDeviationOfX() {
		DayRecord dr = new DayRecord(new Date());
		dr.addTimeRecord(new TimeRecord(Aspect.CAREER, 2));
		dr.addTimeRecord(new TimeRecord(Aspect.FAMILY, 2));
		dr.addTimeRecord(new TimeRecord(Aspect.HEALTH, 2));
		dr.addTimeRecord(new TimeRecord(Aspect.YOU, 8));

		double expected = (8 - 2) / (double) (2 + 2 + 2 + 8);

		assertEquals(expected, new PointCalculator(null).calculateBiggestRelativeDeviation(dr), 0.005);
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
		PointCalculator calc = spy(new PointCalculator(null));

		doReturn((double) 0).when(calc).calculateBiggestRelativeDeviation(any(DayRecord.class));

		assertEquals(new Long(0), calc.calculateMalus(new DayRecord(new Date())));
	}

	@Test
	public void calculationOfMalusOfOne() {
		PointCalculator calc = spy(new PointCalculator(null));

		doReturn(1d).when(calc).calculateBiggestRelativeDeviation(any(DayRecord.class));

		assertEquals(new Long(100), calc.calculateMalus(new DayRecord(new Date())));
	}

	@Test
	public void calculationOfMalusOfX() {
		PointCalculator calc = spy(new PointCalculator(null));

		doReturn((double) 0.5).when(calc).calculateBiggestRelativeDeviation(any(DayRecord.class));

		assertEquals(new Long(50), calc.calculateMalus(new DayRecord(new Date())));
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
		PointCalculator calc = new PointCalculator(null);
		assertEquals(new Long(0), calc.calculateOverallpoints(new DayRecord(new Date())));
	}

	@Test
	public void noMalusAndNoBonusResultsIn100Points() {
		PointCalculator mock = spy(new PointCalculator(null));
		when(mock.calculateBonus(any(DayRecord.class))).thenReturn(0L);
		doReturn(0L).when(mock).calculateMalus(any(DayRecord.class));

		DayRecord fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero = new DayRecord(new Date());
		fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero.addTimeRecord(new TimeRecord(Aspect.CAREER, 1));
		assertEquals(new Long(100), mock.calculateOverallpoints(fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero));
	}

	@Test
	public void malusOf100ResultsIn0PointsTotal() {
		PointCalculator mock = spy(new PointCalculator(null));
		when(mock.calculateBonus(any(DayRecord.class))).thenReturn(0L);
		doReturn(100L).when(mock).calculateMalus(any(DayRecord.class));

		assertEquals(new Long(0), mock.calculateOverallpoints(new DayRecord(new Date())));
	}

	@Test
	public void malusOf50ResultsIn0PointsTotal() {
		PointCalculator mock = spy(new PointCalculator(null));
		when(mock.calculateBonus(any(DayRecord.class))).thenReturn(0L);
		doReturn(50L).when(mock).calculateMalus(any(DayRecord.class));

		DayRecord fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero = new DayRecord(new Date());
		fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero.addTimeRecord(new TimeRecord(Aspect.CAREER, 1));
		assertEquals(new Long(50), mock.calculateOverallpoints(fakeDayRecordWithOneRecordSoTheCalculationWillNotGiveZero));
	}
}
