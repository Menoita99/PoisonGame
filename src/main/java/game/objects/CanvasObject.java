package game.objects;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *This class represents a game object that can be draw 
 *on canvas
 * 
 * @author RuiMenoita
 */
public abstract class CanvasObject {

	private Image image;
	private double opacity = 1;
	
	private DoubleProperty positionXProperty  = new SimpleDoubleProperty();
	private DoubleProperty positionYProperty  = new SimpleDoubleProperty();
	private DoubleProperty widthProperty  = new SimpleDoubleProperty();
	private DoubleProperty heightProperty  = new SimpleDoubleProperty();
	private int id;
	private int layer;
	


	/**
	 * If image is null this will display a rectangle instead
	 */
	public CanvasObject(double x, double y,int id, Image graphicImage ,double width,double height, int layer) {
		positionXProperty.set(x);
		positionYProperty.set(y);    
		this.heightProperty.set(height);
		this.widthProperty.set(width);
		this.image = graphicImage;
		this.id = id;
		this.layer = layer;
	}




	/**
	 * This method must be implemented when you wan't a permanent action.
	 */
	public abstract void update();



	/**
	 *This receives an graphicContext and draw this object at position <x, y> 
	 *if image is null it will draw a black rectangle
	 *Every time this method is called update() is called as well

	 */
	public void render(GraphicsContext gc, double xOffSet, double yOffSet){
		update();
		
		gc.setGlobalAlpha(opacity);					//set's the opacity of this entity
		
		if(image != null)
			gc.drawImage(image, getX()-xOffSet, getY()-yOffSet, getWidth(), getHeight());
		else {
			gc.setFill(Color.BLACK);
			gc.fillRect( getX()-xOffSet, getY()-yOffSet, getWidth(), getHeight());
		}
		
		gc.setGlobalAlpha(1);						//set's canvas opacity to default
	}


	/**
	 *Get's the boundary of this object, useful to detect collision  
	 */
	public Bounds getBoundary() {
		return new Rectangle(getX(), getY(),getWidth(),getHeight()).getBoundsInParent();
	}


	/**
	 * returns if a object is in contact with another object
	 */
	public boolean intersects(CanvasObject s){
		return s.getBoundary().intersects(this.getBoundary());
	}




	//Getters and Setters



	public void setImage(Image i){
		image = i;
	}

	public void setPosition(double x, double y){
		positionXProperty.set(x);
		positionYProperty.set(y); 
	}

	public void setX(double x) {
		positionXProperty.set(x);
	}

	public void setY(double y) {
		positionYProperty.set(y);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getX() {
		return positionXProperty.doubleValue();
	}

	public double getY() {
		return positionYProperty.doubleValue();
	}

	public DoubleProperty getPositionXProperty() {
		return positionXProperty;
	}

	public DoubleProperty getPositionYProperty() {
		return positionYProperty;
	}

	public DoubleProperty getWidthPorperty() {
		return widthProperty;
	}

	public DoubleProperty getHeightProperty() {
		return heightProperty;
	}

	public double getWidth() {
		return widthProperty.doubleValue();
	}

	public void setWidth(double width) {
		this.widthProperty.set(width);
	}

	public double getHeight() {
		return heightProperty.doubleValue();
	}

	public void setHeight(double height) {
		this.heightProperty.set(height);
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	public Image getImage() {
		return image;
	}
	
	public double getOpacity() {
		return opacity;
	}

	public void setOpacity(double opacity) {
		if(opacity>=0 && opacity<=1)
			this.opacity = opacity;
	}
}




