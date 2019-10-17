package game.objects;

import javafx.scene.image.Image;

public class Platform extends CanvasObject {
	
	private final static String GRAPHIC = null;
	
	private final static int LAYER = 2;
	
	public Platform(double x, double y, int id, Image graphicImage, double width, double height) {
		super(x, y, id, graphicImage, width, height, LAYER);
	}

	@Override
	public void update() {}

	public static String getGRAPHIC() {
		return GRAPHIC;
	}

}
