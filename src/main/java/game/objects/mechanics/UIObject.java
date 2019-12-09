package game.objects.mechanics;

import javafx.scene.canvas.GraphicsContext;

public interface UIObject {
	
	/**
	 * This method is called by layers that are rendering inside game loop
	 * @param gc canvas 2D graphic context 
	 * @param xOffSet camera x offset
	 * @param yOffSet camera y offset
	 */
	void render(GraphicsContext gc, double xOffSet, double yOffSet);
	
	/**
	 *Layer that will display this object 
	 */
	int getLayer();

	/**
	 * @return x coordinate
	 */
	double getX();
	
	/**
	 * @return y coordinate
	 */
	double getY();
	
	/**
	 * @return width value
	 */
	double getWidth();
	
	/**
	 * @return height value
	 */
	double getHeight();
}
