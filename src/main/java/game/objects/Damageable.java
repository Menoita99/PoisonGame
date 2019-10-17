package game.objects;

public interface Damageable {
	
	/**
	 *	When there are a collision with a Strikable Object this method must be called
	 */
	void takeDMG(Strikable s);

}
