package game.objects;

import game.controllers.GameController;
import javafx.scene.image.Image;

public class CheckPoint extends CanvasObject {
	
	public static final String GRAPHIC = "";
	public static int LAYER = 1;
	
	private boolean isActive = false;
	private GameController controller;
	
	
	
	
	public CheckPoint(double x, double y, int id, Image graphicImage, double width, double height, GameController controller) {
		super(x, y, id, graphicImage, width, height, LAYER);
		this.controller = controller;
	}

	
	
	
	@Override
	public void update() {
		if(controller.getPlayer().intersects(this)) {
			System.out.println("TOUCHING CheckPoint");
			setActive(true);
		}
	}

	
	

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
