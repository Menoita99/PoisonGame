package game.objects.mechanics;

import javafx.geometry.Point2D;

public enum Orientation {
	TOP(new Point2D(0,-1)),LEFT(new Point2D(-1,0)),RIGHT(new Point2D(1,0)),BOTTOM(new Point2D(0,1));
	
	private Point2D coord;

	
	private Orientation(Point2D coord) {
		this.coord = coord;
	}
	
	
	
	/**
	 * @return the coord
	 */
	public Point2D getCoord() {
		return coord;
	}

}
