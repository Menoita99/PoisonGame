package game.algorithms;

import java.util.HashMap;
import java.util.Map;

public class DataVisualizerTest {

	private static final int N_TEST = 10000;

	public static void main(String[] args) {
		
		double[] arr = new double[N_TEST];
		
		Map<String,Double> map = new HashMap<>();	
		
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
		
		DataVisualizer.main(args);
	}
}
