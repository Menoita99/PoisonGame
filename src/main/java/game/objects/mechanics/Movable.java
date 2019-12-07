package game.objects.mechanics;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;

public abstract class Movable extends CanvasObject {

	
	private DoubleProperty levelWidth = new SimpleDoubleProperty();
	
	
	public Movable(double x, double y, int id, Image graphicImage, double width, double height, int layer,DoubleProperty currentLevelWidth) {
		super(x, y, id, graphicImage, width, height, layer);

		levelWidth.bind(currentLevelWidth);
	}

	
	
	/**
	 *If x<0 && y< 0 && (x+getWidth())<=levelWidth) this will do nothing  
	 */
	@Override
	public void setPosition(double x, double y){
		if(x>=0 && y>=0 && (x+getWidth()) <= getLevelWidth())   
			super.setPosition(x, y);
	}
	
	
	/**
	 *If x<0 && (x+getWidth())<=levelWidth) this will do nothing  
	 */
	@Override
	public void setX(double x) {
		if(x>=0 && (x+getWidth()) <= getLevelWidth()) 
			super.setX(x);
	}
	
	
	/**
	 *If y<0 this will do nothing  
	 */
	@Override
	public void setY(double y) {
		if(y>=0) //TODO field end 
			super.setY(y);
	}
	
	public DoubleProperty getLevelWidthProprety() {
		return levelWidth;
	}
	
	public double getLevelWidth() {
		return levelWidth.doubleValue();
	}
}
