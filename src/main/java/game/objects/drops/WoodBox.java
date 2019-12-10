package game.objects.drops;

import game.algorithms.Algorithm;
import game.controllers.GameController;
import game.objects.mechanics.CanvasObject;
import game.objects.mechanics.Damageable;
import game.objects.mechanics.Strikable;
import javafx.scene.image.Image;

public class WoodBox extends CanvasObject implements Damageable{

	public static final String GRAPHIC = "box";
	private static final int LAYER = 2;
	private GameController controller;



	public WoodBox(double x, double y, int id, Image graphicImage, double width, double height,GameController controller) {
		super(x, y, id, graphicImage, width, height, LAYER);
		this.controller = controller;
	}


	/**
	 * if player touch wood box from button this box takes damage
	 */
	@Override
	public void update() {
		setY(getY()+1);			//IM USING THIS UGLY ASS SHIT BECAUSE controller.getPlayer().getBoundery().intersects(x,y,w,h); return always true 
		setX(getX()+1);
		setWidth(getWidth()-2);
		
		if(controller.getPlayer().intersects(this))
			takeDMG(controller.getPlayer());
		
		setY(getY()-1);
		setX(getX()-1);
		setWidth(getWidth()+2);
	}


	@Override
	public void takeDMG(Strikable s) {
		controller.destroyEntity(this);
		if(Algorithm.randDropCoinOrItem() == 0) {
			Item drop = new Item(getX(), getY(), controller.getNewId(), controller.getGraphic(Item.GRAPHIC),  GameController.BLOCKS_SIZE, GameController.BLOCKS_SIZE, controller);
			controller.addEntity(drop);
		}else {
			Coin coin = new Coin(getX(), getY(), controller.getNewId(), controller.getGraphic(Coin.GRAPHIC),  GameController.BLOCKS_SIZE, GameController.BLOCKS_SIZE, controller);
			controller.addEntity(coin);
		}
	}
}
