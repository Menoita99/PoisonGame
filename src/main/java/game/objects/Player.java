package game.objects;

import game.controllers.GameController;
import game.objects.mechanics.CanvasObject;
import game.objects.mechanics.Damageable;
import game.objects.mechanics.Gravitable;
import game.objects.mechanics.Movable;
import game.objects.mechanics.Strikable;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Player extends Movable implements Gravitable, Damageable{

	private static final int LAYER = 2;
	public static final String GRAPHIC = "transferir";
	private GameController gameController;
	
	//player stats
	private int hp = 100;
	private boolean dead = false;
	private double damageMult = 1.0;
	private double speedMult = 1;
	private double jumpForce = 1;
	private boolean canJump = false;
	private int baseMove = 4;
	private int baseJump = 18;
	private int yVelocity = 0;
	private boolean buffed = false;
	


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
			yVelocity = yVelocity - ((int) (baseJump*jumpForce));
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
	
		
	
	//Getters & Setters
	
	
	
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
		// TODO Auto-generated method stub
		System.out.println("Player was damaged by "+s.getDMG()+" and the mob was "+ s.getClass());
	}
	
		
}