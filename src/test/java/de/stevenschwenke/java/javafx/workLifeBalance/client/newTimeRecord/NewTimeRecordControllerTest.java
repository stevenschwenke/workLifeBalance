package de.stevenschwenke.java.javafx.workLifeBalance.client.newTimeRecord;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javafx.scene.control.TextField;

import org.junit.Test;

import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.DayRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;

/**
 * Tests for {@link NewTimeRecordController}.
 * 
 * @author Steven Schwenke
 * 
 */
public class NewTimeRecordControllerTest {

	@Test
	public void fillingAnExistingDayRecordFromTheFormCreatesNewTimeRecords() {
		NewTimeRecordController controller = new NewTimeRecordController();
		TextField careerHours = new TextField();
		careerHours.setText("1");
		controller.setCareerHours(careerHours);
		controller.setFamilyHours(new TextField());
		controller.setHealthHours(new TextField());
		controller.setYouHours(new TextField());

		DayRecord dayRecord = new DayRecord(new Date());
		assertTrue(dayRecord.getTimeRecordsToday().isEmpty());

		controller.fillDayRecordFromTextfields(dayRecord);
		assertEquals(1, dayRecord.getTimeRecordsToday().size());
		assertEquals(1, dayRecord.getTimeRecordsToday().get(0).getHours());
	}

	@Test
	public void overwritingAnExistingValueInATextfieldUpdatesTheExistingTimeRecordToTheNewValue() {

		DayRecord dayRecordWithExistingValues = createDayRecordWithValuesForEveryAspect();

		NewTimeRecordController controllerWithNewHours = createAControllerWithValuesForEveryAspect();

		controllerWithNewHours
				.fillDayRecordFromTextfields(dayRecordWithExistingValues);

		assertEquals(
				"If a record that already has a value is updated, the new value should only be the value from the controller, not the sum of the existing one and the new one.",
				5, dayRecordWithExistingValues.getTimeRecordsToday().get(0)
						.getHours());
		assertEquals(
				"If a record that already has a value is updated, the new value should only be the value from the controller, not the sum of the existing one and the new one.",
				6, dayRecordWithExistingValues.getTimeRecordsToday().get(1)
						.getHours());
		assertEquals(
				"If a record that already has a value is updated, the new value should only be the value from the controller, not the sum of the existing one and the new one.",
				7, dayRecordWithExistingValues.getTimeRecordsToday().get(2)
						.getHours());
		assertEquals(
				"If a record that already has a value is updated, the new value should only be the value from the controller, not the sum of the existing one and the new one.",
				8, dayRecordWithExistingValues.getTimeRecordsToday().get(3)
						.getHours());
	}

	private DayRecord createDayRecordWithValuesForEveryAspect() {
		DayRecord dayRecordWithExistingValues = new DayRecord(new Date());
		dayRecordWithExistingValues.addTimeRecord(new TimeRecord(Aspect.CAREER,
				1));
		dayRecordWithExistingValues.addTimeRecord(new TimeRecord(Aspect.FAMILY,
				2));
		dayRecordWithExistingValues.addTimeRecord(new TimeRecord(Aspect.HEALTH,
				3));
		dayRecordWithExistingValues
				.addTimeRecord(new TimeRecord(Aspect.YOU, 4));
		return dayRecordWithExistingValues;
	}

	private NewTimeRecordController createAControllerWithValuesForEveryAspect() {
		NewTimeRecordController controllerWithNewHours = new NewTimeRecordController();
		TextField careerTextFieldHours = new TextField();
		careerTextFieldHours.setText("5");
		controllerWithNewHours.setCareerHours(careerTextFieldHours);
		TextField familyTextField = new TextField();
		familyTextField.setText("6");
		controllerWithNewHours.setFamilyHours(familyTextField);
		TextField healthTextField = new TextField();
		healthTextField.setText("7");
		controllerWithNewHours.setHealthHours(healthTextField);
		TextField youTextField = new TextField();
		youTextField.setText("8");
		controllerWithNewHours.setYouHours(youTextField);
		return controllerWithNewHours;
	}
}
