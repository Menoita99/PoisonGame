package game.objects.mobs;

import game.controllers.GameController;
import game.objects.CanvasObject;
import game.objects.Strikable;
import game.utils.Cutter;
import javafx.scene.image.Image;

public class Bat extends Enemy {

	private static final int IMAGE_RATE = 5;	//frames per image

	private static String GRAPHIC= "bat";

	//motion attributes
	private int imgIndex = 0;
	private int frameCount = 0;
	
	

	public Bat(double x, double y, int id, Image graphicImage, double width, double height,GameController controller) {
		super(x, y, id, graphicImage, width, height, controller);
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
	public double getDMG() {
		return getAttackDamage();
	}

	
	@Override
	public void takeDMG(Strikable s) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the GRAPHIC
	 */
	public static String getGRAPHIC() {
		return GRAPHIC;
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
