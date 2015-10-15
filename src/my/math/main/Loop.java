package my.math.main;

import my.javagame.main.IDGameLoop;

public class Loop extends IDGameLoop{

	CollisionEqualMass coll;
	CollisionEqualMassMultie coll2;
	ElasticBallFun collFun;
	
	boolean collBool1 = false, collBool2 = false, collBool3 = true;
	
	public Loop(int width, int height) {
		super(width, height);
	}

	@Override
	public void init() {
		super.init();
		coll = new CollisionEqualMass();
		coll2 = new CollisionEqualMassMultie();
		collFun = new ElasticBallFun();
		if(collBool1)
			coll.init();
		if(collBool2)
			coll2.init();
		if(collBool3)
			collFun.init();
	}
	
	@Override
	public void tick(double deltaTime) {
		if(collBool1)
			coll.tick(deltaTime);
		if(collBool2)
			coll2.tick(deltaTime);
		if(collBool3)
			collFun.tick(deltaTime);
	}
	
	@Override
	public void render() {
		if(collBool1)
			coll.render(graphics2D);
		if(collBool2)
			coll2.render(graphics2D);
		if(collBool3)
			collFun.render(graphics2D);
		clear();
		super.render();
	}
	
	@Override
	public void clear() {
		super.clear();
	}

}
