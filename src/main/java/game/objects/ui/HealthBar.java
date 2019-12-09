package game.objects.ui;

import game.objects.Player;
import game.objects.mechanics.UIObject;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBar implements UIObject {
	
	
	private static final int CURVATURE = 10;
	private static final int WIDTH = 100;
	private static final int HEIGHT = 15;
	private static final int Y_DISTANCE_TO_BORDER = 15;
	private static final int X_DISTANCE_TO_BORDER = 15;

	private Player player;
	
	private Rectangle2D box;
	private Rectangle2D heal;
	
	private Rectangle screen;
	
	private SimpleDoubleProperty xProperty = new SimpleDoubleProperty();
	private SimpleDoubleProperty yProperty = new SimpleDoubleProperty();

	
	
	
	
	
	public HealthBar(Player player, Rectangle viewPort ) {
		this.player = player;
		this.screen = viewPort;
		
		box = new Rectangle2D(0, 0, WIDTH, HEIGHT);
		heal = new Rectangle2D(0, 0, 100, HEIGHT);
		
		initListeners();
	}	

	
	
	
	
	/**
	 * Initialise responsive listeners
	 */
	private void initListeners() {
		
		xProperty.set(screen.getWidth()-box.getWidth()-X_DISTANCE_TO_BORDER);
		
		screen.layoutXProperty().addListener((obs, old, newValue)->{
			double x = newValue.doubleValue() + screen.getWidth()-box.getWidth()-X_DISTANCE_TO_BORDER;
			xProperty.set(x);
		});
		
		screen.widthProperty().addListener((obs, old, newValue)->{		//Reposition the health bar
			xProperty.set(newValue.doubleValue()-box.getWidth()-X_DISTANCE_TO_BORDER);
		});
		
		yProperty.bind(screen.layoutYProperty().add(Y_DISTANCE_TO_BORDER));
	}

	
	
	
	
	/**
	 * Updates heal bar with player heal
	 */
	private void update() {
		heal = new Rectangle2D(0, 0, Math.max(player.getHP(),0), getHeight());
	}

	
	
	
	
	@Override
	public void render(GraphicsContext gc, double xOffSet, double yOffSet) {
		update();
		gc.setFill(Color.GREY);
		gc.fillRoundRect(getX()-xOffSet, getY()-yOffSet, getWidth(), getHeight(), CURVATURE, CURVATURE);
		gc.setFill(Color.RED);
		gc.fillRoundRect(getX()-xOffSet, getY()-yOffSet, heal.getWidth(), getHeight(),CURVATURE,CURVATURE);
	}

	
	
	
	
	@Override
	public int getLayer() {
		return 4;
	}

	
	
	
	
	@Override
	public double getX() {
		return xProperty.get();
	}

	
	
	
	
	@Override
	public double getY() {
		return yProperty.get();
	}

	
	
	
	
	@Override
	public double getWidth() {
		return WIDTH;
	}

	
	
	
	
	@Override
	public double getHeight() {
		return HEIGHT;
	}
}
