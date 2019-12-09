package game.objects.mobs;

import game.controllers.GameController;
import game.objects.mechanics.Strikable;
import javafx.scene.image.Image;

public class Rat extends GravitableEnemy {
	
	//TODO
	

	public Rat(double x, double y, int id, Image graphicImage, double width, double height, double hp, GameController controller) {
		super(x, y, id, graphicImage, width, height, hp, controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getDMG() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void takeDMG(Strikable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void motion() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
