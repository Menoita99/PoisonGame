package game.objects;

import game.controls.Camera;
import game.objects.mechanics.CanvasObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Background extends CanvasObject{

	private static String GRAPHIC= "background";

	private static final int LAYER = 0;


	public Background( int id, Image graphicImage,Camera camera) {
		super(0, 0, id, graphicImage, camera.getViewPort().widthProperty().doubleValue(), 
				camera.getViewPort().heightProperty().doubleValue(), LAYER);

		getWidthPorperty().bind(camera.getViewPort().widthProperty());		
		getHeightProperty().bind(camera.getViewPort().heightProperty());

	}


	@Override
	public void update() {} //we can change background image here, to night or something
	


	@Override
	public void render(GraphicsContext gc, double xOffSet, double yOffSet){
		update();
		if(getImage() != null)
			gc.drawImage(getImage(),getX(), getY(),getWidth(), getHeight());
	}


	/**
	 * @return the image name
	 */
	public static String getGRAPHIC() {
		return GRAPHIC;
	}
}
