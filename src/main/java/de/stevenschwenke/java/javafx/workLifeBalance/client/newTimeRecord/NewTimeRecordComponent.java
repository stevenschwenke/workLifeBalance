package de.stevenschwenke.java.javafx.workLifeBalance.client.newTimeRecord;

import java.util.Calendar;

import javafx.scene.Group;
import de.stevenschwenke.java.javafx.workLifeBalance.client.Component;

/**
 * TODO Comment!
 * 
 * @author Steven Schwenke
 * 
 */
public class NewTimeRecordComponent extends Component {

	private NewTimeRecordController newTimeRecordController;

	private NewTimeRecordDao dao;

	/**
	 * @param parent
	 * @param dao
	 * @param fxmlFileName
	 * @param group
	 */
	public NewTimeRecordComponent(Component parent, NewTimeRecordDao dao,
			String fxmlFileName, Group group) {
		super(parent, fxmlFileName, group);

		this.dao = dao;

		newTimeRecordController = (NewTimeRecordController) getFxmlLoaderInternal()
				.getController();
		newTimeRecordController.setComponent(this);
		newTimeRecordController.setDao(dao);
		newTimeRecordController.initDate(Calendar.getInstance());

	}

	@Override
	public void notifyDataChanged(Component component) {
	}

}
