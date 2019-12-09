package game.objects.drops;

import game.algorithms.Algorithm;
import game.controllers.GameController;
import game.objects.Player;
import game.objects.mechanics.CanvasObject;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Item extends CanvasObject {

	private static final long buffTimer = 10 * 1000; //milliseconds

	private Rarity rarity;

	public static final String GRAPHIC = "item";

	private GameController gameController;

	public static int LAYER = 1;


	public Item(double x, double y, int id, Image graphicImage, double width, double height, GameController controller) {
		super(x, y, id, graphicImage, width, height, LAYER);
		this.gameController = controller;
		this.rarity = getDropType();
				
	}


	/**
	 * This method uses Algorithm randDropClass method that returns the drop type
	 * @return returns the drop type
	 */
	private Rarity getDropType() {
		return Algorithm.randDropClass();
	}



	@Override
	public void update() {
		checkIfDropping();
	}



	
	
	/**
	 * Checks if player is colliding with the item (picking it up) and applies buff to player (random, depending on rarity)
	 */
	public void checkIfDropping() {
		if (gameController.getPlayer().intersects(this) && !gameController.getPlayer().isBuffed()) {
			switch (rarity) { 	
			case COMMON:
				applyBuffCOMMON(gameController.getPlayer());
				break;
			case RARE:
				applyBuffRARE(gameController.getPlayer());
				break;
			case EPIC:
				applyBuffEPIC(gameController.getPlayer());
				break;
			case LEGENDARY:
				applyBuffLEGENDARY(gameController.getPlayer());
				break;
			}
			gameController.destroyEntity(this);
		}
	}



	
	
	public void applyBuffCOMMON(Player player) {
		
		int specificBuff = Algorithm.randUniform(1);
		
		switch (specificBuff) { 	
		case 1: //hp
			healthBuff(player);
			break;
		}
	}



	
	
	public void applyBuffRARE(Player player) {
		
		int a = Algorithm.randUniform(2);
		
		switch (a) { 	
		case 1: //speed
			speedBuff(player);
			break;
		case 2: //jump
			jumpBuff(player);
			break;
		}
	}



	
	
	public void applyBuffEPIC(Player player) {
		
		int a = Algorithm.randUniform(1);
		
		switch (a) { 	
		case 1: //hp
			healthBuff(player);
			break;
		}
	}



	
	
	public void applyBuffLEGENDARY(Player player) {
		
		int a = Algorithm.randUniform(1);
		
		switch (a) { 	
		case 1: //hp
			healthBuff(player);
			break;
		}
	}



	
	
	//BUFFS
	
	/**
	 * Implements the health buff (healing 25hp)
	 * @param player 
	 */	
	private void healthBuff(Player player) {
		player.setHP( player.getHP() + 25 );
		if (player.getHP() > 100) player.setHP( 100 );
		gameController.writeText(getX(), getY(), 25+"", 1000, Color.GREEN);
		System.out.println("player healed, health is " + player.getHP()); //DEBUGGING
	}
	
	
	
	/**
	 * Implements the jump buff (jump boost 1.5x)
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
	 * Implements the speed buff (speed boost 2x)
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
