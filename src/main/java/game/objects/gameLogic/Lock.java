package game.objects.gameLogic;

import game.controllers.GameController;
import game.objects.Player;
import game.objects.mechanics.CanvasObject;
import game.objects.mechanics.Damageable;
import game.objects.mechanics.Strikable;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Lock extends CanvasObject implements Damageable{
	
	public static int LAYER = 2;
	public static final String GRAPHIC = "Lock";
	private GameController controller;
	private Color color;

	
	
	public Lock(double x, double y, int id, Image graphicImage, double width, double height,Color color,GameController controller) {
		super(x, y, id, graphicImage, width, height, LAYER);
		this.color = color;
		this.controller = controller;
	}

	@Override
	public void update() {}

	
	

	public void unLock(Key key) {
		
	}

	
	/**
	 * This method verify if player can open this lock
	 * if key is present in the player key list
	 * this lock open and is deleted
	 * @param key 
	 */
	@Override
	public void takeDMG(Strikable s) {
		if(s instanceof Player) 
			for(Key key : ((Player)s).getKeys())
				if(key.getColor().equals(this.color))
					controller.destroyStaticEntity(this);
	}
}
