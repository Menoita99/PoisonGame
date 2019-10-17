package game.objects;

import game.algorithms.Algorithm;
import game.controllers.GameController;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Player extends Movable implements Gravitable{

	private static final int LAYER = 2;
	private GameController gameController;
	
	private static String GRAPHIC = "player";
	
	//player stats
	private int hp = 100;
	private boolean dead = false;
	private double damageMult = 1.0;
	private int speedMult = 1;
	private int jumpForce = 1;
	private boolean canJump = false;
	private int baseMove = 5;
	private int baseJump = 20;
	private int yVelocity = 0;
	


	public Player(double x, double y, int id, Image graphicImage, double width, double height, GameController gameController) {
		super(x, y, id, graphicImage, width, height, LAYER, gameController.getCurrentLevelWidth());
		this.gameController = gameController;
	}

	
	@Override
	public void update() {
		if (hp <= 0) dead = true;
		move();
		
		if (yVelocity < 10) {
			yVelocity++;
		}
		sufferGravityForce();
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
			yVelocity = yVelocity - (baseJump*jumpForce);
			canJump = false;
		}
	}
	
	/**
	 * This method moves the player to the right checking for collisions
	 */
	private void moveRight() {
	
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
					if(entity.getBoundary().intersects(getX()+1, getY()-1, getWidth()-2, getHeight()) && !entity.equals(this)){
						yVelocity = 0;
						return;
					}
				}
			}
			setY(getY() + (movingDown ? G_FORCE : -G_FORCE));
		}
	}
	
	

	
	
	//Getters & Setters
	
	
	public String getGRAPHIC() {
		return GRAPHIC;
	}
	
	
	public int getHP() {
		return hp;
	}
	public void setHP(int HP) {
		hp = HP;
	}

	
	public double getDamageMult() {
		return damageMult;
	}
	public void setDamageMult(int DM) {
		damageMult = DM;
	}
	
	
	public int getSpeed() {
		return speedMult;
	}
	public void setSpeed(int speed) {
		speedMult = speed;
	}
	
	
	public int getJumpForce() {
		return jumpForce;
	}
	
	public void setJumpForce(int JF) {
		jumpForce = JF;
	}
	
		
}