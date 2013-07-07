package de.stevenschwenke.java.javafx.workLifeBalance.client.calendarView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Calendar;

import javafx.scene.Group;
import javafx.scene.chart.XYChart.Series;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;

import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit.MyBatisDao;
import de.stevenschwenke.java.javafx.workLifeBalance.client.data.TestDatabaseFacade;

/**
 * Tests for {@link CalendarComponent}
 * 
 * @author Steven Schwenke
 * 
 */
public class CalendarComponentTest {

	/** path to test config for the in-memory database */
	private MyBatisDao dao;

	@Before
	public void setup() {
		TestDatabaseFacade testDatabaseFacade = new TestDatabaseFacade();
		testDatabaseFacade.createInMemoryDatabase();
		dao = testDatabaseFacade.createDao();
		BasicConfigurator.configure();
	}

	@Test
	public void getTimeRecordForAspectReturnsNullForAnEmptyList() {
		assertNull(CalendarComponent.getTimeRecordForAspect(new ArrayList<TimeRecord>(), Aspect.CAREER));
	}

	@Test
	public void getTimeRecordForAspectTest() {
		TimeRecord tr = new TimeRecord(Aspect.HEALTH, 42);

		ArrayList<TimeRecord> list = new ArrayList<TimeRecord>();
		list.add(new TimeRecord(Aspect.YOU, 2));
		list.add(tr);

		TimeRecord timeRecordFound = CalendarComponent.getTimeRecordForAspect(list, Aspect.HEALTH);
		assertEquals(tr, timeRecordFound);
	}

	@Test
	public void fillSeriesWithDataTest() {

		// Record 1 - 01.01.2000
		Calendar c1 = Calendar.getInstance();
		c1.set(Calendar.YEAR, 2000);
		c1.set(Calendar.MONTH, 0);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		DayRecord dr1 = new DayRecord(c1.getTime());
		dr1.addTimeRecord(new TimeRecord(Aspect.CAREER, 2));
		dr1.addTimeRecord(new TimeRecord(Aspect.HEALTH, 4));
		dao.insertDayRecord(dr1);

		// Record 2 - 02.01.2000
		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.YEAR, 2000);
		c2.set(Calendar.MONTH, 0);
		c2.set(Calendar.DAY_OF_MONTH, 2);
		DayRecord dr2 = new DayRecord(c2.getTime());
		dr2.addTimeRecord(new TimeRecord(Aspect.CAREER, 1));
		dr2.addTimeRecord(new TimeRecord(Aspect.HEALTH, 3));
		dr2.addTimeRecord(new TimeRecord(Aspect.YOU, 5));
		dao.insertDayRecord(dr2);

		Series<String, Number> healthSeries = new Series<String, Number>();
		Series<String, Number> familySeries = new Series<String, Number>();
		Series<String, Number> careerSeries = new Series<String, Number>();
		Series<String, Number> youSeries = new Series<String, Number>();

		CalendarComponent component = new CalendarComponent(null, dao, "calendar/calendar.fxml", new Group());
		component.fillSeriesWithData(healthSeries, familySeries, careerSeries, youSeries);

		assertEquals(2, careerSeries.getData().size());
		assertEquals("2000-01-01", careerSeries.getData().get(0).getXValue());
		assertEquals(2, careerSeries.getData().get(0).getYValue());
		assertEquals("2000-01-02", careerSeries.getData().get(1).getXValue());
		assertEquals(1, careerSeries.getData().get(1).getYValue());

		assertEquals(2, healthSeries.getData().size());
		assertEquals("2000-01-01", healthSeries.getData().get(0).getXValue());
		assertEquals(4, healthSeries.getData().get(0).getYValue());
		assertEquals("2000-01-02", healthSeries.getData().get(1).getXValue());
		assertEquals(3, healthSeries.getData().get(1).getYValue());

		assertEquals(2, youSeries.getData().size());
		assertEquals("2000-01-01", youSeries.getData().get(0).getXValue());
		// This test demonstrates that for days without any data, a zero-value
		// is inserted
		assertEquals(0, youSeries.getData().get(0).getYValue());
		assertEquals("2000-01-02", youSeries.getData().get(1).getXValue());
		assertEquals(5, youSeries.getData().get(1).getYValue());
	}
}
