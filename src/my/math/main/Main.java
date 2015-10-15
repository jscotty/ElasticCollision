package my.math.main;


import my.javagame.main.GameWindow;

public class Main {
	
	public static int width = 800;
	public static int height = 600;
	
	public static void main(String[] args) {
		GameWindow window = new GameWindow("Elastic Collision", width, height);

		window.addKeyListener(new CollisionEqualMass());
		window.addMouseListener(new ElasticBallFun());
		window.addMouseMotionListener(new ElasticBallFun());
		window.add(new Loop(800,600));
		window.setVisible(true);

	}

}
