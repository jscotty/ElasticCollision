package my.math.main;

import java.awt.Graphics2D;
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import my.javagame.main.Dot;
import my.javagame.main.Vector2D;
import my.javagame.main.VectorOperations;

public class CollisionEqualMassMultie {

	private Dot[] balls;
	private int totballs = 6;
	
	Vector2D pos;
	Vector2D velocity;
	
	File bounce = new File("audio.wav");
	
	public CollisionEqualMassMultie() {
		
	}
	
	public void init(){
		balls = new Dot[totballs];
		
		for (int i = 0; i < balls.length; i++) {
			int r = (int) (Math.random() * 255);int g = (int) (Math.random() * 255);int b = (int) (Math.random() * 255);
			
			pos = new Vector2D((float)(Math.random() * 700 + 50) , (float)(Math.random() * 500 + 50) );
			velocity = new Vector2D((float)(Math.random() * 6 - 3), (float)(Math.random() * 6 - 3) );
			
			balls[i] = new Dot(pos,velocity, r,g,b, (int)(Math.random() * 20 + 30), 1, false);
			if(balls[i].radius >= 40){
				balls[i].mass = 2;
			}
			if(balls[i].radius >= 45){
				balls[i].mass = 3;
			}
		}
		
		
	}
	
	public void tick(double deltaTime){ 
		for (int i = 0; i < balls.length; i++) {
			balls[i].pos = new Vector2D(balls[i].pos.xPos + balls[i].velocity.xPos, balls[i].pos.yPos + balls[i].velocity.yPos);
			for (int j = i + 1; j < totballs; j++) {
					if(balls[i].pos.xPos > balls[j].pos.xPos && balls[i].pos.xPos < balls[j].pos.xPos + balls[j].radius / 2 && balls[i].pos.yPos > balls[j].pos.yPos && balls[i].pos.yPos < balls[j].pos.yPos + balls[j].radius / 2){
						balls[i].pos.setxPos((float)Math.random()*600+100);
					}
				
				colliding(balls[i], balls[j]);
				if(colliding(balls[i], balls[j]) == true){
					resolveCollision(balls[j], balls[i]);
					playSound(bounce);
				}
			}
			
			if (balls[i].xPos() < 0 || balls[i].xPos() > 800 - balls[i].radius) {
				balls[i].velocity.xPos = -balls[i].velocity.xPos;
				
	        }
			if (balls[i].yPos() < 0 || balls[i].yPos() > 600 - balls[i].radius*2) {
				balls[i].velocity.yPos = -balls[i].velocity.yPos;
	        }
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
		for (int i = 0; i < balls.length; i++) {
			balls[i].render(g);
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

}
