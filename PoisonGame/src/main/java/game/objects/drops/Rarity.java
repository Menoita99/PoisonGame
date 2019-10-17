package game.objects.drops;

public enum Rarity {
	
	COMMON(0.5), RARE(0.3) , EPIC(0.15), LEGENDARY(0.05);
	
	
	private double percentage; //expresses the class rarity

	private Rarity(double percentage) {
		this.percentage = percentage;
	}


	/**
	 * @return the percentage
	 */
	public double getPercentage() {
		return percentage;
	}
}
