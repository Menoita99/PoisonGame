package game.controls;

import game.objects.CanvasObject;
import javafx.scene.shape.Rectangle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;

/**
 * This class will create a responsive View Port giving a Scene object that
 * will be centred on a target object (CanvasObject)
 *  at a width rate of 1/2 and height rate 4/5
 * 
 * @author RuiMenoita
 *
 */
public class Camera {

	private Rectangle viewPort = new Rectangle(0,0,0,0);
	private CanvasObject targetObject;
	private DoubleProperty maxWidth = new SimpleDoubleProperty();

	
	public Camera(Scene scene,CanvasObject targetObject, DoubleProperty currentLevelWidth) {
		this.targetObject = targetObject;					//object that camera will focus on
		
		maxWidth.bind(currentLevelWidth);							//Responsive level width (Useful for different width level of hell)
		viewPort.widthProperty().bind(scene.widthProperty());		//Responsive resize
		viewPort.heightProperty().bind(scene.heightProperty());
		
		initListeners();
	}
	
	
	private void initListeners() {
		targetObject.getPositionXProperty().addListener((obs, old, newValue) -> {		//focus object listener on X axis
			double newPositionX = newValue.doubleValue();
			double width = viewPort.getWidth();
			
			if ((newPositionX > width/2 || newPositionX <  width/2) && newPositionX>width/2 && newPositionX< (maxWidth.doubleValue() - (width/2)) ) {
				viewPort.setLayoutX((newPositionX - width/2));
			}
		});
		
		targetObject.getPositionYProperty().addListener((obs, old, newValue) -> {		//focus object listener on Y axis
			double newPositionY = newValue.doubleValue();
			double height = viewPort.getHeight();

			if ((newPositionY > 4*(height/5) || newPositionY <  4*(height/5)) && newPositionY> 4*(height/5)) {	//TODO add hell height
				viewPort.setLayoutY((newPositionY - 4*(height/5)));
			}
		});
		
		viewPort.widthProperty().addListener((obs, old, newValue) -> {			//when scene resizes this centralise viewPort at the target object
			double newPositionX = targetObject.getX();
			double width = newValue.doubleValue();
			
			if ((newPositionX > width/2 || newPositionX <  width/2) && newPositionX>width/2 && newPositionX< (maxWidth.doubleValue() - (width/2)) ) {
				viewPort.setLayoutX((newPositionX - width/2));
			}
		});
		
		
		viewPort.heightProperty().addListener((obs, old, newValue) -> {			//when scene resizes this centralise viewPort at the target object
			double newPositionY = targetObject.getY();
			double height = newValue.doubleValue();

			if ((newPositionY > 4*(height/5) || newPositionY <  4*(height/5)) && newPositionY> 4*(height/5)) {	//TODO add hell height
				viewPort.setLayoutY((newPositionY - 4*(height/5)));
			}
		});
		
	}


	public Rectangle getViewPort() {
		return viewPort;
	}


	/**
	 * @return the maxWidth
	 */
	public DoubleProperty getMaxWidthPorperty() {
		return maxWidth;
	}
}
