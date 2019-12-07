package game.controls;

import javafx.scene.shape.Rectangle;
import game.objects.mechanics.CanvasObject;
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
	private DoubleProperty maxHeight = new SimpleDoubleProperty();

	private double yRatio = 0.65;


	public Camera(Scene scene,CanvasObject targetObject, DoubleProperty currentLevelWidth, DoubleProperty currentLevelHeight) {
		this.targetObject = targetObject;							//object that camera will focus on

		maxWidth.bind(currentLevelWidth);							//Responsive level width (Useful for different width level of hell)
		maxHeight.bind(currentLevelHeight);
		viewPort.widthProperty().bind(scene.widthProperty());		//Responsive resize
		viewPort.heightProperty().bind(scene.heightProperty());

		initListeners();
	}





	/**
	 * This method initialise the object listeners. When object moves the camera follow
	 */
	private void initListeners() {
		targetObject.getPositionXProperty().addListener((obs, old, newValue) -> {		//focus object listener on X axis
			double newPositionX = newValue.doubleValue();
			double width = viewPort.getWidth();
			double layoutXValue = Math.min(Math.max((newPositionX - width/2), 0),maxWidth.get()-width);

			if(viewPort.getX() != layoutXValue)
				viewPort.setLayoutX(layoutXValue);
		});

		targetObject.getPositionYProperty().addListener((obs, old, newValue) -> {		//focus object listener on Y axis
			double newPositionY = newValue.doubleValue();
			double height = viewPort.getHeight();
			double layoutYValue = Math.min(Math.max((newPositionY-(height*yRatio) ), 0),maxHeight.get()-height);

			if(viewPort.getY() != layoutYValue)
				viewPort.setLayoutY(layoutYValue);
		});

		viewPort.widthProperty().addListener((obs, old, newValue) -> {			//when scene resizes this centralise viewPort at the target object
			double newPositionX = targetObject.getX();
			double width = newValue.doubleValue();
			double layoutXValue = Math.min(Math.max((newPositionX - width/2), 0),maxWidth.get()-width);

			if(viewPort.getX() != layoutXValue)
				viewPort.setLayoutX(layoutXValue);
		});


		viewPort.heightProperty().addListener((obs, old, newValue) -> {			//when scene resizes this centralise viewPort at the target object
			double newPositionY = targetObject.getY();
			double height = newValue.doubleValue();
			double layoutYValue = Math.min(Math.max((newPositionY-(height*yRatio) ), 0),maxHeight.get()-height);

			if(viewPort.getY() != layoutYValue)
				viewPort.setLayoutY(layoutYValue);
		});

	}





	/**
	 * @return returns the view port
	 */
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
