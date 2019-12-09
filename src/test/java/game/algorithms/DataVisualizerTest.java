package game.algorithms;


public class DataVisualizerTest {

	private static final int N_TEST = 10000000;
	
	public static double round (double value, int precision) {
	    int scale = (int) Math.pow(10, precision);
	    return (double) Math.round(value * scale) / scale;
	}
	

	public static void main(String[] args) {
		
		double[] arr = new double[N_TEST];
//		
//		Map<String,Double> map = new HashMap<>();	
//		
//		for (int i = 0; i < arr.length; i++) 
//			arr[i] = Math.round(Algorithm.normal(25, Math.sqrt(5), 20, 30) * Math.pow(10, 1)) / Math.pow(10, 1) ;
//
//		DataVisualizer.writeData(arr, "normal");
		
	

//		for (int i = 0; i < arr.length; i++) 
//			arr[i] = Math.round(Algorithm.gaussian() * Math.pow(10, 1)) / Math.pow(10, 1) ;
//
//		DataVisualizer.writeData(arr, "g");
		
		
		
		for (int i = 0; i < arr.length; i++) 
			arr[i] = round(Algorithm.triangle(),2) ;

		DataVisualizer.writeData(arr, "triangle");
		
		
		/***
		 * 
		 * 
		for (int i = 0; i < arr.length; i++) 
			arr[i] = Algorithm.randDropClass();
		
		DataVisualizer.writeData(arr, "ARRrandDrop");
		
		for (int i = 0; i < arr.length; i++) 
			arr[i] = Algorithm.randMobClass();
		
		DataVisualizer.writeData(arr, "ARRrandMob");
		
		
		for (int i = 0; i < arr.length; i++) 
			arr[i] = Algorithm.randMobClass();
		
		for (int i = 0; i < arr.length; i++) {
			if(map.containsKey(arr[i]+""))map.put(arr[i]+"",map.get(arr[i]+"")+1);
			else map.put(arr[i]+"",1.0);
		}
			
		DataVisualizer.writeData(map, "MAPrandMob");
		
		 * 
		 */
		
		DataVisualizer.main(args);
	}
}
