package game.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class MainMenuController implements Initializable, Controllable {

	
	private ManagerController manager;
	
	@FXML private Button loadbut;
	
	
	/**
	 * Set's the manager controller
	 */
	@Override
	public void setManagerController(ManagerController mc) {
		manager = mc;
	}
	
    @FXML
    void loadGame(ActionEvent event) {
    	
    }

    @FXML
    void newGame(ActionEvent event) {
    	manager.setRoot("levelMenuScene");
    }
	
	/**
	 * When the scene is load is the manager controller JavaFX call this method
	 * This must be used to initialise objects
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}


}
