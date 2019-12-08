package game.objects.drops;

import game.algorithms.Algorithm;
import game.controllers.GameController;
import game.objects.Player;
import game.objects.mechanics.CanvasObject;
import javafx.scene.image.Image;

public class Item extends CanvasObject {

	private static final long buffTimer = 10 * 1000; //milliseconds
	private boolean dropped = false;

	private String type;

	public static final String GRAPHIC = "item";

	private GameController gameController;

	public static int LAYER = 1;


	public Item(double x, double y, int id, Image graphicImage, double width, double height, GameController controller) {
		super(x, y, id, graphicImage, width, height, LAYER);
		this.gameController = controller;
		this.type = getDropType();
				
	}


	/**
	 * This method uses Algorithm randDropClass method that returns the drop type
	 * @return returns the drop type
	 */
	private String getDropType() {
		Algorithm.randDropClass(); 	//sugestão usar o output deste metodo para o applyBuff
		return "jump";
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
				gameController.destroyEntity(this);
			}
	}


	public void applyBuff(Player player) {
		
		switch (type) { 	
		case "hp": //hp
			player.setHP( player.getHP() + 25 );
			if (player.getHP() > 100) player.setHP( 100 );
			break;
		case "speed": //speed
			speedBuff(player);
			break;
		case "jump": //jump
			jumpBuff(player);
			break;
		}
	}


	//BUFFS
	
	/**
	 * Implements the jump buff
	 * @param player 
	 */
	private void jumpBuff(Player player) {
		player.setJumpForce(1.5);
		player.setBuffed(true);
		new Thread(() ->{
			try {
				Thread.sleep(buffTimer);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			player.setJumpForce(1);
			player.setBuffed(false);
		}).start();
	}


	
	
	/**
	 * Implements the speed buff
	 * @param player 
	 */
	private void speedBuff(Player player) {
		player.setSpeedMult(2);
		player.setBuffed(true);
		new Thread(() ->{
			try {
				Thread.sleep(buffTimer);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			player.setSpeedMult(1);
			player.setBuffed(false);
		}).start();
	}
}
