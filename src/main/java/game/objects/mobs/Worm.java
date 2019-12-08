package game.objects.mobs;

import game.algorithms.Algorithm;
import game.controllers.GameController;
import game.objects.mechanics.Damageable;
import game.objects.mechanics.Strikable;
import game.utils.Cutter;
import javafx.scene.image.Image;

public class Worm extends GravitableEnemy{

	private static final int IMAGE_RATE = 20;	//frames per image
	
	private static final double MOB_RANGE_DMG_INCREMENT = 0;	//mob range damage
	private static final double MOB_DMG_INCREMENT = 0;		//mob power
	
	private static final double MOB_HP_INCREMENT = 0;

	private static String GRAPHIC= "worm";

	//motion attributes
	private int imgIndex = 0;
	private int frameCount = 0;



	
	
	public Worm(double x, double y, int id, javafx.scene.image.Image graphicImage, double width, double height,GameController controller) {
		super(x, y, id, graphicImage, width, height,MOB_HP_INCREMENT, controller);
		initGraphics(graphicImage);
	}



	
	
	/**
	 * fills images list
	 */
	private void initGraphics(Image img) {					//monkey image have this format: [r r r r l l l l]

		for(Image i:Cutter.imageCutter(img, 2)) 			//cuts in 2 (right part and left part)
			for(Image motion : Cutter.imageCutter(i, 4)) 	//cuts to obtain motion images
				getImages().add(motion);

		setImage(getImages().get(0));

	}



	
	
	@Override
	public double getDMG() {
		double base = BASE_DMG + MOB_DMG_INCREMENT;
		double range = RANGE_DMG + MOB_RANGE_DMG_INCREMENT;
		return Algorithm.normal2(base, Math.sqrt(range), base-range, base+range);
	}



	
	
	@Override
	public void takeDMG(Strikable s) {
		if(getHealPoints()>0) {
			setHealPoints(getHealPoints()-s.getDMG());
			if(getHealPoints()<=0){
				getController().destroyEntity(this);
				//TODO fade animation
			}
			System.out.println("mob "+this.getClass()+" taked "+s.getDMG()+" damage from "+s.getClass());
		}
	}



	
	
	@Override
	public void update() {
		sufferGravityForce();

		if(isLeft())
			moveLeft();
		else
			moveRigth();
		motion();
		
		if(getController().getPlayer().intersects(this) && !isInCooldown()) {
			((Damageable)getController().getPlayer()).takeDMG(this);
			beginCoolDown();
		}
	}



	
	
	/**
	 * This method changes images with a frame rate
	 * This is what gives the idea of movement
	 */
	@Override
	public void motion() {
		frameCount = frameCount== IMAGE_RATE*getImages().size() ? 1 :  frameCount+1; 					//count the frames

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



	
	
	/**
	 * @return the image name
	 */
	public static String getGRAPHIC() {
		return GRAPHIC;
	}
}
