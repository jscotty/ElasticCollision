
package my.math.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.event.MouseInputListener;

import my.javagame.main.Dot;
import my.javagame.main.Vector2D;
import my.javagame.main.VectorOperations;

public class ElasticBallFun implements MouseListener,MouseInputListener,MouseMotionListener{

	private CopyOnWriteArrayList<Dot> balls = new CopyOnWriteArrayList<Dot>();
	private int totballs = 200;
	
	Vector2D pos = new Vector2D(-100,-100);
	Vector2D velocity;
	
	File bounce = new File("audio.wav");
	
	private static float mouseX,mouseY;
	private static boolean released;
	private static boolean pressed;
	private static int rad = 10 , mass = 0;
	
	private int index = 0, yPos = 400, divide = 8;
	

	public ElasticBallFun() {
		
	}
	
	public void init(){
		
		
		for (int i = 0; i < totballs; i++) {
			int r = (int) (Math.random() * 255);int g = (int) (Math.random() * 255);int b = (int) (Math.random() * 255);
			
			velocity = new Vector2D(0,0);
			
			balls.add(new Dot(pos,velocity, r,g,b, 20, 2, false));
			
		}
		
		for (int i = 1; i < divide + 1; i++) {
			posBalls(i, divide);
		}
		
	}
	
	private void posBalls(int row, int divide){

		for (int x = 0; x < balls.size()/divide; x++) {
			pos = new Vector2D(x * (balls.get(index).radius + balls.get(index).radius/2) + 30, yPos + (balls.get(index).radius * row)  + ((balls.get(index).radius * row)/2));
			balls.get(index).pos = pos;
				
				//System.out.println(" x: "+pos.xPos + " y: "+pos.yPos + " | col: " + x );
			//System.out.println(row);
			index++;
		}
	}
	
	public void tick(double deltaTime){ 
		for (int i = 0; i < balls.size(); i++) {
			balls.get(i).pos = new Vector2D(balls.get(i).pos.xPos + balls.get(i).velocity.xPos, balls.get(i).pos.yPos + balls.get(i).velocity.yPos);
			for (int j = i + 1; j < totballs; j++) {
					
				
				colliding(balls.get(i), balls.get(j));
				if(colliding(balls.get(i), balls.get(j)) == true){
					resolveCollision(balls.get(j), balls.get(i));
				}
			}
			
			if (balls.get(i).xPos() < 0 + balls.get(i).radius/2  || balls.get(i).xPos() > 800 - balls.get(i).radius/2) {
				balls.get(i).velocity.xPos = -balls.get(i).velocity.xPos;
				
	        }
			if (balls.get(i).yPos() < 0 + balls.get(i).radius/2 || balls.get(i).yPos() > 600 - balls.get(i).radius/2) {
				balls.get(i).velocity.yPos = -balls.get(i).velocity.yPos;
	        }
		}

		if(released){
			int r = (int) (Math.random() * 255);int g = (int) (Math.random() * 255);int b = (int) (Math.random() * 255);
			Vector2D pos = new Vector2D(mouseX -10, mouseY - 40);
			Vector2D vel = new Vector2D(0, 2);
			balls.add(1,new Dot(pos,vel, r,g,b, rad, mass, false));
			
			released = false;
			pressed = false;
		}
	}
	
	public boolean colliding(Dot ball1, Dot ball2){
		Vector2D x1x2 = VectorOperations.substractVectors(ball1.pos, ball2.pos);
		Vector2D x2x1 = VectorOperations.substractVectors(ball2.pos, ball1.pos);
		Vector2D v1v2 = VectorOperations.substractVectors(ball1.velocity, ball2.velocity);
		Vector2D v2v1 = VectorOperations.substractVectors(ball2.velocity, ball1.velocity);
		
		float scal1 = 2*ball2.mass / (ball1.mass + ball2.mass) * VectorOperations.dotProduct(v1v2, x1x2)/(x1x2.getR() * x1x2.getR());
		float scal2 = 2*ball1.mass / (ball2.mass + ball1.mass) * VectorOperations.dotProduct(v2v1, x2x1)/(x1x2.getR() * x1x2.getR());	
		
		float radiusDif = (ball1.radius / 2) + (ball2.radius / 2);
		
		if(x1x2.getR() < radiusDif){
			ball1.velocity = VectorOperations.substractVectors(ball1.velocity, VectorOperations.scalairMultiplication(x1x2, scal1));
			ball2.velocity = VectorOperations.substractVectors(ball2.velocity, VectorOperations.scalairMultiplication(x2x1, scal2));
			return true;
		}else
			return false;
	}
	
	public void resolveCollision(Dot ball1, Dot ball2){
		Vector2D x1x2 = VectorOperations.substractVectors(ball1.pos, ball2.pos);
		Vector2D x2x1 = VectorOperations.substractVectors(ball2.pos, ball1.pos);
		Vector2D v1v2 = VectorOperations.substractVectors(ball1.velocity, ball2.velocity);
		Vector2D v2v1 = VectorOperations.substractVectors(ball2.velocity, ball1.velocity);
		
		float scal1 = 2*ball2.mass / (ball1.mass + ball2.mass) * VectorOperations.dotProduct(v1v2, x1x2)/(x1x2.getR() * x1x2.getR());
		float scal2 = 2*ball1.mass / (ball2.mass + ball1.mass) * VectorOperations.dotProduct(v2v1, x2x1)/(x1x2.getR() * x1x2.getR());	
		
		
		ball1.velocity = VectorOperations.substractVectors(ball1.velocity, VectorOperations.scalairMultiplication(x1x2, scal1));
		ball2.velocity = VectorOperations.substractVectors(ball2.velocity, VectorOperations.scalairMultiplication(x2x1, scal2));
		
	}
	
	public void render(Graphics2D g){
		for (int i = 0; i < balls.size(); i++) {
			balls.get(i).render(g);
		}
		
		if(pressed){
			rad++;mass++;
			g.setColor(Color.white);
			g.drawOval((int)mouseX - rad / 2,(int) mouseY - rad/2 , (int)rad, (int)rad);
			System.out.println((mass/5));
		} else {
			rad = 10;mass=0;
		}
	}
	
	static void playSound(File sound){
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(sound));

			clip.start();
		} catch (Exception e) {

		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			pressed = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			released = true;
		}
	}

}
