package de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit.DAO;

/**
 * Test class for {@link DAO}.
 * 
 * @author Steven Schwenke
 * 
 */
public class DAOTest {
	private DAO dao;

	@Before
	public void setup() {
		dao = new DAO();
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
		DAO dao = spy(new DAO());

		doReturn((double) 0).when(dao).calculateBiggestRelativeDeviation(
				any(DayRecord.class));

		assertEquals(new Long(0), dao.calculateMalus(new DayRecord(new Date())));
	}

	@Test
	public void calculationOfMalusOfOne() {
		DAO dao = spy(new DAO());

		doReturn(1d).when(dao).calculateBiggestRelativeDeviation(
				any(DayRecord.class));

		assertEquals(new Long(100),
				dao.calculateMalus(new DayRecord(new Date())));
	}

	@Test
	public void calculationOfMalusOfX() {
		DAO dao = spy(new DAO());

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
		DAO mock = spy(new DAO());
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
		DAO mock = spy(new DAO());
		when(mock.calculateBonus(any(DayRecord.class))).thenReturn(0L);
		doReturn(100L).when(mock).calculateMalus(any(DayRecord.class));

		assertEquals(new Long(0),
				mock.calculateOverallpoints(new DayRecord(new Date())));
	}

	@Test
	public void malusOf50ResultsIn0PointsTotal() {
		DAO mock = spy(new DAO());
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
