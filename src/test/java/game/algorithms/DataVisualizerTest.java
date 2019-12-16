package game.algorithms;


public class DataVisualizerTest {

	private static final int N_TEST = 10000000;
	
	public static double round (double value, int precision) {
	    int scale = (int) Math.pow(10, precision);
	    return (double) Math.round(value * scale) / scale;
	}
	 

	public static void main(String[] args) {
//		
		double[] arr = new double[N_TEST];
		
		for (int i = 0; i < arr.length; i++) 
			arr[i] = round(Algorithm.randUniform(10),2) ;
		DataVisualizer.writeData(arr, "randUniform(10)");
		
//		DataVisualizer.main(args);
	}
}
