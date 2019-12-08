package game.objects;

import java.util.ArrayList;
import java.util.List;

import game.controllers.GameController;
import game.objects.mechanics.CanvasObject;
import game.utils.Cutter;
import javafx.scene.image.Image;

public class Flag extends CanvasObject {
	
	private static final int IMAGE_RATE = 13;
	public static final String GRAPHIC = "flag";
	public static int LAYER = 1;
	
	private GameController controller;
	
	private List<Image> images = new ArrayList<>();
	
	//motion attributes
	private int imgIndex = 0;
	private int frameCount = 0;

	
	
	
	
	
	public Flag(double x, double y, int id, Image graphicImage, double width, double height, GameController controller) {
		super(x + (width/2-((width/2)/2)) , y, id, graphicImage, width/2, height, LAYER);
		this.controller = controller;
		initGraphics(graphicImage);
	}

	
	
	
	
	
	/**
	 * fills images list to get motion
	 */
	private void initGraphics(Image img) {				//graphicImage have this format: [up down]
		for (Image i:Cutter.imageCutter(img, 2)) 
			images.add(i);
		
		setImage(images.get(0));						//off
	}	

	
	
	
	
	
	@Override
	public void update() {
		if(controller.getPlayer().intersects(this)) 
			controller.endGame(true);
		
		motion();
	}
	
	
	
	
	/**
	 * Makes Flag animation
	 */
	private void motion() {
		frameCount = frameCount== IMAGE_RATE*images.size() ? 1 :  frameCount+1; 	//set's frames counter 
																					
		if(frameCount%IMAGE_RATE == 0) {
			imgIndex = imgIndex == 0 ? 1 : 0;
			setImage(images.get(imgIndex));
		}
	}

}
