package game.algorithms;

import org.apache.commons.math3.special.Erf;

public class Algorithm {

	//For n cases return a random n
	public static int randUniform (int numberCases) {
		double u = Math.random();
		for (int i=1; i<=numberCases; i++)
			if (u <= (1.0/(double)numberCases)*(double)i)
				return i;
		return 0;
	}

	// 50% Common out-1 // 30% Rare out-2 // 15% Epic out-3 // 5% Legendary out-4 //
	
	//Usa o enumerado rarity
	public static int randDropClass () {
		double u = Math.random();
		if (u <= 0.5) return 1;
		else if (u <= 0.8) return 2;
		else if (u <= 0.95) return 3;
		else return 4;
	}

	// 70% Normal out-1 // 30% Strong out-2 //
	public static int randMobClass () {
		double u = Math.random();
		if (u <= 0.7) return 1;
		else return 2;
	}

	// mob attack timer
	public static double triangle () { 
		return Math.sqrt(25*Math.random())+7;		//in seconds
	}

	public static double gaussian () {
		return Erf.erfInv(((130091176/66737197)*Math.random())-0.974652681323)*Math.sqrt(10)+25;
	}

	
	//generate variables with normal distribution with media and variance
	
	public static double uniform (int inf, int sup) {
		return (sup-inf)*Math.random()+inf;
	}
	
	public static double normal( double media, double var, double ddown, double dup ) {
		assert (var > 0 && ddown < dup);
		double p, p1, p2, x;
		do {
			do {
				p1 = uniform(-1, 1);
				p2 = uniform(-1, 1);
				p = p1 * p1 + p2 * p2;
			} while ( p >= 1. );
			x = media + var * p1 * Math.sqrt(-2 * Math.log(p) / p);
		} while ( x < ddown || x > dup);
		return x;
	}

	public static double normal2( double media, double var, double ddown, double dup ) {
		assert (var > 0 && ddown < dup);
		double x;
		do {
			x = media+var*Math.sqrt(-2*(Math.log10(Math.random())/Math.log10(Math.E)))*Math.cos(2*Math.PI*Math.random());
		} while ( x < ddown || x > dup);
		return x;
	}


}
