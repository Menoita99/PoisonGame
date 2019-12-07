package game.objects.mobs;

import game.algorithms.Algorithm;
import game.controllers.GameController;
import game.objects.mechanics.CanvasObject;
import game.objects.mechanics.Strikable;
import game.utils.Cutter;
import javafx.scene.image.Image;

public class Bat extends Enemy {

	private static final int IMAGE_RATE = 5;	//frames per image

	private static String GRAPHIC= "bat";
	
	private static final double MOB_RANGE_DMG_INCREMENT = 0;	//mob range damage
	private static final double MOB_DMG_INCREMENT = 0;		//mob power

	private static final double MOB_HP_INCREMENT = 0;

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

//		FadeAnimation fa = new FadeAnimation(this,1,100,5,0.2,1);
//		fa.animate();
	}


	@Override
	public void update() {
		if(isLeft())
			moveLeft();
		else
			moveRigth();
		motion();
		
	}


	@Override
	public void motion() {
		frameCount = frameCount== IMAGE_RATE*getImages().size() ? 0 :  frameCount+1; 	//set's frames counter

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
			setHealPoints(getHealPoints()-s.getDMG());
			System.out.println("mob "+this.getClass()+" taked "+s.getDMG()+" damage from "+s.getClass());
		}else {
			getController().destroyEntity(this);
			dropItem();
			//TODO fade animation
		}
	}

	/**
	 * @return the GRAPHIC
	 */
	public static String getGRAPHIC() {
		return GRAPHIC;
	}
	
	
	
	
	

	@Override
	public double getDMG() {
		double base = BASE_DMG + MOB_DMG_INCREMENT;
		double range = RANGE_DMG + MOB_RANGE_DMG_INCREMENT;
		return Algorithm.normal(base, Math.sqrt(range), base-range, base+range);
	}
	
	
	
	
	
	/**
	 * Class that represents the bat special attack
	 */
	private class batSpecialAttack extends CanvasObject implements Strikable{


		public batSpecialAttack(double x, double y, int id, Image graphicImage, double width, double height,int layer) {
			super(x, y, id, graphicImage, width, height, layer);
			// TODO Auto-generated constructor stub
		}

		@Override
		public double getDMG() {
			//TODO Auto-generated constructor stub
			return 0;
		}

		@Override
		public void update() {
			// TODO Auto-generated method stub
			
		}
		
	}
}
