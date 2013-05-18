package de.stevenschwenke.java.javafx.workLifeBalance.server;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.log4j.BasicConfigurator;

import de.stevenschwenke.java.javafx.workLifeBalance.client.Component;
import de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit.CockpitController;
import de.stevenschwenke.java.javafx.workLifeBalance.server.newTimeRecord.DAO;

/**
 * TODO Comment!
 * 
 * @author Steven Schwenke
 * 
 */
public class Start extends Application {

	private static Start instance;

	private Group root;

	private DAO dao;

	public static Start getInstance() {
		if (instance == null) {
			instance = new Start();
		}
		return instance;
	}

	public Start() {
		instance = this;
	}

	private void init(Stage primaryStage) {

		root = new Group();

		primaryStage.setScene(new Scene(root));

		Component cockpit = new Component(null, "cockpit/cockpit.fxml", root);
		CockpitController cockpitController = (CockpitController) cockpit
				.getViewController();

		cockpitController.setDao(dao);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		dao = new DAO();

		init(primaryStage);

		primaryStage.show();
	}

	public static void main(String[] args) {

		BasicConfigurator.configure();

		launch(args);

	}
}

// TODO Overall-Points: Deviation from 25-25-25-25
