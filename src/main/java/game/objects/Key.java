package game.objects;

import game.controllers.GameController;
import game.objects.mechanics.CanvasObject;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Key extends CanvasObject {
	
	public static final String GRAPHIC = "yellowKey";
	public static int LAYER = 1;
	private GameController controller;
	private Color color;
	

	public Key(double x, double y, int id, Image graphicImage, double width, double height,Color color,GameController controller) {
		super(x, y, id, graphicImage, width*2/3, height*1/3, LAYER);
		this.color = color;
		this.controller = controller;
	}

	
	
	
	
	@Override
	public void update() {
		if(controller.getPlayer().intersects(this)) {
			controller.getPlayer().addKey(this);		//adds this key to player keys
			controller.destroyEntity(this);
		}
	}





	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
}
