package de.stevenschwenke.java.javafx.workLifeBalance.client.newTimeRecord;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jfxtras.labs.scene.control.CalendarTextField;
import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.Component;
import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;

public class NewTimeRecordController implements Initializable {

	private Component component;

	private NewTimeRecordDao dao;

	@FXML
	// fx:id="date"
	private CalendarTextField date; // Value injected by FXMLLoader

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

		date.valueProperty().addListener(new ChangeListener<Calendar>() {
			@Override
			public void changed(
					ObservableValue<? extends Calendar> observableValue,
					Calendar oldValue, Calendar newValue) {

				clearTimeFields();

				Calendar selectedValue = newValue != null ? newValue : oldValue;

				boolean dayRecordExists = dao.dayRecordExists(selectedValue
						.getTime());

				if (dayRecordExists) {
					setTimeFieldsAccordingToDayRecord(selectedValue);
				}
			}
		});
	}

	protected void initDate(Calendar calendar) {
		date.setValue(calendar);

		boolean dayRecordExists = dao.dayRecordExists(calendar.getTime());

		if (dayRecordExists) {
			setTimeFieldsAccordingToDayRecord(calendar);
		} else {
			clearTimeFields();
		}
	}

	/**
	 * Empties the time fields.
	 */
	private void clearTimeFields() {
		careerHours.clear();
		familyHours.clear();
		healthHours.clear();
		youHours.clear();
	}

	/**
	 * Sets the fields in the form according to the selected date.
	 */
	private void setTimeFieldsAccordingToDayRecord(Calendar value) {
		DayRecord dayRecord = dao.getDayRecord(value.getTime());

		for (TimeRecord tr : dayRecord.getTimeRecordsToday()) {
			if (tr.getAspect().equals(Aspect.CAREER)) {
				careerHours.setText("" + tr.getHours());
			}
			if (tr.getAspect().equals(Aspect.FAMILY)) {
				familyHours.setText("" + tr.getHours());
			}
			if (tr.getAspect().equals(Aspect.HEALTH)) {
				healthHours.setText("" + tr.getHours());
			}
			if (tr.getAspect().equals(Aspect.YOU)) {
				youHours.setText("" + tr.getHours());
			}
		}
	}

	@FXML
	public void ok() {
		boolean dayRecordAlreadyExists = dao.dayRecordExists(date.getValue()
				.getTime());
		DayRecord dailyRecord = null;
		if (dayRecordAlreadyExists) {
			dailyRecord = dao.getDayRecord(date.getValue().getTime());
			fillDayRecordFromTextfields(dailyRecord);
			dao.updateDayRecord(dailyRecord);
		} else {
			dailyRecord = new DayRecord(date.getValue().getTime());
			fillDayRecordFromTextfields(dailyRecord);
			dao.insertDayRecord(dailyRecord);
		}

		component.bubbleDataChanged(component);

		component.getGroup().getChildren().remove(component.getView());
	}

	/**
	 * Fills a {@link DayRecord} with the values from the textfields. Values are
	 * overriden, not added.
	 * 
	 * @param dailyRecord
	 *            to fill
	 */
	void fillDayRecordFromTextfields(DayRecord dailyRecord) {
		updateCareerHoursFromTextFieldIntoRecord(dailyRecord);
		updateFamilyHoursFromTextFieldIntoRecord(dailyRecord);
		updateHealthHoursFromTextFieldIntoRecord(dailyRecord);
		updateYouHoursFromTextFieldIntoRecord(dailyRecord);
	}

	private void updateCareerHoursFromTextFieldIntoRecord(DayRecord dailyRecord) {
		if (!careerHours.getText().isEmpty()) {

			int hoursFromTextfield = Integer.parseInt(careerHours.getText());

			for (TimeRecord tr : dailyRecord.getTimeRecordsToday()) {
				if (tr.getAspect().equals(Aspect.CAREER)) {
					tr.setHours(hoursFromTextfield);
					return;
				}
			}

			TimeRecord record = new TimeRecord(Aspect.CAREER,
					hoursFromTextfield);
			dailyRecord.addTimeRecord(record);
		}
	}

	private void updateFamilyHoursFromTextFieldIntoRecord(DayRecord dailyRecord) {
		if (!familyHours.getText().isEmpty()) {

			int hoursFromTextfield = Integer.parseInt(familyHours.getText());

			for (TimeRecord tr : dailyRecord.getTimeRecordsToday()) {
				if (tr.getAspect().equals(Aspect.FAMILY)) {
					tr.setHours(hoursFromTextfield);
					return;
				}
			}

			TimeRecord record = new TimeRecord(Aspect.FAMILY,
					hoursFromTextfield);
			dailyRecord.addTimeRecord(record);
		}
	}

	private void updateHealthHoursFromTextFieldIntoRecord(DayRecord dailyRecord) {
		if (!healthHours.getText().isEmpty()) {

			int hoursFromTextfield = Integer.parseInt(healthHours.getText());

			for (TimeRecord tr : dailyRecord.getTimeRecordsToday()) {
				if (tr.getAspect().equals(Aspect.HEALTH)) {
					tr.setHours(hoursFromTextfield);
					return;
				}
			}

			TimeRecord record = new TimeRecord(Aspect.HEALTH,
					hoursFromTextfield);
			dailyRecord.addTimeRecord(record);
		}
	}

	private void updateYouHoursFromTextFieldIntoRecord(DayRecord dailyRecord) {
		if (!youHours.getText().isEmpty()) {

			int hoursFromTextfield = Integer.parseInt(youHours.getText());

			for (TimeRecord tr : dailyRecord.getTimeRecordsToday()) {
				if (tr.getAspect().equals(Aspect.YOU)) {
					tr.setHours(hoursFromTextfield);
					return;
				}
			}

			TimeRecord record = new TimeRecord(Aspect.YOU, hoursFromTextfield);
			dailyRecord.addTimeRecord(record);
		}
	}

	/**
	 * For testing purposes only!
	 */
	void setHealthHours(TextField healthHours) {
		this.healthHours = healthHours;
	}

	/**
	 * For testing purposes only!
	 */
	void setYouHours(TextField youHours) {
		this.youHours = youHours;
	}

	public void setColoniesLabel(Label coloniesLabel) {
		this.coloniesLabel = coloniesLabel;
	}

	/**
	 * For testing purposes only!
	 */
	void setFamilyHours(TextField familyHours) {
		this.familyHours = familyHours;
	}

	/**
	 * For testing purposes only!
	 */
	void setCareerHours(TextField careerHours) {
		this.careerHours = careerHours;
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
