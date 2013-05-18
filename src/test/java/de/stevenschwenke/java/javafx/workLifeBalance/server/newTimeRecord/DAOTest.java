package de.stevenschwenke.java.javafx.workLifeBalance.server.newTimeRecord;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
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

	@Test
	public void perfectDistributionOfAspectsResultsIn100Points() {
		dao.addNewTimeRecord(new TimeRecord(Aspect.CAREER, 1));
		dao.addNewTimeRecord(new TimeRecord(Aspect.FAMILY, 1));
		dao.addNewTimeRecord(new TimeRecord(Aspect.HEALTH, 1));
		dao.addNewTimeRecord(new TimeRecord(Aspect.YOU, 1));

		assertEquals(new Long(100), dao.calculateOverallpoints());
	}

	@Test
	public void perfectDistributionOfMultipleAspectsResultsIn100Points() {

		// TODO Target is to enter daily records where each record has exactly
		// one amounts of hours per aspect (so, 4 records, for each aspect one).
		// Hence, the method to calculate points has to deal with just one
		// instance of an aspect.

		dao.addNewTimeRecord(new TimeRecord(Aspect.CAREER, 1));
		dao.addNewTimeRecord(new TimeRecord(Aspect.FAMILY, 1));
		dao.addNewTimeRecord(new TimeRecord(Aspect.HEALTH, 1));
		dao.addNewTimeRecord(new TimeRecord(Aspect.YOU, 1));

		dao.addNewTimeRecord(new TimeRecord(Aspect.CAREER, 1));
		dao.addNewTimeRecord(new TimeRecord(Aspect.FAMILY, 1));
		dao.addNewTimeRecord(new TimeRecord(Aspect.HEALTH, 1));
		dao.addNewTimeRecord(new TimeRecord(Aspect.YOU, 1));

		assertEquals(new Long(100), dao.calculateOverallpoints());
	}

}
