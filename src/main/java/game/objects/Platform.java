package game.objects;

import java.util.ArrayList;
import java.util.List;

import game.controllers.GameController;
import game.utils.Cutter;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

public class Platform extends CanvasObject {

	private final static String GRAPHIC = "platform";
	private final static int LAYER = 2;

	private boolean fristFrame = true;

	private List<Image> images = new ArrayList<>();

	private GameController controller;

	
	
	public Platform(double x, double y, int id, Image graphicImage, double width, double height, GameController controller) {
		super(x, y, id, null, width, height, LAYER);
		this.controller = controller;

		for(Image motion : Cutter.imageCutter(graphicImage, 5)) 	//cuts to obtain motion images
			images.add(motion);										//images structure [solo,left,mid,right,centre]
	}


	@Override
	public void update() {
		if(fristFrame)
			selectImage();
		fristFrame=false;
	}


	/**
	 * Choose what images must platform have depending on neighbours
	 */
	private void selectImage() { 
		if(isObjectAt(Orientation.TOP)){	//centre
			setImage(images.get(4));
			return;
		}
		if((isObjectAt(Orientation.LEFT) && isObjectAt(Orientation.RIGHT)) ||  
				(!isObjectAt(Orientation.LEFT) && !isObjectAt(Orientation.RIGHT) && isObjectAt(Orientation.BOTTOM))){	//mid
			setImage(images.get(2));
			return;
		}
		if(isObjectAt(Orientation.LEFT) && !isObjectAt(Orientation.RIGHT)) {	//right
			setImage(images.get(3));
			return;
		}
		if(!isObjectAt(Orientation.LEFT) && isObjectAt(Orientation.RIGHT)) {	//left
			setImage(images.get(1));
			return;
		}
		setImage(images.get(0));	//solo
	}


	/**
	 * 
	 * @return true if there is a platform at given orientation 
	 */
	private boolean isObjectAt(Orientation orientation) {
		Point2D center = new Point2D(getX()+getWidth()/2,getY()+getHeight()/2);
		
		for(CanvasObject co :controller.getObjectsAtLayer(LAYER)) 
			if(co instanceof Platform)
				if(co.getBoundary().contains(center.getX()+orientation.getCoord().getX()*getWidth(),
										     center.getY()+orientation.getCoord().getY()*getHeight()))
					return true;
		return false;
	}


	public static String getGRAPHIC() {
		return GRAPHIC;
	}

}
