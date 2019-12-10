package game.objects.gameLogic;

import game.controllers.GameController;
import game.objects.mechanics.CanvasObject;
import javafx.scene.image.Image;

public class Portal extends CanvasObject {
	
	private GameController controller;
	public static final String GRAPHIC = "portal";
	public static int LAYER = 1;

	public Portal(double x, double y, int id, Image graphicImage, double width, double height, GameController controller) {
		super(x, y-height*0.25, id, graphicImage, width, height*1.25, LAYER);
		this.controller = controller;
	}

	@Override
	public void update() {
		if(controller.getPlayer().intersects(this)) {
			CheckPoint cp = controller.getCheckPoint();
			controller.getPlayer().setPosition(cp.getX(), cp.getY());		//Teleport's player to checkPoint
		}
	}

}
