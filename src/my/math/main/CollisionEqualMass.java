package my.math.main;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import my.javagame.main.Dot;
import my.javagame.main.Vector2D;
import my.javagame.main.VectorOperations;

public class CollisionEqualMass implements KeyListener  {

	private Dot a;
	private Dot b;
	
	private Vector2D rA = new Vector2D(30,30);
	private Vector2D rB = new Vector2D(600,200);
	
	private Vector2D vA = new Vector2D(1,2);
	private Vector2D vB = new Vector2D(2,1);
	
	public CollisionEqualMass() {
		
	}
	
	public void init(){
		a = new Dot(rA,123, 50, 1, false);
		b = new Dot(rB,123, 150, 1, false);
		
	}
	
	public void tick(double deltaTime){
		rA = VectorOperations.addVectors(rA, vA);
		rB = VectorOperations.addVectors(rB, vB);
		
		a.getPos().setxPos(rA.xPos - a.getRadius() / 2);
		a.getPos().setyPos(rA.yPos - a.getRadius() / 2);
		
		b.getPos().setxPos(rB.xPos - b.getRadius() / 2);
		b.getPos().setyPos(rB.yPos - b.getRadius() / 2);

		reduceGravity();
		
		Vector2D x1x2 = VectorOperations.substractVectors(rA, rB);
		Vector2D x2x1 = VectorOperations.substractVectors(rB, rA);
		Vector2D v1v2 = VectorOperations.substractVectors(vA, vB);
		Vector2D v2v1 = VectorOperations.substractVectors(vB, vA);
		
		float scal1 = 2*b.getMass() / (a.getMass() + b.getMass()) * VectorOperations.dotProduct(v1v2, x1x2)/(x1x2.getR() * x1x2.getR());
		float scal2 = 2*a.getMass() / (b.getMass() + a.getMass()) * VectorOperations.dotProduct(v2v1, x2x1)/(x1x2.getR() * x1x2.getR());	
		
		float radiusDif = (a.getRadius() / 2) + (b.getRadius() / 2);
		
		if(x1x2.getR() < radiusDif){
			//System.out.println("hit");
			vA = VectorOperations.substractVectors(vA, VectorOperations.scalairMultiplication(x1x2, scal1));
			vB = VectorOperations.substractVectors(vB, VectorOperations.scalairMultiplication(x2x1, scal2));
		}
		
		if (a.getPos().xPos < 0 || a.getPos().xPos > 800 - a.getRadius()) {
            vA.xPos = -vA.xPos;
        } else if (a.getPos().yPos < 0 || a.getPos().yPos > 600 - a.getRadius()) {
            vA.yPos = -vA.yPos;
        }
		if (b.getPos().xPos < 0 || b.getPos().xPos > 800 - b.getRadius()) {
            vB.xPos = -vB.xPos;
        } else if (b.getPos().yPos < 0 || b.getPos().yPos > 600 - b.getRadius()) {
            vB.yPos = -vB.yPos;
        }
	}
	
	private void reduceGravity() {
		float grafA = vA.yPos;
		float grafB = vB.yPos;
		if(a.getPos().yPos < 600){
			grafA += 0.1f;
		} else {
			grafA = 0f;
			vA.setxPos(0);
		}if(b.getPos().yPos < 600){
			grafB += 0.1f;
		} else {
			grafB = 0f;
			vB.setxPos(0);
		}
		
		vA.setyPos(grafA);
		vB.setyPos(grafB);
		
	}

	public void render(Graphics2D g){
		a.render(g);
		b.render(g);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_SPACE){
			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_SPACE){
			
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}
