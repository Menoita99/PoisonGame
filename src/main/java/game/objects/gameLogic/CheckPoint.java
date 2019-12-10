package game.objects.gameLogic;

import java.util.ArrayList;
import java.util.List;

import game.controllers.GameController;
import game.objects.mechanics.CanvasObject;
import game.utils.Cutter;
import javafx.scene.image.Image;

public class CheckPoint extends CanvasObject {
	
	private static final int IMAGE_RATE = 15;
	public static final String GRAPHIC = "checkPoint";
	public static int LAYER = 1;
	
	private boolean isActive = false;
	private GameController controller;
	
	private List<Image> images = new ArrayList<>();
	
	//motion attributes
	private int imgIndex = 0;
	private int frameCount = 0;
	
	
	public CheckPoint(double x, double y, int id, Image graphicImage, double width, double height, GameController controller) {
		super(x, y, id, graphicImage, width/4, height, LAYER);
		this.controller = controller;
		initGraphics(graphicImage);
	}

	
	
	
	/**
	 * fills images list to get motion
	 */
	private void initGraphics(Image img) {				//graphicImage have this format: [on on off]
		for (Image i:Cutter.imageCutter(img, 3)) 
			images.add(i);
		
		setImage(images.get(2));						//off
	}	




	@Override
	public void update() {
		if(controller.getPlayer().intersects(this)) {
			setActive(true);
			controller.setCheckPoint(this);
		}
		motion();
	}

	
	
	/**
	 * Makes checkPoint animation
	 */
	private void motion() {
		frameCount = frameCount== IMAGE_RATE*images.size()-1 ? 1 :  frameCount+1; 	//set's frames counter 
																					//(-1) is to exclude off image
		if(isActive && frameCount%IMAGE_RATE == 0) {
			imgIndex = imgIndex == 0 ? 1 : 0;
			setImage(images.get(imgIndex));
		}
	}



	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
