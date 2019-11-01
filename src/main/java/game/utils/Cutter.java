package game.utils;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Cutter {

	
	//Utils
	
	
	/**
	 * Return a list with the images that result from the @param img cut in
	 * @param pieces
	 */
	public static List<Image> imageCutter(Image img, int pieces){
		List<Image> images = new ArrayList<Image>();
		
		ImageView view = new ImageView(img);
		
		double cutWidth = img.getWidth()/pieces;
		
		for (int i = 0; i*cutWidth < img.getWidth() ; i++) {
			
			view.setViewport(new Rectangle2D(i* cutWidth, 0, cutWidth, img.getHeight()));
			
			SnapshotParameters params = new SnapshotParameters();
			params.setFill(Color.TRANSPARENT);
			
			images.add(view.snapshot(params, new WritableImage((int) cutWidth,(int)img.getHeight())));
		}
		
		return images;
	}
	
}
