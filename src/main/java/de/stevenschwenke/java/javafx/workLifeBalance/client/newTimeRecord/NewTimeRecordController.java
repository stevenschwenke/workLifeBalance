package de.stevenschwenke.java.javafx.workLifeBalance.client.newTimeRecord;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.Component;
import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;

public class NewTimeRecordController implements Initializable {

	private Component component;

	private NewTimeRecordDao dao;

	@FXML
	// fx:id="cancel"
	private Button cancel; // Value injected by FXMLLoader

	@FXML
	// fx:id="coloniesLabel"
	private Label coloniesLabel; // Value injected by FXMLLoader

	@FXML
	// fx:id="careerHours"
	private TextField careerHours; // Value injected by FXMLLoader

	@FXML
	// fx:id="familyHours"
	private TextField familyHours; // Value injected by FXMLLoader

	@FXML
	// fx:id="healthHours"
	private TextField healthHours; // Value injected by FXMLLoader

	@FXML
	// fx:id="youHours"
	private TextField youHours; // Value injected by FXMLLoader

	@FXML
	// fx:id="ok"
	private Button ok; // Value injected by FXMLLoader

	// This method is called by the FXMLLoader when initialization is complete
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

	}

	@FXML
	public void ok() {

		DayRecord dailyRecord = new DayRecord(new Date());

		if (!careerHours.getText().isEmpty()) {
			TimeRecord careerRecord = new TimeRecord(Aspect.CAREER,
					Integer.parseInt(careerHours.getText()));
			dailyRecord.addTimeRecord(careerRecord);
		}

		if (!familyHours.getText().isEmpty()) {
			TimeRecord familyRecord = new TimeRecord(Aspect.FAMILY,
					Integer.parseInt(familyHours.getText()));
			dailyRecord.addTimeRecord(familyRecord);
		}

		if (!healthHours.getText().isEmpty()) {
			TimeRecord healthRecord = new TimeRecord(Aspect.HEALTH,
					Integer.parseInt(healthHours.getText()));
			dailyRecord.addTimeRecord(healthRecord);
		}

		if (!youHours.getText().isEmpty()) {
			TimeRecord youRecord = new TimeRecord(Aspect.YOU,
					Integer.parseInt(youHours.getText()));
			dailyRecord.addTimeRecord(youRecord);

		}

		dao.addNewDayRecord(dailyRecord);

		component.bubbleDataChanged(component);

		component.getGroup().getChildren().remove(component.getView());
	}

	@FXML
	public void cancel() {
		component.getGroup().getChildren().remove(component.getView());
	}

	public void setComponent(Component component) {
		this.component = component;

	}

	public void setDao(NewTimeRecordDao dao) {
		this.dao = dao;
	}

}
