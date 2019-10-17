package game.objects;

public interface Gravitable {

	public static final double G_FORCE = 1;	//Gravity force pixels per frame
	
	
	/**
	 * This method must be implemented and used with an extension of CanvasObject
	 * This expresses the force to which the object is subjected  
	 */
	public void sufferGravityForce();
	
}
