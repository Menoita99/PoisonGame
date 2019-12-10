package game.objects.mobs;

import java.util.ArrayList;
import java.util.List;

import game.algorithms.Algorithm;
import game.controllers.GameController;
import game.objects.drops.Item;
import game.objects.mechanics.CanvasObject;
import game.objects.mechanics.Damageable;
import game.objects.mechanics.Movable;
import game.objects.mechanics.Strikable;
import javafx.scene.image.Image;

public abstract class Enemy extends Movable implements Strikable, Damageable {

	private static final int LAYER = 2;
	
	private List<Image> images = new ArrayList<>();
	private boolean directionChanged = false;			//Useful for motion images

	private boolean isLeft = false;						//Useful for motion images
	private double xVelocity = 5;						//pixel per frame
	
	private double healPoints;								
		
	private GameController controller;	
	
	public static final double BASE_DMG = 25.0;
	public static final double RANGE_DMG = 5;	
	
	private static final double BASE_HP = 50;
	private static final double HP_RATIO = 0.20;

	
	
	
	
	public Enemy(double x, double y, int id, Image graphicImage, double width, double height,double hp, GameController controller) {
		super(x, y, id, graphicImage, width, height, LAYER, controller.getCurrentLevelWidth());
		this.controller = controller;
		
		double hpMedia = Math.max(BASE_HP+hp,0);
		double hpRange = hpMedia * HP_RATIO;
		healPoints = Algorithm.normal(hpMedia, Math.sqrt(hpRange), hpMedia-hpRange, hpMedia+hpRange);
	}

	
	
	
	
	//abstract methods
	
	
	
	/**
	 * This method must implement the entity motion, the idea of movement
	 */
	public abstract void motion();

	
	
	
	
	//Movement methods
	
	
	/**
	 * This method moves the entity to Right 
	 * if it collides with something it changes direction (go to the left)
	 */
	public void moveRigth() {
		
		for (double i = 0; i < Math.abs(xVelocity); i++) {

			for(CanvasObject entity : getController().getObjectsAtLayer(getLayer())) {

				if((entity.intersects(this) && !entity.equals(this))|| getX()+getWidth()+1 >= getLevelWidth()-1){	 //checks collision and checks if entity is it self

					if(entity.getBoundary().intersects(getX()+1, getY()+1, getWidth(), getHeight()-2) ||
							getX()+getWidth()+1 >= getLevelWidth()-1){											//checks if this is near level bounds
						
						isLeft=true;
						directionChanged = true;
						return;
					}
				}
			}
		}
		setX(getX() + 1);			//move 1 pixel
	}



	
	
	/**
	 * This method moves to the entity left 
	 * if it collides with something it changes direction (go to the right)
	 */
	public void moveLeft() {
		
		for (double i = 0; i < Math.abs(xVelocity); i++) {

			for(CanvasObject entity : getController().getObjectsAtLayer(getLayer())) {

				if((entity.intersects(this) && !entity.equals(this)) || getX()-1 <= 0){									//checks collision and checks if entity is it self

					if(entity.getBoundary().intersects(getX()-1, getY()+1, getWidth(), getHeight()-2) || getX()-1 <= 0){	//checks if this is near level bounds
						
						isLeft=false;
						directionChanged = true;
						return;
					}
				}
			}
		}
		setX(getX() - 1);		//moves 1 pixel

	}
	
	
	
	
	
	
	
	/**
	 *This method drops an item when called
	 */
	public void dropItem() {
		Item drop = new Item(getX(), getY(), getController().getNewId(), getController().getGraphic(Item.GRAPHIC), 
							GameController.BLOCKS_SIZE, GameController.BLOCKS_SIZE, getController());
		getController().addEntity(drop);
	}
	
	
	
	//GETTERS AND SETTERS
	
	/**
	 * @return the healPoints
	 */
	public double getHealPoints() {
		return healPoints;
	}

	
	
	
	
	/**
	 * @param healPoints the healPoints to set
	 */
	public void setHealPoints(double healPoints) {
		this.healPoints = healPoints;
	}

	
	
	
	
	/**
	 * @return the controller
	 */
	public GameController getController() {
		return controller;
	}

	
	
	
	
	/**
	 * @return the images
	 */
	public List<Image> getImages() {
		return images;
	}

	/**
	 * @param images the images to set
	 */
	public void setImages(List<Image> images) {
		this.images = images;
	}

	
	
	
	
	/**
	 * @return the directionChanged
	 */
	public boolean isDirectionChanged() {
		return directionChanged;
	}

	
	
	
	
	/**
	 * @param directionChanged the directionChanged to set
	 */
	public void setDirectionChanged(boolean directionChanged) {
		this.directionChanged = directionChanged;
	}

	
	
	
	
	/**
	 * @return the xVelocity
	 */
	public double getXVelocity() {
		return xVelocity;
	}

	
	
	
	
	/**
	 * @param xVelocity the xVelocity to set
	 */
	public void setXVelocity(double xVelocity) {
		this.xVelocity = xVelocity;
	}

	
	
	
	
	/**
	 * @return the isLeft
	 */
	public boolean isLeft() {
		return isLeft;
	}

	
	
	
	
	/**
	 * @param isLeft the isLeft to set
	 */
	public void setLeft(boolean isLeft) {
		this.isLeft = isLeft;
	}
}