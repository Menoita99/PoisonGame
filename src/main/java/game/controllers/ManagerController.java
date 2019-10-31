package game.controllers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ManagerController extends Application {

	private static final int WIDTH = 1260;
	private static final int HEIGHT = 680;
	private static final String SCENES_DIR = "src/main/java/game/controllers";				//Scenes directory

	private Map<String,Parent> scenes = new HashMap<>();   				//Store the load nodes of scenes
	private Map<String,Controllable> controllers = new HashMap<>();		//Store the controllers

	private Map<String, Object> propertyMap = new HashMap<>();		//Stores attributes used to communicate between scenes
	
	private Pane root;													//root pane

	private Scene scene;												//game scene
	private Stage window;												//game window
	
	
	
	/**
	 *Changes Scene
	 *if there is no scene with the key @param sceneName  
	 *this is throw an IllegalArgumentExeption
	 */
	public void setRoot(String sceneName) {
		if(scenes.containsKey(sceneName)) {
			root.getChildren().clear();
			root.getChildren().add(scenes.get(sceneName));
		}
		else
			throw new IllegalArgumentException("Scene: "+sceneName+ "  does not exists");
	}
	
	
	/**
	 * Set's a new attribute.
	 * if there was already an attribute with the current key, 
	 * this method will override it
	 */
	public void setProperty(String key,Object value) {
		propertyMap.put(key, value);
	}
	
	
	/**
	 * Returns the attribute stored with the key given
	 */
	public Object getProperty(String key) {
		return propertyMap.get(key);
	}
	
	/**
	 *@return the Scene
	 */
	public Scene getScene() {
		return scene;
	}
	
//	/**
//	 * @return the window
//	 */
//	public Stage getWindow() {
//		return window;
//	}
	

	/**
	 * Return a controller
	 */
	public Controllable getController(String key) {
		return controllers.get(key);
	}


	/**
	 * This method starts the engine and the platform Thread
	 * 
	 * The Platform Thread is here the User interface will be executed
	 * all the GUI actions must be executed in this Thread (like swing)
	 * 
	 * You can use inside controllers the Method Plataform.runLater(Runnable r ); when 
	 * you need to execute an GUI action that is not called by Platform Thread
	 */
	@Override
	public void start(Stage stage) throws Exception {
		window = stage;

		File dir = new File(SCENES_DIR);

		for(File sceneFile : dir.listFiles()) {
			if(sceneFile.getName().matches("([^\\s]+(\\.(?i)(fxml))$)")) {			//Verifies if is fxml file

				FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneFile.getName()));	//Loads fxml files
				Parent root = loader.load();

				Controllable controller= (Controllable) loader.getController();
				controller.setManagerController(this);

				String sceneName = sceneFile.getName();											
				scenes.put(sceneName.substring(0, sceneName.lastIndexOf(".")),root);		//Saves Scene name e.g: mainMenuScene
				
				String className = controller.getClass().getName();							//Saves Controller name e.g: MainMenuController
				controllers.put(className.substring(className.lastIndexOf(".")+1,className.length()).trim(), controller);	
				
			}
		}

		root = new Pane();
		root.getChildren().add(scenes.get("mainMenuScene"));
		
		scene = new Scene(root, WIDTH, HEIGHT);	//set scenes
		
		//window.setResizable(false);
		window.setTitle("Poison");
		window.setScene(scene);
		window.show();						//Displays scene
	}


	public static void main(String[] args) {
		launch(args);
	}
}
