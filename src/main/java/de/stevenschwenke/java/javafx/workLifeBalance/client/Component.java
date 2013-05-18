package de.stevenschwenke.java.javafx.workLifeBalance.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Group;
import javafx.scene.Node;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Represents a user interface component. Wrapper class for the {@link Node} and
 * its {@link ViewController}.
 * 
 * @author Steven Schwenke
 * 
 */
public class Component {

	private static Logger log = LogManager.getLogger(Component.class.getName());

	private String fxmlFileName;

	/**
	 * Components are organized in a tree. This is the parent for this component
	 * or null, if this component is the root of the tree.
	 */
	private Component parent;

	/**
	 * Components are organized in a tree. These are the children of this
	 * component.
	 */
	private List<Component> children = new ArrayList<Component>();

	private Node view;

	private ViewController viewController;

	private Group group;

	public Component(Component parent, String fxmlFileName, Group group) {

		this.parent = parent;
		if (parent != null) {
			parent.getChildren().add(this);
		}
		this.fxmlFileName = fxmlFileName;
		this.group = group;

		FXMLLoader fxmlLoaderInternal = new FXMLLoader();
		InputStream in = Component.class.getResourceAsStream(fxmlFileName);
		fxmlLoaderInternal.setBuilderFactory(new JavaFXBuilderFactory());
		URL locationUrl = Component.class.getResource(fxmlFileName);
		fxmlLoaderInternal.setLocation(locationUrl);

		try {
			view = (Node) fxmlLoaderInternal.load(in);
		} catch (IOException e) {
			log.error("Error while creating Component for " + fxmlFileName
					+ ": " + e.getMessage());
		}

		viewController = (ViewController) fxmlLoaderInternal.getController();
		viewController.setComponent(this);
		group.getChildren().add(view);
	}

	/**
	 * Events between {@link Component}s are distributed like in the W3C Event
	 * Model. In the bubble phase, the information that data of a certain
	 * {@link Component} changed traverses to the root of the {@link Component}
	 * -tree. If the root is reached, the second phase (Capture Phase) starts.
	 * In this phase, the information is passed down to all {@link Components}
	 * of the tree.
	 * 
	 * @param component
	 *            in which the data change occured
	 */
	public void bubbleDataChanged(Component component) {

		if (parent != null) {
			log.debug("(" + fxmlFileName
					+ ") Bubbling data changed event from " + component
					+ " to " + parent);
			parent.bubbleDataChanged(component);
		} else {
			sendingDataChangedEventToChildren(component);
		}
	}

	/**
	 * Events between {@link Component}s are distributed like in the W3C Event
	 * Model. In the bubble phase, the information that data of a certain
	 * {@link Component} changed traverses to the root of the {@link Component}
	 * -tree. If the root is reached, the second phase (Capture Phase) starts.
	 * In this phase, the information is passed down to all {@link Components}
	 * of the tree.
	 * 
	 * @param component
	 *            in which the data change occured
	 */
	private void sendingDataChangedEventToChildren(Component component) {

		viewController.notifyDataChanged(component);

		log.debug("(" + fxmlFileName + ") Sending data changed event from "
				+ component + " down to " + children.size() + " children.");
		for (Component c : children) {
			c.sendingDataChangedEventToChildren(component);
		}
	}

	public Group getGroup() {
		return group;
	}

	public Node getView() {
		return view;
	}

	public ViewController getViewController() {
		return viewController;
	}

	public List<Component> getChildren() {
		return children;
	}

	@Override
	public String toString() {
		return "Component " + fxmlFileName;
	}
}
