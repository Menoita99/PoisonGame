package game.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class LevelMenuController implements Controllable, Initializable {


	private ManagerController manager;


	/**
	 * Set's the manager controller
	 */
	@Override
	public void setManagerController(ManagerController mc) {
		manager = mc;
	}

	
	/**
	 * Method called by buttons when pressed
	 */
	@FXML
	void selectLevel(ActionEvent event) {
		int level = Integer.parseInt(((Button)event.getSource()).getText());		//get button level
		manager.setProperty("level", level);										//Set's global property
		manager.setRoot("gameScene");												//Set's next scene
		((GameController)manager.getController("GameController")).start();			//Starts GameController loop
	}


	/**
	 * When the scene is load is the manager controller JavaFX call this method
	 * This must be used to initialise objects
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}


}
