package main;
import game.algorithms.Algorithm;
import game.controllers.ManagerController;

public class Main {
	
	/**
	 * Starts the engine
	 * 
	 * To run this program call this method
	 */
	
	public static void main(String[] args) {
		
		//
		for (int i=0; i<=10; i++)
			System.out.println(Algorithm.randDropClass());
		//
		
		ManagerController.main(args);
		//example.ExampleMain.main(args);
	}
}
