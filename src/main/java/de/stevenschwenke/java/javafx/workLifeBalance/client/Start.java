package de.stevenschwenke.java.javafx.workLifeBalance.client;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.log4j.BasicConfigurator;

import de.stevenschwenke.java.javafx.workLifeBalance.client.cockpit.MyBatisDao;

/**
 * TODO Comment!
 * 
 * @author Steven Schwenke
 * 
 */
public class Start extends Application {

	private static Start instance;

	private Group root;

	private MyBatisDao dao;

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

		ComponentFactory.getInstance().setRoot(root);

		primaryStage.setScene(new Scene(root));

		ComponentFactory.getInstance().createCockpitComponent(null);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		dao = ComponentFactory.getInstance().getDao();

		init(primaryStage);

		primaryStage.show();
	}

	public static void main(String[] args) {

		BasicConfigurator.configure();

		launch(args);

	}
}
