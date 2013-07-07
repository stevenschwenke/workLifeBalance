package de.stevenschwenke.java.javafx.workLifeBalance.client;

import javafx.scene.Group;
import de.stevenschwenke.java.javafx.workLifeBalance.client.calendarView.CalendarComponent;
import de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit.CockpitComponent;
import de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit.MyBatisDao;
import de.stevenschwenke.java.javafx.workLifeBalance.client.newTimeRecord.NewTimeRecordComponent;

/**
 * Factory to build {@link Components}.
 * 
 * @author Steven Schwenke
 * 
 */
public class ComponentFactory {

	private static ComponentFactory instance;

	/** currently the only DAO */
	private MyBatisDao dao;

	private Group root;

	private ComponentFactory() {

	}

	public static ComponentFactory getInstance() {
		if (instance == null) {
			instance = new ComponentFactory();
		}
		return instance;
	}

	public MyBatisDao getDao() {
		if (dao == null) {
			dao = new MyBatisDao("de/stevenschwenke/java/javafx/workLifeBalance/client/data/mybatis-live-config.xml");
		}
		return dao;
	}

	public NewTimeRecordComponent createNewTimeRecordComponent(Component parent) {
		return new NewTimeRecordComponent(parent, dao, "newTimeRecord/newTimeRecord.fxml", parent.getGroup());
	}

	public CalendarComponent createCalendarComponent(Component parent) {
		return new CalendarComponent(parent, dao, "calendar/calendar.fxml", parent.getGroup());
	}

	public CockpitComponent createCockpitComponent(Component parent) {
		return new CockpitComponent(null, dao, "cockpit/cockpit.fxml", root);
	}

	public void setRoot(Group root) {
		this.root = root;
	}
}
