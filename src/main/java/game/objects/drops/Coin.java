package game.objects.drops;

import game.controllers.GameController;
import game.objects.mechanics.CanvasObject;
import javafx.scene.image.Image;

public class Coin extends CanvasObject {
	
	public static String GRAPHIC= "goldCoin";
	
	private static final int POINTS = 10;
	private static final int LAYER = 1;
	
	private GameController controller;
	
	
	
	

	public Coin(double x, double y, int id, Image graphicImage, double width, double height, GameController controller) {
		super(x+(width/2)/2, y+(height/2)/2, id, graphicImage, width/2, height/2, LAYER);
		this.controller = controller;
	}
	
	
	

	@Override
	public void update() {
		if(controller.getPlayer().intersects(this)) {
			controller.setScore(controller.getScore()+POINTS);
			controller.getPlayer().addCoin();
			controller.destroyEntity(this);
		}
			

	}

}
