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
import de.stevenschwenke.java.javafx.workLifeBalance.client.Component;
import de.stevenschwenke.java.javafx.workLifeBalance.client.ViewController;
import de.stevenschwenke.java.javafx.workLifeBalance.client.newTimeRecord.NewTimeRecordController;
import de.stevenschwenke.java.javafx.workLifeBalance.client.newTimeRecord.NewTimeRecordDao;

public class CockpitController implements Initializable, ViewController {

	private static Logger log = LogManager.getLogger(CockpitController.class
			.getName());

	private Component component;

	private NewTimeRecordDao dao;

	private ObservableList<PieChart.Data> pieChartData;

	private PieChart.Data pieDataCareer;

	private PieChart.Data pieDataFamily;

	private PieChart.Data pieDataHealth;

	private PieChart.Data pieDataYou;

	@FXML
	// fx:id="add"
	private Button add; // Value injected by FXMLLoader

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
		Component newTimeRecordPane = new Component(component,
				"newTimeRecord/newTimeRecord.fxml", component.getGroup());
		NewTimeRecordController newTimeRecordController = (NewTimeRecordController) newTimeRecordPane
				.getViewController();

		newTimeRecordController.setDao(dao);
	}

	public void setComponent(Component component) {
		this.component = component;

	}

	public void setDao(NewTimeRecordDao dao) {
		this.dao = dao;
	}

	public void notifyDataChanged(Component component) {
		if (component.getViewController() instanceof NewTimeRecordController) {
			log.debug("Capturing data changed event from " + component);

			pieDataCareer.setPieValue(dao.calculateCareer());
			pieDataFamily.setPieValue(dao.calculateFamily());
			pieDataHealth.setPieValue(dao.calculateHealth());
			pieDataYou.setPieValue(dao.calculateYou());

			points.setText(dao.calculateOverallpoints().toString());
		}
	}
}
