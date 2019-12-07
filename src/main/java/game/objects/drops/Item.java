package game.objects.drops;

import game.algorithms.Algorithm;
import game.controllers.GameController;
import game.objects.Player;
import game.objects.mechanics.CanvasObject;
import javafx.scene.image.Image;

public class Item extends CanvasObject {
	
	private static final long buffTimer = 10000; //milliseconds
	private boolean dropped = false;
	private final int rarity;
	
	public static final String GRAPHIC = "item";
	
	private GameController gameController;
	
	public static int LAYER = 1;
	
	
	public Item(double x, double y, int id, Image graphicImage, double width, double height, GameController controller) {
		super(x, y, id, graphicImage, width, height, LAYER);
		this.gameController = controller;
		rarity = Algorithm.randDropClass();
	}



	@Override
	public void update() {
		if (!dropped) checkIfDropping();
	}
	
	public void checkIfDropping() {
		for (CanvasObject entity : gameController.getObjectsAtLayer(Player.getPlayerLayer()))
			if (entity instanceof Player && entity.intersects(this) && !((Player) entity).isBuffed()) {
				dropped=true;
				applyBuff((Player) entity);
			}
	}
	
	
	public void applyBuff(Player player) {
		Thread thread;
		String jump = "jump";
		switch (jump) {
		case "hp": //hp
			player.setHP( player.getHP() + 25 );
			if (player.getHP() > 100) player.setHP( 100 );
			break;
		case "speed": //speed
			player.setSpeedMult(2);
			player.setBuffed(true);
			thread = new Thread(){
				public void run(){
					try {
						sleep(buffTimer);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					player.setSpeedMult(1);
					player.setBuffed(false);
				}
			};
			thread.start();
			break;
		case "jump": //jump
			player.setJumpForce(1.5);
			player.setBuffed(true);
			thread = new Thread(){
				public void run(){
					try {
						sleep(buffTimer);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					player.setJumpForce(1);
					player.setBuffed(false);
				}
			};
			thread.start();
			break;
		}
	}


	public String getBuff() {
		// TODO Auto-generated method stub
		return null;
	}


}
