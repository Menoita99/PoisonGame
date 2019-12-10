package game.objects.mobs;

import game.algorithms.Algorithm;
import game.controllers.GameController;
import game.objects.mechanics.Damageable;
import game.objects.mechanics.Strikable;
import game.utils.Cutter;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Rat extends GravitableEnemy {
	
	private static final double MOB_RANGE_DMG_INCREMENT = 5;	//mob range damage
	private static final double MOB_DMG_INCREMENT = 0;		//mob power

	private static final double MOB_HP_INCREMENT = 1000;

	private static final int IMAGE_RATE = 6;		//frames per image

	public static String GRAPHIC= "rat";


	//motion attributes
	private int imgIndex = 0;
	private int frameCount = 0;


	public Rat(double x, double y, int id, Image graphicImage, double width, double height, GameController controller) {
		super(x, y, id, graphicImage, width, height, MOB_HP_INCREMENT, controller);
		System.out.println(getHealPoints());
		initGraphics(graphicImage);
	}





	/**
	 * fills images list
	 */
	private void initGraphics(Image img) {			//monkey image have this format: [r r r r l l l l]

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
			getController().writeText(getX(), getY(), Math.round(s.getDMG())+"", 1000, Color.DEEPSKYBLUE);
			setHealPoints(getHealPoints()-s.getDMG());
			if(getHealPoints()<=0){
				getController().destroyEntity(this);
				dropItem();
			}
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

		if(getController().getPlayer().intersects(this)) 
			((Damageable)getController().getPlayer()).takeDMG(this);
	}





	/**
	 * This method changes images with a frame rate
	 * This is what gives the idea of movement
	 */
	@Override
	public void motion() {
		frameCount = frameCount== IMAGE_RATE*getImages().size() ? 1 :  frameCount+1; 	//count the frames

		if(frameCount%IMAGE_RATE == 0 || isDirectionChanged() ) {
			setDirectionChanged(false);

			if(isLeft()) {
				if(imgIndex < getImages().size()/2)
					imgIndex =+ getImages().size()/2;													//Set left Images [4,5,6,7]
				else
					imgIndex = imgIndex == getImages().size()-1 ? getImages().size()/2 : imgIndex+1; 	//if index == list.size() -1 than index will be list.size()/2
			}else {																						//otherwise it will increment by 1
				if(imgIndex >= getImages().size()/2) 
					imgIndex -= 4;																		//Set right Images [0,1,2,3]
				else
					imgIndex = imgIndex == getImages().size()/2 -1 ? 0 : imgIndex+1; 					//if index == list.size()/2 -1 than index will be 0
			}																							//otherwise it will increment by 1

			setImage(getImages().get(imgIndex));
		}
	}
}