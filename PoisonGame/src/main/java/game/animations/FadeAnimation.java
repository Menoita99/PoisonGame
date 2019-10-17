package game.animations;


import game.objects.CanvasObject;

/**
 * This class represents an fade transition
 * 
 * @author RuiMenoita
 */
public class FadeAnimation extends Thread{

	private CanvasObject targetObject;
	private double opacity;
	private int time;					//this value is in milliseconds
	private int loops;					

	public final static int INFINITE = -1;
	private double minOpacity=0;
	private double maxOpcaity;


	public FadeAnimation(CanvasObject targetObject, double opacity, int time , int loops, double minOpacity , double maxOpcaity) {
		this.targetObject = targetObject;
		this.opacity = opacity>maxOpcaity ? maxOpcaity : opacity;
		this.time = time;
		this.loops = loops;
		this.minOpacity = minOpacity;
		this.maxOpcaity = maxOpcaity;

	}


	/**
	 * Changes opacity trough time
	 */
	@Override
	public void run() {
		double rate = (1/((double)time)) * (0.5);
		double step = 1/rate;
		int loop = 0;

		double lastOpacity = -1;

		while( loops == INFINITE || loop < loops ) {
			for (int i = 0; i < step; i++) {
				if(opacity <= minOpacity || opacity >= maxOpcaity)
					rate = -rate;

				opacity+=rate;
				if(Math.abs(lastOpacity-opacity)>= 0.05) {
					lastOpacity=opacity;
					targetObject.setOpacity(opacity);
				}
				try {Thread.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
			}
			loops++;
		}
	}




	public void animate() {
		this.start();
	}

}
