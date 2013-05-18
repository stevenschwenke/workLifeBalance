package de.stevenschwenke.java.javafx.workLifeBalance.server.newTimeRecord;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;

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

	@Test
	public void calculateOverallpointsWithNoRecords() {
		assertEquals(new Long(0), dao.calculateOverallpoints());
	}

	@Ignore("The calculation of overall points will be refactored so this test doesn't make sense at the moment.")
	@Test
	public void perfectDistributionOfAspectsResultsIn100Points() {

		DayRecord dr = new DayRecord();
		dr.addTimeRecord(new TimeRecord(Aspect.CAREER, 1));
		dr.addTimeRecord(new TimeRecord(Aspect.FAMILY, 1));
		dr.addTimeRecord(new TimeRecord(Aspect.HEALTH, 1));
		dr.addTimeRecord(new TimeRecord(Aspect.YOU, 1));

		dao.addNewDayRecord(dr);

		assertEquals(new Long(100), dao.calculateOverallpoints());
	}

}
