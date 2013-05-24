package de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit;

import javafx.scene.Group;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.stevenschwenke.java.javafx.workLifeBalance.client.Component;
import de.stevenschwenke.java.javafx.workLifeBalance.client.newTimeRecord.NewTimeRecordComponent;

/**
 * TODO Comment!
 * 
 * @author Steven Schwenke
 * 
 */
public class CockpitComponent extends Component {

	private static Logger log = LogManager.getLogger(CockpitComponent.class
			.getName());

	private CockpitController cockpitController;

	private DAO dao;

	/**
	 * @param parent
	 * @param fxmlFileName
	 * @param group
	 */
	public CockpitComponent(Component parent, DAO dao, String fxmlFileName,
			Group group) {
		super(parent, fxmlFileName, group);

		this.dao = dao;

		cockpitController = (CockpitController) getFxmlLoaderInternal()
				.getController();
		cockpitController.setComponent(this);

	}

	protected DAO getDao() {
		return dao;
	}

	public void notifyDataChanged(Component component) {
		if (component instanceof NewTimeRecordComponent) {
			log.debug("Capturing data changed event from " + component);
			cockpitController.getPieDataCareer().setPieValue(
					dao.calculateCareer());
			cockpitController.getPieDataFamily().setPieValue(
					dao.calculateFamily());
			cockpitController.getPieDataHealth().setPieValue(
					dao.calculateHealth());
			cockpitController.getPieDataYou().setPieValue(dao.calculateYou());

			cockpitController.getPoints().setText(
					dao.calculateOverallpoints().toString());
		}
	}
}
