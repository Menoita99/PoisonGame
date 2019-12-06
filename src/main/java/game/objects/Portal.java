package game.objects;

import game.controllers.GameController;
import javafx.scene.image.Image;

public class Portal extends CanvasObject {
	
	private GameController controller;

	public Portal(double x, double y, int id, Image graphicImage, double width, double height, int layer, GameController controller) {
		super(x, y, id, graphicImage, width, height, layer);
		this.controller = controller;
	}

	@Override
	public void update() {
		if(controller.getPlayer().intersects(this)) {
			CheckPoint cp = controller.getLastActiveCheckPoint();
			controller.getPlayer().setX(cp.getX());
			controller.getPlayer().setY(cp.getY());
			System.out.println("TOUCHING PORTAL");
		}

	}

}
