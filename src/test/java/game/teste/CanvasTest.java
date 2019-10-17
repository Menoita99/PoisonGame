package game.teste;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class CanvasTest extends Application {

	@Override
	public void start(Stage window) throws Exception {
		Pane pane = new Pane();

		Canvas canvas = new Canvas();

		pane.getChildren().add(canvas);


		Scene scene = new Scene(pane,100,100);

		canvas.widthProperty().bind(scene.widthProperty());
		canvas.heightProperty().bind(scene.heightProperty());
		
		window.setScene(scene);
		window.show();
		
		new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				canvas.getGraphicsContext2D().setFill(Color.BLACK);
				canvas.getGraphicsContext2D().fillRect(125, 50, 100, 100);
				canvas.getGraphicsContext2D().setFill(Color.RED);
				canvas.getGraphicsContext2D().fillRect(25, 25, 100, 100);
				//canvas.setLayoutX(canvas.getLayoutX()+0.5);
			}
		}.start();
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
