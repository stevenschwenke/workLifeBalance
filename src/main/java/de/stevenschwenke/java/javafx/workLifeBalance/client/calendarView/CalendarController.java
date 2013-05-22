package de.stevenschwenke.java.javafx.workLifeBalance.client.calendarView;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.Component;
import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.ViewController;

public class CalendarController implements Initializable, ViewController {

	private Component component;

	private CalendarDao dao;

	public void setDao(CalendarDao dao) {
		this.dao = dao;

		XYChart.Series healthSeries = new XYChart.Series();
		healthSeries.setName("Health");
		for (DayRecord dr : dao.getAllDayRecords()) {
			for (TimeRecord timeRecordsToday : dr.getTimeRecordsToday()) {
				if (timeRecordsToday.getAspect().equals(Aspect.HEALTH))
					healthSeries.getData().add(
							new XYChart.Data(dr.getDate().toString(),
									timeRecordsToday.getHours()));
			}
		}
		chart.getData().add(healthSeries);

		XYChart.Series familySeries = new XYChart.Series();
		familySeries.setName("Family");
		for (DayRecord dr : dao.getAllDayRecords()) {
			for (TimeRecord timeRecordsToday : dr.getTimeRecordsToday()) {
				if (timeRecordsToday.getAspect().equals(Aspect.FAMILY))
					familySeries.getData().add(
							new XYChart.Data(dr.getDate().toString(),
									timeRecordsToday.getHours()));
			}
		}
		chart.getData().add(familySeries);

		XYChart.Series careerSeries = new XYChart.Series();
		careerSeries.setName("Health");
		for (DayRecord dr : dao.getAllDayRecords()) {
			for (TimeRecord timeRecordsToday : dr.getTimeRecordsToday()) {
				if (timeRecordsToday.getAspect().equals(Aspect.CAREER))
					careerSeries.getData().add(
							new XYChart.Data(dr.getDate().toString(),
									timeRecordsToday.getHours()));
			}
		}
		chart.getData().add(careerSeries);

		XYChart.Series youSeries = new XYChart.Series();
		youSeries.setName("Health");
		for (DayRecord dr : dao.getAllDayRecords()) {
			for (TimeRecord timeRecordsToday : dr.getTimeRecordsToday()) {
				if (timeRecordsToday.getAspect().equals(Aspect.YOU))
					youSeries.getData().add(
							new XYChart.Data(dr.getDate().toString(),
									timeRecordsToday.getHours()));
			}
		}
		chart.getData().add(youSeries);
	}

	@FXML
	// fx:id="chart"
	private LineChart<String, Number> chart; // Value injected by FXMLLoader

	// Handler for Button[Button[id=null, styleClass=button]] onAction
	public void cancel(ActionEvent event) {
		component.getGroup().getChildren().remove(component.getView());
	}

	// Handler for Button[Button[id=null, styleClass=button]] onAction
	public void ok(ActionEvent event) {
		component.getGroup().getChildren().remove(component.getView());
	}

	@Override
	// This method is called by the FXMLLoader when initialization is complete
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		assert chart != null : "fx:id=\"chart\" was not injected: check your FXML file 'calendarView.fxml'.";

	}

	@Override
	public void notifyDataChanged(Component component) {
		// TODO Auto-generated method stub

	}

	public void setComponent(Component component) {
		this.component = component;

	}
}
