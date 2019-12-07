package game.objects.mobs;

import game.controllers.GameController;
import game.objects.mechanics.CanvasObject;
import game.objects.mechanics.Gravitable;
import javafx.scene.image.Image;

public abstract class GravitableEnemy extends Enemy implements Gravitable{

	private static final double MAX_Y_VELOCITY = 10;	//pixel per frame

	private double yVelocity = 0;						


	
	
	public GravitableEnemy(double x, double y, int id, Image graphicImage, double width, double height,double hp, GameController controller) {
		super(x, y, id, graphicImage, width, height,hp, controller);
	}



	@Override
	public void sufferGravityForce() {

		setYVelocity(getYVelocity() + Gravitable.G_FORCE);						//add gravity force to Y component

		boolean movingDown = getYVelocity() > 0;								

		for (double i = 0; i < Math.abs(getYVelocity()); i += G_FORCE) {

			for (CanvasObject entity : getController().getObjectsAtLayer(getLayer())) {

				if(entity.intersects(this) && !entity.equals(this)) {
					
					if(movingDown) {
						if(entity.getBoundary().intersects(getX()+1, getY()+1, getWidth()-2, getHeight())){
							setYVelocity(0);
							return;
						}
					}else {
						if(entity.getBoundary().intersects(getX()+1, getY()-1, getWidth()-2, getHeight())) {
							setYVelocity(0);
							return;
						}
					}
				}
			}
			setY(getY()+ (movingDown ? G_FORCE : -G_FORCE));
		}
	}




	/**
	 * @return the yVelocity
	 */
	public double getYVelocity() {
		return yVelocity ;
	}


	/**
	 * @param yVelocity the yVelocity to set
	 */
	public void setYVelocity(double yVelocity) {
		if(yVelocity <= MAX_Y_VELOCITY && yVelocity>=0)
			this.yVelocity = yVelocity;
	}
}
