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
import java.util.concurrent.CopyOnWriteArrayList;

import game.controls.Camera;
import game.controls.LevelData;
import game.objects.Background;
import game.objects.Platform;
import game.objects.Player;
import game.objects.Thorn;
import game.objects.drops.Coin;
import game.objects.drops.Item;
import game.objects.drops.WoodBox;
import game.objects.gameLogic.CheckPoint;
import game.objects.gameLogic.Door;
import game.objects.gameLogic.Key;
import game.objects.gameLogic.Lock;
import game.objects.gameLogic.Portal;
import game.objects.mechanics.CanvasObject;
import game.objects.mechanics.UIObject;
import game.objects.mobs.*;
import game.objects.ui.StatusBar;
import javafx.animation.AnimationTimer;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class GameController implements Controllable, Initializable{

	private static final int UPDATE_DISTANCE = 200;
	private static final String GRAPHIC_PATH = "src/main/resources/images";
	private static final int LAYERS = 5;
	public static final int BLOCKS_SIZE = 30;	
	private double xFactor=1;
	private double yFactor=1;
	private int idCounter = 0;

	private DoubleProperty hellStartHeight    = new SimpleDoubleProperty();
	private DoubleProperty currentLevelWidth  = new SimpleDoubleProperty();
	private DoubleProperty currentLevelHeight = new SimpleDoubleProperty();

	private ManagerController manager;								//scene manager
	private Camera camera;											

	private Map<KeyCode, Boolean> keys  = new HashMap<>();			//keys pressed list
	private	Map<String, Image> graphics = new HashMap<>();			
	private	Map<Integer, Canvas> canvas = new HashMap<>();			//layers

	private Map<Point2D,Platform> platforms = new HashMap<>();
	
	private AnimationTimer gameLoop;								

	@FXML private StackPane mainPane;								//pane that have the layers(canvas)

	private Player player;											
	private	CheckPoint checkPoint;									//Last checkpoint

	private	List<UIObject> objects = new CopyOnWriteArrayList<>();	//Game Objects (render list)
	
	private int score;											



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
	 *4- PLAYER STATS, DMG
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
		for (int layer : canvas.keySet()) {	//clear canvas
			Canvas  c = canvas.get(layer);	
			c.getGraphicsContext2D().clearRect(0, 0, c.getWidth(), c.getHeight());		
		}

		Rectangle viewPort = camera.getViewPort();

		for (UIObject uiObj : objects) {	//render other objects
			
			if(uiObj instanceof CanvasObject) {
				CanvasObject sprite = (CanvasObject)uiObj;
			
				if(viewPort.getBoundsInParent().intersects(sprite.getBoundary()) || sprite.getLayer()==0) {	//this renders background(layer 0) and objects that are in viewPort
					GraphicsContext gc = canvas.get(sprite.getLayer()).getGraphicsContext2D();
					sprite.render(gc,viewPort.getLayoutX(),viewPort.getLayoutY());			//draw canvas
	
				}else if(viewPort.getBoundsInParent().intersects(sprite.getX()-UPDATE_DISTANCE,sprite.getY()-UPDATE_DISTANCE, 
						sprite.getWidth()+2*UPDATE_DISTANCE,sprite.getHeight()+2*UPDATE_DISTANCE))	
					sprite.update();
				
			}else {
				if(viewPort.getBoundsInParent().intersects(uiObj.getX(),uiObj.getY(),uiObj.getWidth(),uiObj.getHeight()))
					uiObj.render(canvas.get(uiObj.getLayer()).getGraphicsContext2D(), viewPort.getLayoutX(),viewPort.getLayoutY());
			}
		}
		
		for(Point2D coord : platforms.keySet()) { //render platforms
			Platform plat = platforms.get(coord);
			if(viewPort.getBoundsInParent().intersects(plat.getBoundary()))
				plat.render(canvas.get(plat.getLayer()).getGraphicsContext2D(), viewPort.getLayoutX(),viewPort.getLayoutY());
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

		objects.clear();
		platforms.clear();

		for (int i = 0; i < data.length; i++)  			
			for (int j = 0; j < data[i].length(); j++)  
				createEntity(data, i, j);

		Background b = new Background(idCounter, graphics.get(Background.getGRAPHIC()),camera,this);
		objects.add(b);
		idCounter++;
		
		score = 0;
	}






	/**
	 *Creates CanvasObjects giving the key Code 
	 *this is a CanvasObject factory
	 */
	private void createEntity(String[] data, int i, int j) {
		idCounter++;

		switch (data[i].charAt(j) ) {
		case '-':
			hellStartHeight.set(i*BLOCKS_SIZE);
			return;
		case '0':
			return;
		case '1':	//platform
			CanvasObject platform = new Platform(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter, 
					graphics.get(Platform.getGRAPHIC()), BLOCKS_SIZE,BLOCKS_SIZE,this);
			platforms.put(new Point2D(j, i), (Platform) platform);																		
			return;
		case '2':	//Monkey enemy
			CanvasObject monkey = new Monkey(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get(Monkey.GRAPHIC), BLOCKS_SIZE, BLOCKS_SIZE, this);
			objects.add(monkey);	
			return;
		case '3':	//Bat enemy
			CanvasObject bat = new Bat(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get(Bat.GRAPHIC), BLOCKS_SIZE, BLOCKS_SIZE, this);
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
			
			objects.add(new StatusBar(player, camera.getViewPort(), this));
			return;
		case '6':	//CheckPoint
			createCheckPoint(i, j);
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
		case '9':   //Door
			CanvasObject flag = new Door(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get(Door.GRAPHIC), BLOCKS_SIZE, BLOCKS_SIZE, this);
			objects.add(flag);	
			return;
		case 'a':   //yellow key
			CanvasObject ykey = new Key(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get("yellow"+Key.GRAPHIC), BLOCKS_SIZE, BLOCKS_SIZE, Color.YELLOW, this);
			objects.add(ykey);	
			return;
		case 'b':   //yellow lock
			CanvasObject ylock = new Lock(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get("yellow"+Lock.GRAPHIC), BLOCKS_SIZE, BLOCKS_SIZE, Color.YELLOW, this);
			objects.add(ylock);	
			return;
		case 'c':   //blue key
			CanvasObject bkey = new Key(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get("blue"+Key.GRAPHIC), BLOCKS_SIZE, BLOCKS_SIZE, Color.BLUE, this);
			objects.add(bkey);	
			return;
		case 'd':   //blue lock
			CanvasObject block = new Lock(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get("blue"+Lock.GRAPHIC), BLOCKS_SIZE, BLOCKS_SIZE, Color.BLUE, this);
			objects.add(block);	
			return;
			
		//e f g h  (reserved for red lock , red key, green lock , green key) 
			
		case 'i':   //Coin
			CanvasObject coin= new Coin(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get(Coin.GRAPHIC), BLOCKS_SIZE, BLOCKS_SIZE, this);
			objects.add(coin);	
			return;
		case 'j':	//Rat enemy
			CanvasObject rat = new Rat(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get(Rat.GRAPHIC), BLOCKS_SIZE, BLOCKS_SIZE, this);
			objects.add(rat);	
			return;
		case 'k':	//thorn
			CanvasObject thorn = new Thorn(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get(Thorn.GRAPHIC), BLOCKS_SIZE, BLOCKS_SIZE, this);
			objects.add(thorn);	
			return;
		case 'l':	//box
			CanvasObject woodBox = new WoodBox(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter,
					graphics.get( WoodBox.GRAPHIC), BLOCKS_SIZE, BLOCKS_SIZE, this);
			objects.add(woodBox);	
			return;
		default:
			throw new IllegalArgumentException("Unexpected value: " + data[i].charAt(j) +" invalid entity");
		}
	}






	/**
	 * Creates checkPoint instance and sets controller checkpoint to the first comes in x coordinate
	 */
	private void createCheckPoint(int i, int j) {
		CanvasObject checkPoint = new CheckPoint(j*xFactor*BLOCKS_SIZE, i*yFactor*BLOCKS_SIZE, idCounter, graphics.get(CheckPoint.GRAPHIC), BLOCKS_SIZE, BLOCKS_SIZE, this);
		objects.add(checkPoint);

		if(this.checkPoint == null) {															//this conditional block sets the first check point
			((CheckPoint)checkPoint).setActive(true);
			this.checkPoint = ((CheckPoint)checkPoint);

		}else if(this.checkPoint.getX() > checkPoint.getX()){

			this.checkPoint.setActive(false);
			((CheckPoint)checkPoint).setActive(true);
			this.checkPoint = ((CheckPoint)checkPoint);
		}
	}






	/**
	 * Initialise Scene listeners and Canvas
	 */
	private void initListeners() {
		manager.getScene().setOnKeyPressed(event -> keys.put(event.getCode(), true));		//add Listeners
		manager.getScene().setOnKeyReleased(event -> keys.put(event.getCode(), false));

		mainPane.getChildren().clear();

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
		for (UIObject canvasObject : objects)
			if(canvasObject instanceof CanvasObject)
				if(canvasObject.getLayer()== layer)			//verifies if they have the same layer
					output.add((CanvasObject)canvasObject);		
		return output;
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
	 * Ends Level and saves status
	 * @param won if won equals true means level was passed with success
	 */
	public void endGame(boolean won) {
		gameLoop.stop();
		//		IOManager.getIntance.saveProgress(this)
		displayEndGameAnimation(won);
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
	 * @return the score
	 */
	public int getScore() {
		return score;
	}






	/**
	 * This method returns the platforms that intercepts with the given boundary 
	 * This only works with w<= BLOCKS_SIZE and h<= BLOCKS_SIZE
	 */
	public List<Platform> getPlatforms(double x , double y , double w , double h) {
		//TODO fix: only works with w<= BLOCKS_SIZE and h<= BLOCKS_SIZE
		List<Platform> output = new ArrayList<>();
		
		Point2D upperLeftPoint = new Point2D((int)(x/BLOCKS_SIZE),(int)(y/BLOCKS_SIZE));
		Point2D upperRightPoint = new Point2D((int)((x+w)/BLOCKS_SIZE),(int)(y/BLOCKS_SIZE));
		Point2D bottomLeftPoint = new Point2D((int)(x/BLOCKS_SIZE),(int)((y+h)/BLOCKS_SIZE));
		Point2D bottomRightPoint = new Point2D((int)((x+w)/BLOCKS_SIZE),(int)((y+h)/BLOCKS_SIZE));
		
		if(platforms.containsKey(upperLeftPoint)) output.add(platforms.get(upperLeftPoint));
		if(platforms.containsKey(upperRightPoint)) output.add(platforms.get(upperRightPoint));
		if(platforms.containsKey(bottomLeftPoint)) output.add(platforms.get(bottomLeftPoint));
		if(platforms.containsKey(bottomRightPoint)) output.add(platforms.get(bottomRightPoint));
		
		return output;
	}






	/**
	 * This method returns the platforms that intercepts with the given point
	 */
	public List<Platform> getPlatforms(Point2D p) {
		List<Platform> output = new ArrayList<>();
		
		Point2D point = new Point2D((int)(p.getX()/BLOCKS_SIZE),(int)(p.getY()/BLOCKS_SIZE));

		if(platforms.containsKey(point)) output.add(platforms.get(point));
		
		return output;
	}






	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
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





	/**
	 * @param graphic image name
	 * @return returns the image with the name given
	 */
	public Image getGraphic(String graphic) {
		return graphics.get(graphic);
	}



	
	
	/**
	 * This method will display a text message at layer 4, at coordinates x, y 
	 *  with the given colour and will disappear after time milliseconds
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param text text to be displayed
	 * @param time time in milli 
	 * @param color text color
	 */
	public void writeText(double x, double y, String text, long time,Color color) {
		Font font = Font.font("Arial Black", FontWeight.BOLD, 14);
		
		UIObject obj = new UIObject() {
			
			@Override
			public void render(GraphicsContext gc, double xOffSet, double yOffSet) {
				gc.setFill(color);
				gc.setFont(font);
				gc.fillText(text, getX()-xOffSet, getY()-yOffSet);
				gc.setFill(Color.BLACK);
			}
			
			@Override
			public double getY() {
				return y;
			}
			
			@Override
			public double getX() {
				return x;
			}
			
			@Override
			public double getWidth() {
				Text t = new Text(text);
				t.setFont(font);
				return t.getLayoutBounds().getWidth();
			}
			
			@Override
			public int getLayer() {
				return canvas.size()-1;
			}
			
			@Override
			public double getHeight() {
				Text t = new Text(text);
				t.setFont(font);
				return t.getLayoutBounds().getHeight();
			}
		};
		
		objects.add(obj);
		new Thread(() -> {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) { e.printStackTrace(); }
			objects.remove(obj);
		}).start();
	}
	
	
	



	/**
	 * End Game Animation
	 */
	private void displayEndGameAnimation(boolean won) {
		manager.setResizable(false);
		
		double sceneWidth = manager.getScene().getWidth();
		double sceneHeight = manager.getScene().getHeight();
		
		AnchorPane anchor = new AnchorPane();
		anchor.setMinSize(sceneWidth, sceneHeight);
		
		AnchorPane topPane = new AnchorPane();
		topPane.setPrefSize(sceneWidth,sceneHeight/2);
		topPane.setMinSize(sceneWidth,sceneHeight/2);
		topPane.setMaxSize(sceneWidth,sceneHeight/2);
		topPane.setStyle("-fx-background-color:#34baeb");
		topPane.setLayoutX(0);
		topPane.setLayoutY(-sceneHeight/2 );
		
		
		AnchorPane botPane = new AnchorPane();
		botPane.setPrefSize(sceneWidth,sceneHeight/2);
		botPane.setMinSize(sceneWidth,sceneHeight/2);
		botPane.setMaxSize(sceneWidth,sceneHeight/2);
		botPane.setStyle("-fx-background-color:#c2c4c4;");
		botPane.setLayoutX(0);
		botPane.setLayoutY(sceneHeight);
		
		anchor.getChildren().addAll(botPane,topPane);
		
		mainPane.setMaxHeight(sceneHeight);
		mainPane.getChildren().add(anchor);
		
		TranslateTransition translationTopToBot = new TranslateTransition();
		translationTopToBot.setNode(topPane);
		translationTopToBot.setFromY(0);
		translationTopToBot.setToY(sceneHeight/2);
		translationTopToBot.setDuration(Duration.millis(3000));
		translationTopToBot.setAutoReverse(false);
		
		TranslateTransition translationBotToTop = new TranslateTransition();
		translationBotToTop.setNode(botPane);
		translationBotToTop.setFromY(0);
		translationBotToTop.setToY(-sceneHeight/2);
		translationBotToTop.setDuration(Duration.millis(3000));
		translationBotToTop.setAutoReverse(false);
		
		
		ParallelTransition p = new ParallelTransition(translationBotToTop,translationTopToBot);
		p.play();
		
		
		p.setOnFinished((evt) -> {
			
			HBox hb = new HBox();	

			Region hbleft = new Region();
			Region hbright = new Region();

			Region vbup = new Region();
			vbup.setPrefSize(250, 160);

			Region vbdown = new Region();

			VBox.setVgrow(hbleft, Priority.ALWAYS);
			VBox.setVgrow(hbright, Priority.ALWAYS);

			HBox.setHgrow(hbleft, Priority.ALWAYS);
			HBox.setHgrow(hbright, Priority.ALWAYS);

			VBox endPane = new VBox();
			endPane.setMinWidth(210);

			Label label = new Label(won ? "LEVEL PASSED!!!" : "GAME OVER");
			label.setTextFill(Color.WHITE);
			label.setMinWidth(210);
			label.setTextAlignment(TextAlignment.CENTER);
			label.setFont(Font.font(100));

			Button lvlMenuButton = new Button("Return to main menu");
			lvlMenuButton.setOnAction((evt1) ->{ 
				mainPane.setMaxHeight(manager.getScreenSize().getHeight());
				manager.setRoot("levelMenuScene");
				manager.setResizable(true);
			});
			
			Button nextLvlButton = new Button("Next Level");
			nextLvlButton.setOnAction((evt2) -> {
				manager.setProperty("level", (int)manager.getProperty("level")+1);	//THIS WILL GIVE AN ERROR WHEN IT'S THE LAST LVL
				start();
			});

			
			Button retryButton = new Button("Retry");	//retry button
			retryButton.setOnAction((evt2) -> start());
			
			hb.getChildren().addAll(hbleft,endPane,hbright);
			endPane.getChildren().addAll(vbup,label,lvlMenuButton);

			if(won)
				endPane.getChildren().addAll(nextLvlButton,vbdown);
			else
				endPane.getChildren().addAll(retryButton,vbdown);
			
			mainPane.getChildren().add(hb);
		});
	}
}
