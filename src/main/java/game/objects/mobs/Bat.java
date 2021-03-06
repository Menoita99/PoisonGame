package game.objects.mobs;

import game.algorithms.Algorithm;
import game.controllers.GameController;
import game.objects.mechanics.Damageable;
import game.objects.mechanics.Strikable;
import game.utils.Cutter;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Bat extends Enemy {

	private static final int IMAGE_RATE = 5;	//frames per image

	public static String GRAPHIC= "bat";
	
	private static final double MOB_RANGE_DMG_INCREMENT = 0;	//mob range damage
	private static final double MOB_DMG_INCREMENT = 0;		//mob power

	private static final double MOB_HP_INCREMENT = 0;

	
	private boolean inCooldown = false;
	
	//motion attributes
	private int imgIndex = 0;
	private int frameCount = 0;

	
	
	

	public Bat(double x, double y, int id, Image graphicImage, double width, double height, GameController controller) {
		super(x, y, id, graphicImage, width, height,MOB_HP_INCREMENT, controller);
		initGraphics(graphicImage);
	}


	/**
	 * fills images list
	 */
	private void initGraphics(Image img) {			//graphicImage have this format: [r r r r l l l l]

		for(Image i:Cutter.imageCutter(img, 2)) 			//cuts in 2 (right part and left part)
			for(Image motion : Cutter.imageCutter(i, 4)) 	//cuts to obtain motion images
				getImages().add(motion);

		setImage(getImages().get(0));
	}


	@Override
	public void update() {
		if(isLeft())
			moveLeft();
		else
			moveRigth();
		motion();
		
		if(!inCooldown) {
			getController().addEntity(new BatAcidlAttack(getX(), getY()+getHeight()+1, getController()));
			beginCoolDown();
		}
		
	}


	
	
	
	@Override
	public void motion() {
		frameCount = frameCount== IMAGE_RATE*getImages().size() ? 1 :  frameCount+1; 	//set's frames counter

		if(frameCount%IMAGE_RATE == 0 || isDirectionChanged() ) {
			setDirectionChanged(false);

			if(isLeft()) {
				if(imgIndex < getImages().size()/2) 	
					imgIndex =  getImages().size()-imgIndex-1;											//Set left Images [4,5,6,7]
					
				else
					imgIndex = imgIndex == getImages().size()-1 ? getImages().size()/2 : imgIndex+1; 	//if index == list.size() -1 than index will be list.size()/2
			}else {																						//otherwise it will increment by 1
				if(imgIndex >= getImages().size()/2) 
					imgIndex = getImages().size()-imgIndex-1;											//Set right Images [0,1,2,3]
				else
					imgIndex = imgIndex == getImages().size()/2 -1 ? 0 : imgIndex+1; 					//if index == list.size()/2 -1 than index will be 0
			}																							//otherwise it will increment by 1

			setImage(getImages().get(imgIndex));
		}
	}



	
	@Override
	public void takeDMG(Strikable s) {
		if(getHealPoints()>0) {
			getController().writeText(getX(), getY(), Math.round(s.getDMG())+"", 1000, Color.DEEPSKYBLUE);
			setHealPoints(getHealPoints()-s.getDMG());
			if(getHealPoints()<=0){
				getController().destroyEntity(this);
				dropItem();
			}
		}
	}



	
	
	@Override
	public double getDMG() {
		double base = BASE_DMG + MOB_DMG_INCREMENT;
		double range = RANGE_DMG + MOB_RANGE_DMG_INCREMENT;
		return Algorithm.normal(base, Math.sqrt(range), base-range, base+range);
	}


	
	
	
	
	/**
	 * This method when called set's inCooldown to true and starts cool down timer.
	 * when cool down timer ends, inCooldown is set to false
	 */
	public void beginCoolDown() {//TODO triangle parameters
		if(!inCooldown ) {
			inCooldown = true;
			new Thread (() ->{
				try {
					Thread.sleep((long)Algorithm.triangle()*1000);
					inCooldown = false;
				} catch (InterruptedException e) { e.printStackTrace(); }
			}).start();
		}
	}

	
	
	
	
	/**
	 * Class that represents the bat special attack
	 */
	private class BatAcidlAttack extends GravitableEnemy{
		

		
		
		
		@SuppressWarnings("static-access")
		public BatAcidlAttack(double x, double y,GameController controller) {
			super(x, y, controller.getNewId(), controller.getGraphic("acid"), controller.BLOCKS_SIZE/2, controller.BLOCKS_SIZE/3,-30, controller);
			initAttackGraphics(getImage());
			
			new Thread (() ->{
				try {
					Thread.sleep(5*1000);
					getController().destroyEntity(this);
				} catch (InterruptedException e) { e.printStackTrace(); }
			}).start();
		}


		
		
		
		/**
		 * This method cut's the image and fill's the images list with motion images
		 * @param img image to be cut in motion images
		 */
		private void initAttackGraphics(Image img) {
			for(Image i:Cutter.imageCutter(img, 2)) 			
					super.getImages().add(i);

			super.setImage(getImages().get(0));
		}


		
		
		
		
		@Override
		public double getDMG() {
			double base = BASE_DMG + MOB_DMG_INCREMENT;
			double range = RANGE_DMG + MOB_RANGE_DMG_INCREMENT;
			return Algorithm.normal(base, Math.sqrt(range), base-range, base+range);
		}


		
		
		
		
		@Override
		public void takeDMG(Strikable s) {
			if(s.getDMG()>0)
				getController().destroyEntity(this);
		}

		
		
		
		
		
		@Override
		public void motion() {
			if(getYVelocity() == 0)
				super.setImage(super.getImages().get(1));
			else
				super.setImage(super.getImages().get(0));
		}


		
		
		
		
		@Override
		public void update() {
			sufferGravityForce();
			
			if(getController().getPlayer().intersects(this)) {
				((Damageable)getController().getPlayer()).takeDMG(this);
				getController().destroyEntity(this);
			}
			
			motion();
		}
	}
}
