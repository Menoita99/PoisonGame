package game.objects.ui;

import game.controllers.GameController;
import game.objects.Player;
import game.objects.mechanics.UIObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class StatusBar implements UIObject {
	
	private static final int HEIGHT = 30;
	
	private static final int HP_CURVATURE = 10;
	private static final int HP_WIDTH = 100;
	private static final int DEFUALT_HEIGHT = 15;
	
	private static final int Y_DISTANCE_TO_BORDER = 15;
	private static final int X_DISTANCE_TO_BORDER = 15;

	private Player player;
	private GameController controller;
	
	private Rectangle healBox;
	private Rectangle heal;
	
//	private Rectangle coins;
	
//	private Rectangle keys;
	
	private Rectangle score;
	
	private Rectangle screen;
	
	private Font font = Font.font("Arial Black", FontWeight.NORMAL, 16);




	
	public StatusBar(Player player, Rectangle viewPort, GameController controller ) {
		this.player = player;
		this.screen = viewPort;
		this.controller = controller;
		
		Text t = new Text();
		t.setFont(font);
		//score
		t.setText("00000000");	//Makes space for score up to 99999999
		score  = new Rectangle(0,0, t.getLayoutBounds().getWidth(), t.getLayoutBounds().getHeight());
		
		initListeners();
	}	




	
	/**
	 * Initialise responsive listeners
	 */
	private void initListeners() {
		initHealBar();
		initScore();
	}




	//Score
	/**
	 * Initiates health bar listeners
	 */
	private void initScore() {
		int ratioConst = 10;
		
		score.setLayoutX(X_DISTANCE_TO_BORDER);
		score.setLayoutX(Y_DISTANCE_TO_BORDER+10);
		
		screen.layoutXProperty().addListener((obs, old, newValue) -> score.setLayoutX(newValue.doubleValue()+X_DISTANCE_TO_BORDER));
		screen.layoutYProperty().addListener((obs, old, newValue) -> score.setLayoutY(newValue.doubleValue()+Y_DISTANCE_TO_BORDER + ratioConst));
	}




	
	/**
	 * Initiates health bar listeners
	 */
	private void initHealBar() {
		healBox = new Rectangle(0,0, HP_WIDTH, DEFUALT_HEIGHT);
		healBox.setLayoutX(screen.getWidth()-HP_WIDTH-X_DISTANCE_TO_BORDER);
		healBox.setLayoutY(Y_DISTANCE_TO_BORDER);
		
		heal    = new Rectangle(0,0, HP_WIDTH, DEFUALT_HEIGHT);
		heal.setLayoutX(screen.getWidth()-HP_WIDTH-X_DISTANCE_TO_BORDER);
		heal.setLayoutY(Y_DISTANCE_TO_BORDER);
		
		screen.layoutXProperty().addListener((obs, old, newValue) -> {	
			healBox.setLayoutX(newValue.doubleValue()+screen.getWidth()-healBox.getWidth() - X_DISTANCE_TO_BORDER);
			heal.setLayoutX(newValue.doubleValue()+screen.getWidth()-healBox.getWidth() - X_DISTANCE_TO_BORDER);
		});
		
		screen.layoutYProperty().addListener((obs, old, newValue) -> {	
			healBox.setLayoutY(newValue.doubleValue() + Y_DISTANCE_TO_BORDER);
			heal.setLayoutY(newValue.doubleValue() + Y_DISTANCE_TO_BORDER);
		});
	}




	/**
	 * This method tell canvas how he must draw objects
	 */
	@Override
	public void render(GraphicsContext gc, double xOffSet, double yOffSet) {
		update();
		
		//HEALTH BAR
		gc.setFill(Color.GRAY);
		gc.fillRoundRect(healBox.getLayoutX()-xOffSet, healBox.getLayoutY()-yOffSet, healBox.getWidth(), healBox.getHeight(), HP_CURVATURE, HP_CURVATURE);
		gc.setFill(Color.RED);
		gc.fillRoundRect(heal.getLayoutX()-xOffSet, heal.getLayoutY()-yOffSet, heal.getWidth(), heal.getHeight(), HP_CURVATURE, HP_CURVATURE);
		
		//SCORE
		gc.setFill(Color.WHITE);
		gc.setFont(font);
		gc.fillText(controller.getScore()+"", score.getLayoutX()-xOffSet, score.getLayoutY()-yOffSet);
		
	}




	
	/**
	 * Updates heal bar with player heal
	 */
	private void update() {
		heal.setWidth(player.getHP());
	}
	



	
	@Override
	public int getLayer() {
		return 4;
	}




	
	@Override
	public double getX() {
		return screen.getLayoutX();
	}




	
	@Override
	public double getY() {
		return screen.getLayoutY();
	}




	
	@Override
	public double getWidth() {
		return screen.getWidth();
	}




	
	@Override
	public double getHeight() {
		return HEIGHT;
	}
}