package game.algorithms;

public class Algorithm {
	
	// For n cases return a random n
	public static int randUniform (int numberCases) {
		double u = Math.random();
		for (int i=1; i<=numberCases; i++)
			if (u <= (1.0/(double)numberCases)*(double)i)
				return i;
		return 0;
	}
	
	// 50% Common out-1 // 30% Rare out-2 // 15% Epic out-3 // 5% Legendary out-4 //
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

}
