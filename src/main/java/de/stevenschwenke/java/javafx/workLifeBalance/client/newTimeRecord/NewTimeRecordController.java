package de.stevenschwenke.java.javafx.workLifeBalance.client.newTimeRecord;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import de.stevenschwenke.java.javafx.workLifeBalance.client.Aspect;
import de.stevenschwenke.java.javafx.workLifeBalance.client.Component;
import de.stevenschwenke.java.javafx.workLifeBalance.client.TimeRecord;
import de.stevenschwenke.java.javafx.workLifeBalance.client.ViewController;

public class NewTimeRecordController implements Initializable, ViewController {

	private Component component;

	private NewTimeRecordDao dao;

	@FXML
	// fx:id="aspect"
	private ComboBox<Aspect> aspect; // Value injected by FXMLLoader

	@FXML
	// fx:id="cancel"
	private Button cancel; // Value injected by FXMLLoader

	@FXML
	// fx:id="coloniesLabel"
	private Label coloniesLabel; // Value injected by FXMLLoader

	@FXML
	// fx:id="hours"
	private TextField hours; // Value injected by FXMLLoader

	@FXML
	// fx:id="ok"
	private Button ok; // Value injected by FXMLLoader

	// This method is called by the FXMLLoader when initialization is complete
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		assert aspect != null : "fx:id=\"aspect\" was not injected: check your FXML file 'NewTimeRecord.fxml'.";
		assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'NewTimeRecord.fxml'.";
		assert coloniesLabel != null : "fx:id=\"coloniesLabel\" was not injected: check your FXML file 'NewTimeRecord.fxml'.";
		assert hours != null : "fx:id=\"hours\" was not injected: check your FXML file 'NewTimeRecord.fxml'.";
		assert ok != null : "fx:id=\"ok\" was not injected: check your FXML file 'NewTimeRecord.fxml'.";

		aspect.setCellFactory(new Callback<ListView<Aspect>, ListCell<Aspect>>() {

			public ListCell<Aspect> call(ListView<Aspect> arg0) {
				final ListCell<Aspect> cell = new ListCell<Aspect>() {
					{
						super.setPrefWidth(100);
					}

					@Override
					public void updateItem(Aspect item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getName());
						}
					}
				};
				return cell;
			}
		});

	}

	@FXML
	public void ok() {
		String hoursText = hours.getText();
		// TODO Validieren
		TimeRecord newRecord = new TimeRecord((Aspect) aspect
				.getSelectionModel().getSelectedItem(),
				Integer.parseInt(hoursText));

		dao.addNewTimeRecord(newRecord);

		component.bubbleDataChanged(component);

		component.getGroup().getChildren().remove(component.getView());
	}

	public void setComponent(Component component) {
		this.component = component;

	}

	public void setDao(NewTimeRecordDao dao) {
		this.dao = dao;
		aspect.setItems(dao.getAllAspects());
	}

	public void notifyDataChanged(Component component) {

	}
}
