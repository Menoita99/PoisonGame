package game.objects;


import java.util.LinkedList;
import java.util.List;

import game.algorithms.Algorithm;
import game.controllers.GameController;
import game.objects.mechanics.CanvasObject;
import game.objects.mechanics.Damageable;
import game.objects.mechanics.Gravitable;
import game.objects.mechanics.Movable;
import game.objects.mechanics.Strikable;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Player extends Movable implements Gravitable, Damageable, Strikable{

	private static final int LAYER = 2;
	public static final String GRAPHIC = "transferir";
	private GameController gameController;
	
	private boolean isLeft = false;	

	//player stat multipliers
	private double damageMult = 1;
	private double speedMult = 1;
	private double jumpForce = 1;
	
	//player stats
	private double hp = 100;
	private int baseMove = 4;
	private int baseJump = 18;
	private int yVelocity = 0;
	private double baseDamage = 25;
	private double baseRange = 5;
	private boolean canJump = false;
	
	private boolean buffed = false;

	private List<Key> keys = new LinkedList<>();
	private int coins = 0;


	
	
	public Player(double x, double y, int id, Image graphicImage, double width, double height, GameController gameController) {
		super(x, y, id, graphicImage, width*0.95, height*0.95, LAYER, gameController.getCurrentLevelWidth());
		this.gameController = gameController;
	}



	
	
	@Override
	public void update() {
		if (hp <= 0)  gameController.endGame(false);

		move();

		attack();

		if (yVelocity < 10) {
			yVelocity++;
		}
		sufferGravityForce();
		

		if(getY()>gameController.getCurrentLevelHeight().get())	//ends game;
			gameController.endGame(false);
	}



	
	
	//Moving keys
	private void move() {
		if (gameController.isPressed(KeyCode.W) || gameController.isPressed(KeyCode.UP) || gameController.isPressed(KeyCode.SPACE) ) {
			jump();
		}

		if (gameController.isPressed(KeyCode.D) || gameController.isPressed(KeyCode.RIGHT) ) {
			moveRight();
		}

		if (gameController.isPressed(KeyCode.A) || gameController.isPressed(KeyCode.LEFT) ) {
			moveLeft();
		}
	}



	
	
	/**
	 * This method makes the player jump, not allowing for jumps while airborne
	 */
	private void jump() {
		if (canJump) {
			yVelocity = yVelocity - ((int) (baseJump*jumpForce));
			canJump = false;
		}
	}



	
	
	/**
	 * This method moves the player to the right checking for collisions
	 */
	private void moveRight() {
		isLeft = false;
		for (int i = 0; i < baseMove*speedMult; i++) {
			for(CanvasObject entity : gameController.getObjectsAtLayer(getLayer())) {
				if(entity.intersects(this) && !entity.equals(this)){
					if(entity.getBoundary().intersects(getX()+1, getY()+1, getWidth(), getHeight()-2) && !entity.equals(this)) //checks collision and checks if entity is it self
						return;
				}
			}
			setX(getX()+1);	
		}
	}



	
	
	/**
	 * This method moves the player to the left checking for collisions
	 */
	private void moveLeft() {
		isLeft = true;
		for (int i = 0; i < baseMove*speedMult; i++) {
			for(CanvasObject entity : gameController.getObjectsAtLayer(getLayer())) {
				if(entity.intersects(this) && !entity.equals(this)){
					if(entity.getBoundary().intersects(getX()-1, getY()+1, getWidth(), getHeight()-2) && !entity.equals(this)) //checks collision and checks if entity is it self
						return;
				}
			}
			setX(getX()-1);	
		}
	}



	
	
	/**
	 * This method expresses the effect of the force to which the object is subjected
	 */
	@Override	
	public void sufferGravityForce() {
		boolean movingDown = yVelocity > 0;

		for (int i = 0; i < Math.abs(yVelocity); i++) {
			for(CanvasObject entity : gameController.getObjectsAtLayer(getLayer())) {
				if (movingDown) {
					if(entity.getBoundary().intersects(getX()+1, getY()+1, getWidth()-2, getHeight()-1) && !entity.equals(this)){
						yVelocity = 0;	
						canJump = true;
						return;
					}
				} else {
					if( ( entity.getBoundary().intersects(getX()+1, getY()-1, getWidth()-2, getHeight()) && !entity.equals(this) ) || getY()<=0 ){
						yVelocity = 0;
						return;
					}
				}
			}
			canJump = false;
			setY(getY() + (movingDown ? G_FORCE : -G_FORCE));
		}
	}



	
	
	private void attack() {
		if ( gameController.isPressed(KeyCode.X) || gameController.isPressed(KeyCode.M) ) {
			if (isLeft) {
				//play left attack animation
				for(CanvasObject entity : gameController.getObjectsAtLayer(getLayer()))
					if(entity instanceof Damageable && !entity.equals(this))
						if(entity.getBoundary().intersects(getX()-getWidth(), getY(), getWidth(), getHeight()))
							((Damageable) entity).takeDMG(this);
			}
			else {
				//play right attack animation
				for(CanvasObject entity : gameController.getObjectsAtLayer(getLayer()))
					if(entity instanceof Damageable && !entity.equals(this))
						if(entity.getBoundary().intersects(getX()+getWidth(), getY(), getWidth(), getHeight()))
							((Damageable) entity).takeDMG(this);
				
			}
		}
	}



	//Getters & Setters



	public double getHP() {
		return hp;
	}



	
	
	public void setHP(double HP) {
		hp = HP;
	}



	
	
	public double getDamageMult() {
		return damageMult;
	}



	
	
	public void setDamageMult(int DM) {
		damageMult = DM;
	}



	
	
	public double getSpeedMult() {
		return speedMult;
	}



	
	
	public void setSpeedMult(double d) {
		speedMult = d;
	}



	
	
	public double getJumpForce() {
		return jumpForce;
	}



	
	
	public void setJumpForce(double d) {
		jumpForce = d;
	}



	
	
	public boolean isBuffed() {
		return buffed;
	}



	
	
	public void setBuffed(boolean buffed) {
		this.buffed = buffed;
	}



	
	
	public static int getPlayerLayer() {
		return LAYER;
	}



	
	
	@Override
	public void takeDMG(Strikable s) {
		hp = hp - s.getDMG();
		gameController.writeText(getX(), getY(), Math.round(s.getDMG())+"", 1000, Color.RED);
	}



	
	
	@Override
	public double getDMG() {
		int damage = (int) (baseDamage*damageMult);
		return Algorithm.normal2(damage, Math.sqrt(baseRange), damage-baseRange, damage+baseRange);
	}
	
	
	
	
	/**
	 * Adds key to player keys
	 * @param key
	 */
	public void addKey(Key key) {
		keys.add(key);
	}
	
	
	
	
	
	/**
	 * gets player keys
	 */
	public List<Key> getKeys() {
		return keys;
	}




	/**
	 * @return the coins
	 */
	public int getCoins() {
		return coins;
	}





	/**
	 * increment coin value
	 */
	public void addCoin() {
		coins ++;
	}
	
}