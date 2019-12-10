package game.objects.gameLogic;


import game.controllers.GameController;
import game.objects.mechanics.CanvasObject;
import javafx.scene.image.Image;

public class Door extends CanvasObject {
	
	public static final String GRAPHIC = "door";
	public static int LAYER = 1;
	
	private GameController controller;
	
	
	
	public Door(double x, double y, int id, Image graphicImage, double width, double height, GameController controller) {
		super(x, y-(height*0.25), id, graphicImage, width, height*1.25, LAYER);
		this.controller = controller;
	}

	
	
	@Override
	public void update() {
		if(controller.getPlayer().intersects(this)) 
			controller.endGame(true);
	}
}
