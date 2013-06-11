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
	public void fillingAnExistingDayRecordFromTheFormUsesTimeRecordsIfPossible() {
		NewTimeRecordController controller = new NewTimeRecordController();
		TextField careerHours = new TextField();
		careerHours.setText("10");
		controller.setCareerHours(careerHours);
		controller.setFamilyHours(new TextField());
		controller.setHealthHours(new TextField());
		controller.setYouHours(new TextField());

		DayRecord dayRecord = new DayRecord(new Date());
		dayRecord.addTimeRecord(new TimeRecord(Aspect.CAREER, 5));

		controller.fillDayRecordFromTextfields(dayRecord);
		assertEquals(1, dayRecord.getTimeRecordsToday().size());
		assertEquals(15, dayRecord.getTimeRecordsToday().get(0).getHours());
	}

}
