package de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.ComponentFactory;

public class CockpitController implements Initializable {

	private static Logger log = LogManager.getLogger(CockpitController.class.getName());

	private CockpitComponent component;

	private ObservableList<PieChart.Data> pieChartData;

	protected PieChart.Data getPieDataCareer() {
		return pieDataCareer;
	}

	protected PieChart.Data getPieDataFamily() {
		return pieDataFamily;
	}

	protected PieChart.Data getPieDataHealth() {
		return pieDataHealth;
	}

	protected PieChart.Data getPieDataYou() {
		return pieDataYou;
	}

	protected Label getPoints() {
		return points;
	}

	private PieChart.Data pieDataCareer;

	private PieChart.Data pieDataFamily;

	private PieChart.Data pieDataHealth;

	private PieChart.Data pieDataYou;

	@FXML
	// fx:id="add"
	private Button add; // Value injected by FXMLLoader

	@FXML
	// fx:id="calendar"
	private Button calendar; // Value injected by FXMLLoader

	@FXML
	// fx:id="points"
	private Label points; // Value injected by FXMLLoader

	@FXML
	// fx:id="pieChart"
	private PieChart pieChart; // Value injected by FXMLLoader

	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		assert add != null : "fx:id=\"add\" was not injected: check your FXML file 'cockpit.fxml'.";
		assert pieChart != null : "fx:id=\"pieChart\" was not injected: check your FXML file 'cockpit.fxml'.";

		pieDataCareer = new PieChart.Data(Aspect.CAREER.getName(), 25);
		pieDataFamily = new PieChart.Data(Aspect.FAMILY.getName(), 25);
		pieDataHealth = new PieChart.Data(Aspect.HEALTH.getName(), 25);
		pieDataYou = new PieChart.Data(Aspect.YOU.getName(), 25);
		pieChartData = FXCollections.observableArrayList(

		pieDataCareer,

		pieDataFamily,

		pieDataHealth,

		pieDataYou);

		pieChart.setData(pieChartData);
	}

	// Handler for Button[fx:id="add"] onAction
	public void add(ActionEvent event) {
		ComponentFactory.getInstance().createNewTimeRecordComponent(component);
	}

	// Handler for Button[fx:id="calendar"] onAction
	public void calendar(ActionEvent event) {
		ComponentFactory.getInstance().createCalendarComponent(component);
	}

	public void setComponent(CockpitComponent component) {
		this.component = component;

	}

}

// TODO Idee: von Component erben, sodass spezielle Components entstehen, die
// auch die konkreten DAOs und Controller kennen. Die DAOs dann per
// ComponentFactory (die alle diese speziellen Componenten kennt) bei der
// Erstellung der Components setzen.
