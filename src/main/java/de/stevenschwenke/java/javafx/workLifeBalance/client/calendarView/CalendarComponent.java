package de.stevenschwenke.java.javafx.workLifeBalance.client.calendarView;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.scene.Group;
import javafx.scene.chart.XYChart;
import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.Component;
import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;

/**
 * TODO Comment!
 * 
 * @author Steven Schwenke
 * 
 */
public class CalendarComponent extends Component {

	private CalendarController viewController;

	private CalendarDao dao;

	/**
	 * @param parent
	 * @param fxmlFileName
	 * @param group
	 */
	public CalendarComponent(Component parent, CalendarDao dao,
			String fxmlFileName, Group group) {
		super(parent, fxmlFileName, group);
		this.dao = dao;

		viewController = (CalendarController) getFxmlLoaderInternal()
				.getController();
		viewController.setComponent(this);
		init();
	}

	public void init() {

		XYChart.Series healthSeries = new XYChart.Series();
		healthSeries.setName("Health");
		for (DayRecord dr : dao.getAllDayRecords()) {
			for (TimeRecord timeRecordsToday : dr.getTimeRecordsToday()) {
				if (timeRecordsToday.getAspect().equals(Aspect.HEALTH))
					healthSeries.getData().add(
							new XYChart.Data(formatDate(dr.getDate()),
									timeRecordsToday.getHours()));
			}
		}
		viewController.getChart().getData().add(healthSeries);

		XYChart.Series familySeries = new XYChart.Series();
		familySeries.setName("Family");
		for (DayRecord dr : dao.getAllDayRecords()) {
			for (TimeRecord timeRecordsToday : dr.getTimeRecordsToday()) {
				if (timeRecordsToday.getAspect().equals(Aspect.FAMILY))
					familySeries.getData().add(
							new XYChart.Data(formatDate(dr.getDate()),
									timeRecordsToday.getHours()));
			}
		}
		viewController.getChart().getData().add(familySeries);

		XYChart.Series careerSeries = new XYChart.Series();
		careerSeries.setName("Career");
		for (DayRecord dr : dao.getAllDayRecords()) {
			for (TimeRecord timeRecordsToday : dr.getTimeRecordsToday()) {
				if (timeRecordsToday.getAspect().equals(Aspect.CAREER))
					careerSeries.getData().add(
							new XYChart.Data(formatDate(dr.getDate()),
									timeRecordsToday.getHours()));
			}
		}
		viewController.getChart().getData().add(careerSeries);

		XYChart.Series youSeries = new XYChart.Series();
		youSeries.setName("You");
		for (DayRecord dr : dao.getAllDayRecords()) {
			for (TimeRecord timeRecordsToday : dr.getTimeRecordsToday()) {
				if (timeRecordsToday.getAspect().equals(Aspect.YOU))
					youSeries.getData().add(
							new XYChart.Data(formatDate(dr.getDate()),
									timeRecordsToday.getHours()));
			}
		}
		viewController.getChart().getData().add(youSeries);
	}

	@Override
	public void notifyDataChanged(Component component) {
		// TODO Auto-generated method stub

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
