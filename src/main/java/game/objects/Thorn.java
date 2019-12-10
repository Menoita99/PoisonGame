package game.objects;

import game.algorithms.Algorithm;
import game.controllers.GameController;
import game.objects.mechanics.CanvasObject;
import game.objects.mechanics.Strikable;
import javafx.scene.image.Image;

public class Thorn extends CanvasObject implements Strikable{
	
	public static final String GRAPHIC = "thorn";
	public static int LAYER = 2;
	public static int BASE = 15;
	public static int RANGE = 20;
	
	private GameController controller;

	
	
	
	
	
	public Thorn(double x, double y, int id, Image graphicImage, double width, double height,GameController controller) {
		super(x, y+(height*2/3), id, graphicImage, width, height*1/3, LAYER);
		this.controller = controller;
	}

	
	
	
	
	
	@Override
	public void update() {
		if(controller.getPlayer().intersects(this))
			controller.getPlayer().takeDMG(this);
	}






	@Override
	public double getDMG() {
		return Algorithm.normal(BASE, Math.sqrt(RANGE), Math.min(BASE-RANGE,0), BASE+RANGE);
	}

}
