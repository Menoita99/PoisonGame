package game.algorithms;

public class Algorithm {

	//For n cases return a random n
	public int randUniform (int numberCases) {
		double u = Math.random();
		for (int i=1; i<=numberCases; i++)
			if (u <= (1.0/(double)numberCases)*(double)i)
				return i;
		return 0;
	}

	// 50% Common out-1 // 30% Rare out-2 // 15% Epic out-3 // 5% Legendary out-4 //
	public int randDropClass () {
		double u = Math.random();
		if (u <= 0.5) return 1;
		else if (u <= 0.8) return 2;
		else if (u <= 0.95) return 3;
		else return 4;
	}

	// 70% Normal out-1 // 30% Strong out-2 //
	public int randMobClass () {
		double u = Math.random();
		if (u <= 0.7) return 1;
		else return 2;
	}

	// mob attack timer
	public double triangle () {
		return Math.sqrt(25*Math.random())+7;
	}

	public double gaussian () {
		//return erfInv(((130091176/66737197)*Math.random())-0.974652681323)*Math.sqrt(10)+25;
		return 0;
	}

	
	//generate variables with normal distribution with media and variance
	
	public double uniform (int inf, int sup) {
		return (sup-inf)*Math.random()+inf;
	}
	
	public double normal( double media, double var ) {
		assert(var > 0);
		double p, p1, p2;
		do {
			p1 = uniform(-1, 1);
			p2 = uniform(-1, 1);
			p = p1 * p1 + p2 * p2;
		} while ( p >= 1. );
		return media + var * p1 * Math.sqrt(-2 * Math.log(p) / p);
	}


}
