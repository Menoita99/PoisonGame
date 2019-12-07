package game.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import game.controls.Camera;
import game.controls.LevelData;
import game.objects.Background;
import game.objects.CheckPoint;
import game.objects.Platform;
import game.objects.Player;
import game.objects.Portal;
import game.objects.drops.Item;
import game.objects.mechanics.CanvasObject;
import game.objects.mobs.*;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class GameController implements Controllable, Initializable{

	private static final int UPDATE_DISTANCE = 200;
	private static final String GRAPHIC_PATH = "src/main/resources/images";
	//	private static final int DEFAULT_WIDTH =  1260;							//Used in factors
	//	private static final int DEFAULT_HEIGHT = 680;							//Used in factors
	private static final int LAYERS = 5;
	private static final int BLOCKS_SIZE = 30;	
	private double xFactor=1;
	private double yFactor=1;
	private int idCounter = 0;

	private DoubleProperty hellStartHeight    = new SimpleDoubleProperty();
	private DoubleProperty currentLevelWidth  = new SimpleDoubleProperty();
	private DoubleProperty currentLevelHeight = new SimpleDoubleProperty();

	private ManagerController manager;								//scene manager

	private Camera camera;											//camera

	private Map<KeyCode, Boolean> keys = new HashMap<>();			//keys pressed

	private	Map<String, Image> graphics = new HashMap<>();			//graphics

	private AnimationTimer gameLoop;								//loop

	@FXML private StackPane mainPane;								//pane that have the layers(canvas)

	private Player player;											//Player instance

	private	CheckPoint checkPoint;									//Last checkpoint

	private	List<CanvasObject> objects = new ArrayList<>();			//Game Objects

	private	Map<Integer, Canvas> canvas = new HashMap<>();			//layers

	/*LAYERS STRUCT
	 *
	 *0- BACKGROUND
	 *
	 *1- SECRECT DOORS, TRESURES, DROPS, CHECKPOINTS
	 *
	 *2- ENEMIES, PLAYER, PLATFORMS
	 *
	 *3- EFFECTS, e.g: explosion effects
	 *
	 *4- this layer can be for future stuff or can be for display information as player heal/damage
	 */

	
	
	
	
	
	/**
	 * Set's the manager controller
	 */
	@Override
	public void setManagerController(ManagerController mc) {
		manager = mc;
	}

	
	
	
	
	
	/**
	 * When the scene is load is the manager controller JavaFX call this method
	 * This must be used to initialise objects
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		loadGraphics();													//loads Graphics

		gameLoop = new AnimationTimer() {								//setting game loop
			@Override
			public void handle(long now) {
				update();
			}
		};
	}

	
	
	
	
	
	/**
	 * Method called 60 times per second
	 * This is the game loop
	 */
	private void update() {
		for (int layer : canvas.keySet()) {
			Canvas  c = canvas.get(layer);	
			c.getGraphicsContext2D().clearRect(0, 0, c.getWidth(), c.getHeight());		//clear canvas

		}

		for (CanvasObject sprite : objects) {
			Rectangle viewPort = camera.getViewPort();

			if(viewPort.getBoundsInParent().intersects(sprite.getBoundary()) || sprite.getLayer()==0) {	//this renders background(layer 0) and objects that are in viewPort
				GraphicsContext gc = canvas.get(sprite.getLayer()).getGraphicsContext2D();
				sprite.render(gc,viewPort.getLayoutX(),viewPort.getLayoutY());			//draw canvas

			}else if(viewPort.getBoundsInParent().intersects(sprite.getX()-UPDATE_DISTANCE,sprite.getY()-UPDATE_DISTANCE,
					sprite.getWidth()+2*UPDATE_DISTANCE,sprite.getHeight()+2*UPDATE_DISTANCE))	//this updates objects that are at 100 pixels from view port boundary
				sprite.update();
		}
	}

	
	
	
	
	
	/**
	 * Loads images to a map
	 */
	private void loadGraphics() {
		File dir = new File(GRAPHIC_PATH);												//images directory

		for(File imageFile : dir.listFiles()) {
			if(imageFile.getName().matches("([^\\s]+(\\.(?i)(png|jpg|gif|jpeg))$)")) {	//verifies if file is valid
				try {

					graphics.put(imageFile.getName().substring(0,imageFile.getName().lastIndexOf('.')),		//add images to map graphics
							new Image(new FileInputStream(imageFile.getPath())));

				} catch (FileNotFoundException e) {e.printStackTrace();}
			}
		}
	}

	
	
	
	
	
	/**
	 * Create objects giving the bit field
	 */
	private void loadField() {
		int level = (int)manager.getProperty("level");					//get's level

		String [] data =  LevelData.getInstance().getLevel(level);		//get's level map

		currentLevelWidth.set(data[0].length()*xFactor*BLOCKS_SIZE);	//get's the level width
		currentLevelHeight.set(data.length*xFactor*BLOCKS_SIZE);
		
		for (int i = 0; i < data.length; i++)  			
			for (int j = 0; j < data[i].length(); j++)  
				createEntity(data, i, j);

		Background b = new Background(idCounter, graphics.get(Background.getGRAPHIC()),camera);
		objects.add(b);
		idCounter++;
	}

	
	
	
	
	
	/**
	 *Creates CanvasObjects giving the key Code 
	 *this is a CanvasObject factory
	 */
	private void createEntity(String[] data, int i, int j) {
		idCounter++;

		switch (data[i].charAt(j) ) {
		case '-':
			hellStartHeight.set(j*BLOCKS_SIZE);
			return;
		case '0':
			return;
		case '1':	//platform
			CanvasObject platform = new Platform(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter, //Creates platform
					graphics.get(Platform.getGRAPHIC()), BLOCKS_SIZE,BLOCKS_SIZE,this);
			objects.add(platform);																		//add platform to render list
			return;
		case '2':	//Monkey enemy
			CanvasObject monkey = new Monkey(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get(Monkey.getGRAPHIC()), BLOCKS_SIZE, BLOCKS_SIZE, this);
			objects.add(monkey);	
			return;
		case '3':	//Bat enemy
			CanvasObject bat = new Bat(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get(Bat.getGRAPHIC()), BLOCKS_SIZE, BLOCKS_SIZE, this);
			objects.add(bat);	
			return;
		case '4':	//Worm enemy
			CanvasObject worm = new Worm(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get(Worm.getGRAPHIC()), BLOCKS_SIZE, BLOCKS_SIZE, this);
			objects.add(worm);	
			return;
		case '5':	//Player
			player =  new Player(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, 
					idCounter,graphics.get( Player.GRAPHIC ) , BLOCKS_SIZE, BLOCKS_SIZE,this);
			objects.add(player);

			camera = new Camera(manager.getScene(),player,currentLevelWidth,currentLevelHeight);	//setting camera
			return;
		case '6':	//CheckPoint
			CanvasObject checkPoint = new CheckPoint(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get(CheckPoint.GRAPHIC), BLOCKS_SIZE, BLOCKS_SIZE, this);
			objects.add(checkPoint);

			if(this.checkPoint == null) {															//this conditional block sets the first check point
				((CheckPoint)checkPoint).setActive(true);
				this.checkPoint = ((CheckPoint)checkPoint);
				
			}else if(this.checkPoint.getX() > checkPoint.getX()){
				
				this.checkPoint.setActive(false);
				((CheckPoint)checkPoint).setActive(true);
				this.checkPoint = ((CheckPoint)checkPoint);
			}
			
			return;
		case '7':	//Item
			CanvasObject item = new Item(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get(Item.GRAPHIC), BLOCKS_SIZE, BLOCKS_SIZE, this);
			objects.add(item);
			return;
		case '8':   //Portal
			CanvasObject portal = new Portal(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get(Portal.GRAPHIC), BLOCKS_SIZE, BLOCKS_SIZE, this);
			objects.add(portal);	
			return;
		default:
			throw new IllegalArgumentException("Unexpected value: " + data[i].charAt(j) +" invalid entity");
		}
	}

	
	
	
	
	
	/**
	 * Initialise Scene listeners and Canvas
	 */
	private void initListeners() {
		//		xFactor = manager.getScene().getWidth()/DEFAULT_WIDTH;			//Defines factors 	//	TODO use property 	//discuss with NUNO LOBATO	
		//		yFactor = manager.getScene().getHeight()/DEFAULT_HEIGHT;							//	TODO use property

		manager.getScene().setOnKeyPressed(event -> keys.put(event.getCode(), true));		//add Listeners
		manager.getScene().setOnKeyReleased(event -> keys.put(event.getCode(), false));

		for (int i = 0; i < LAYERS ; i++) {								//Initialise canvas
			Canvas c = new Canvas(manager.getScene().getWidth(),manager.getScene().getHeight());
			canvas.put(i,c);

			c.widthProperty().bind(manager.getScene().widthProperty());
			c.heightProperty().bind(manager.getScene().heightProperty());


			mainPane.getChildren().add(canvas.get(i));
		}
	}

	
	
	
	
	
	/**
	 * Checks if a key is pressed
	 */
	public boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
	}

	
	
	
	
	
	/**
	 * Returns all canvasObject at given layer 
	 */
	public List<CanvasObject> getObjectsAtLayer(int layer){
		List<CanvasObject> output = new ArrayList<CanvasObject>();
		for (CanvasObject canvasObject : objects)
			if(canvasObject.getLayer()== layer)			//verifies if they have the same layer
				output.add(canvasObject);		
		return output;
	}

	
	
	
	
	
	public List<CanvasObject> getObjects() {
		return objects;
	}




	/**
	 * @return the currentLevelWidth
	 */
	public DoubleProperty getCurrentLevelWidth() {
		return currentLevelWidth;
	}

	
	
	
	
	
	/**
	 * @return the currentLevelHeight
	 */
	public DoubleProperty getCurrentLevelHeight() {
		return currentLevelHeight;
	}






	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	
	
	
	
	
	/**
	 * @return the hellStartHeight
	 */
	public DoubleProperty getHellStartHeight() {
		return hellStartHeight;
	}

	
	
	
	
	
	/**
	 * Starts game loop
	 */
	public void start() {
		initListeners();
		loadField();		//loads field using graphics
		gameLoop.start();	//Starts gameLoop		
	}

	
	
	
	
	
	/**
	 * Set's the Check Point 
	 */
	public void setCheckPoint(CheckPoint cp) {
		if( checkPoint == null || (cp.isActive() && cp.getX()> checkPoint.getX())) 
			checkPoint = cp;
	}

	
	
	
	

	/**
	 * @return the Check Point 
	 */
	public CheckPoint getCheckPoint() {
		return checkPoint;
	}
	
	
	
	
	
	/**
	 * Removes an entity from game
	 * @param entity entity to be removed
	 */
	public void destroyEntity(CanvasObject entity) {
		objects.remove(entity);
	}
	
	
	
	
	
	/**
	 * Adds an entity from game
	 * @param entity entity to be removed
	 */
	public void addEntity(CanvasObject entity) {
		objects.add(entity);
	}
	
	
	
	
	
	/**
	 * get's idCounter already incremented
	 */
	public int getNewId() {
		return idCounter++;
	}
}
