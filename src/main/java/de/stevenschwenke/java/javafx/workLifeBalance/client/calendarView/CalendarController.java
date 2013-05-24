package de.stevenschwenke.java.javafx.workLifeBalance.client.calendarView;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import de.stevenschwenke.java.javafx.workLifeBalance.client.Component;

public class CalendarController implements Initializable {

	private Component component;

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

	public void setComponent(Component component) {
		this.component = component;

	}

	protected LineChart<String, Number> getChart() {
		return chart;
	}
}
