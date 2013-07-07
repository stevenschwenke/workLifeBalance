package de.stevenschwenke.java.javafx.workLifeBalance.client.calendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.Component;
import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;

/**
 * This is the central class for everything related to the calendar view.
 * 
 * @author Steven Schwenke
 * 
 */
public class CalendarComponent extends Component {

	private CalendarController viewController;

	private CalendarDao dao;

	/**
	 * @param componentFactory
	 * @param parent
	 * @param fxmlFileName
	 * @param group
	 */
	public CalendarComponent(Component parent, CalendarDao dao, String fxmlFileName, Group group) {
		super(parent, fxmlFileName, group);

		this.dao = dao;

		viewController = (CalendarController) getFxmlLoaderInternal().getController();
		viewController.setComponent(this);
		init();
	}

	public void init() {
		Series<String, Number> healthSeries = new XYChart.Series<String, Number>();
		healthSeries.setName("Health");
		XYChart.Series<String, Number> familySeries = new XYChart.Series<String, Number>();
		familySeries.setName("Family");
		XYChart.Series<String, Number> careerSeries = new XYChart.Series<String, Number>();
		careerSeries.setName("Career");
		XYChart.Series<String, Number> youSeries = new XYChart.Series<String, Number>();
		youSeries.setName("You");

		fillSeriesWithData(healthSeries, familySeries, careerSeries, youSeries);

		viewController.getChart().getData().add(healthSeries);
		viewController.getChart().getData().add(familySeries);
		viewController.getChart().getData().add(careerSeries);
		viewController.getChart().getData().add(youSeries);
	}

	/**
	 * Fills the {@link Series} for the calendar-view with data.
	 * 
	 * @param healthSeries
	 *            Health
	 * @param familySeries
	 *            Family
	 * @param careerSeries
	 *            Career
	 * @param youSeries
	 *            You
	 */
	void fillSeriesWithData(XYChart.Series<String, Number> healthSeries, XYChart.Series<String, Number> familySeries,
			XYChart.Series<String, Number> careerSeries, XYChart.Series<String, Number> youSeries) {

		Date earliestDayOfRecord = dao.getEarliestDayOfRecord();
		Date lastDayOfRecord = dao.getLastDayOfRecord();
		Date actualDate = earliestDayOfRecord;

		while (actualDate.before(lastDayOfRecord) || actualDate.equals(lastDayOfRecord)) {
			DayRecord dr = dao.getDayRecord(actualDate);
			if (dr == null) {
				healthSeries.getData().add(new XYChart.Data<String, Number>(formatDate(actualDate), 0));
				familySeries.getData().add(new XYChart.Data<String, Number>(formatDate(actualDate), 0));
				careerSeries.getData().add(new XYChart.Data<String, Number>(formatDate(actualDate), 0));
				youSeries.getData().add(new XYChart.Data<String, Number>(formatDate(actualDate), 0));

				actualDate = addOneDay(actualDate);
				continue;
			}

			TimeRecord healthRecord = getTimeRecordForAspect(dr.getTimeRecordsToday(), Aspect.HEALTH);
			if (healthRecord == null) {
				healthSeries.getData().add(new XYChart.Data<String, Number>(formatDate(actualDate), 0));
			} else {
				healthSeries.getData().add(new XYChart.Data<String, Number>(formatDate(actualDate), healthRecord.getHours()));
			}

			TimeRecord familyRecord = getTimeRecordForAspect(dr.getTimeRecordsToday(), Aspect.FAMILY);
			if (familyRecord == null) {
				familySeries.getData().add(new XYChart.Data<String, Number>(formatDate(actualDate), 0));
			} else {
				familySeries.getData().add(new XYChart.Data<String, Number>(formatDate(actualDate), familyRecord.getHours()));
			}

			TimeRecord careerhRecord = getTimeRecordForAspect(dr.getTimeRecordsToday(), Aspect.CAREER);
			if (careerhRecord == null) {
				careerSeries.getData().add(new XYChart.Data<String, Number>(formatDate(actualDate), 0));
			} else {
				careerSeries.getData().add(new XYChart.Data<String, Number>(formatDate(actualDate), careerhRecord.getHours()));
			}

			TimeRecord youRecord = getTimeRecordForAspect(dr.getTimeRecordsToday(), Aspect.YOU);
			if (youRecord == null) {
				youSeries.getData().add(new XYChart.Data<String, Number>(formatDate(actualDate), 0));
			} else {
				youSeries.getData().add(new XYChart.Data<String, Number>(formatDate(actualDate), youRecord.getHours()));
			}

			actualDate = addOneDay(actualDate);
		}
	}

	/**
	 * Gets a {@TimeRecord} of a certain {@link Aspect} out of a
	 * list of records.
	 * 
	 * @param records
	 *            in which to search for
	 * @param aspect
	 *            of the searched {@link TimeRecord}
	 * @return record or null if none found
	 */
	static TimeRecord getTimeRecordForAspect(List<TimeRecord> records, Aspect aspect) {
		for (TimeRecord tr : records) {
			if (tr.getAspect().equals(aspect))
				return tr;
		}
		return null;
	}

	private Date addOneDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}

	@Override
	public void notifyDataChanged(Component component) {
		// This component doesn't want to get notified of any changes.
	}

	/**
	 * Formats a date for display.
	 * 
	 * @param date
	 *            to format
	 * @return string for the UI
	 */
	private String formatDate(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(date);
	}
}
