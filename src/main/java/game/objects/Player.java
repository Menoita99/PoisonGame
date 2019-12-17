package game.objects;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import game.algorithms.Algorithm;
import game.animations.FadeAnimation;
import game.controllers.GameController;
import game.objects.gameLogic.Key;
import game.objects.mechanics.CanvasObject;
import game.objects.mechanics.Damageable;
import game.objects.mechanics.Gravitable;
import game.objects.mechanics.Movable;
import game.objects.mechanics.Strikable;
import game.utils.Cutter;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Player extends Movable implements Gravitable, Damageable, Strikable{

	private static final int IMUNE_TIME = 1000; //milli
	private static final int ATTACK_TIME = 200; //milli
	private static final int LAYER = 2;
	public static final String GRAPHIC = "player2";
	private GameController gameController;

	private boolean isLeft = false;	
	private List<Image> images = new ArrayList<>();
	private int imgindex;
	
	
	//player stat multipliers
	private double damageMult = 1;
	private double speedMult = 1;
	private double jumpForce = 1;

	//player stats
	private double hp = 100;
	private int baseMove = 5;
	private int baseJump = 18;
	private int yVelocity = 0;
	private double baseDamage = 25;
	private double baseRange = 5;
	private boolean canJump = false;
	private boolean canAttack = true;

	private boolean buffed = false;

	private List<Key> keys = new LinkedList<>();
	private int coins = 0;

	private boolean imune = false;

	
	


	public Player(double x, double y, int id, Image graphicImage, double width, double height, GameController gameController) {
		super(x, y, id, graphicImage, width*0.80, height*0.95, LAYER, gameController.getCurrentLevelWidth());
		this.gameController = gameController;
		initGraphics(graphicImage);
	}





	@Override
	public void update() {
		//move();

		if(canAttack) attack();

		if(yVelocity < 10) {
			yVelocity++;
		}
		sufferGravityForce();
		
		motion();
		
		move();

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
		
		if(canAttack && canJump) {
			if(imgindex >= 4) imgindex = 0;
			else imgindex++;
			setImage(images.get(imgindex));
		}
		
		for (int i = 0; i < baseMove*speedMult; i++) {
			
			List<CanvasObject> objects = gameController.getObjectsAtLayer(getLayer());

			objects.addAll(gameController.getStaticEntities(getX()+1, getY()+1, getWidth(), getHeight()-2));
			
			for(CanvasObject entity : objects) {
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
		
		if(canAttack && canJump) {
			if(imgindex <= images.size()/2+3) imgindex = 15;
			else imgindex--;
			setImage(images.get(imgindex));
		}
		
		for (int i = 0; i < baseMove*speedMult; i++) {
			
			List<CanvasObject> objects = gameController.getObjectsAtLayer(getLayer());

			objects.addAll(gameController.getStaticEntities(getX()-1, getY()+1, getWidth(), getHeight()-2));
			
			for(CanvasObject entity : objects) {
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
			
			List<CanvasObject> objects = gameController.getObjectsAtLayer(getLayer());

			objects.addAll(gameController.getStaticEntities(getX()+1, getY()+1, getWidth()-2, getHeight()-1));
			objects.addAll(gameController.getStaticEntities(getX()+1, getY()-1, getWidth()-2, getHeight()));
			
			for(CanvasObject entity : objects) {
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

			canAttack = false;

			if (isLeft) {
				for(CanvasObject entity : gameController.getObjectsAtLayer(getLayer()))
					if(entity instanceof Damageable && !entity.equals(this))
						if(entity.getBoundary().intersects(getX()-getWidth(), getY()+1, getWidth(), getHeight()))
							((Damageable) entity).takeDMG(this);

				for(CanvasObject entity : gameController.getStaticEntities(getX()-getWidth(), getY()+1, getWidth(), getHeight()))
					if(entity instanceof Damageable && !entity.equals(this))
						((Damageable) entity).takeDMG(this);
			} else {
				for(CanvasObject entity : gameController.getObjectsAtLayer(getLayer()))
					if(entity instanceof Damageable && !entity.equals(this))
						if(entity.getBoundary().intersects(getX()+2*getWidth(), getY()+1, getWidth(), getHeight()))
							((Damageable) entity).takeDMG(this);

				for(CanvasObject entity : gameController.getStaticEntities(getX()+2*getWidth(), getY()+1, getWidth(), getHeight()))
					if(entity instanceof Damageable && !entity.equals(this))
						((Damageable) entity).takeDMG(this);

			}
			
			new Thread (() ->{
				try {
					if(isLeft) setImage(images.get(9));
					else setImage(images.get(5));
					Thread.sleep(ATTACK_TIME);
					if(isLeft) setImage(images.get(10));
					else setImage(images.get(6));
					Thread.sleep(ATTACK_TIME);
					canAttack = true; 
				} catch (InterruptedException e) { e.printStackTrace(); }
			}).start();

		}
	}
	
	
	
	
	/**
	 * When player takes damage, it takes a IMUNE_TIME for can suffer damage again
	 */
	@Override
	public void takeDMG(Strikable s) {
		if(!imune) {
			hp = hp - s.getDMG();
			gameController.writeText(getX(), getY(), Math.round(s.getDMG())+"", 1000, Color.RED);
			new FadeAnimation(this, 1, 100, 3, 0.3, 1).animate();

			if (hp <= 0)  
				gameController.endGame(false);
			else {
				imune = true;
				new Thread (() ->{
					try {
						Thread.sleep((long)IMUNE_TIME);
						imune = false; 
					} catch (InterruptedException e) { e.printStackTrace(); }
				}).start();
			}
		}
	}



	@Override
	public double getDMG() {
		int damage = (int) (baseDamage*damageMult);
		return Algorithm.normal2(damage, Math.sqrt(baseRange), damage-baseRange, damage+baseRange);
	}

	
	
	
	/**
	 * fills images list
	 */
	private void initGraphics(Image img) {			//image have this format: [r r r r r r r r l l l l l l l l]

		for(Image i:Cutter.imageCutter(img, 2)) 			//cuts in 2 (right part and left part)
			for(Image motion : Cutter.imageCutter(i, 8)) 	//cuts to obtain motion images
				images.add(motion);

		setImage(images.get(0));

	}
	
	/**
	 * This method changes images
	 */
	public void motion() {
		if(canAttack) {
			if (!canJump && isLeft) setImage(images.get(8));
			else if (!canJump && !isLeft) setImage(images.get(7));
			else if (canJump && isLeft) setImage(images.get(15));
			else if (canJump && !isLeft) setImage(images.get(0));
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