package game.teste;

import javafx.scene.shape.Rectangle;

public class MainTest {



	public static void main(String[] args) {
		Rectangle r = new Rectangle(1365,300,30,300);
		Rectangle r1 = new Rectangle(1350,300,30,30);
		System.out.println(r.getBoundsInParent().intersects(r1.getBoundsInParent()));
		
		//CanvasTest.main(args);
	}
	
	
	
}
